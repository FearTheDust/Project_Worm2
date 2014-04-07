package worms.model.world.entity;

import be.kuleuven.cs.som.annotate.*;
import worms.model.Constants;
import worms.util.Position;
import worms.util.Util;

public abstract class Projectile extends SphericalGameObject {

	/*
	 * TODO isValidAngle -- nominal -- add to constructor to check
	 */

	/**
	 * Initialize a projectile with a certain position, angle and force time.
	 * 
	 * @param position The start position of this projectile.
	 * @param angle The angle of this projectile.
	 * @param forceTime The time a force is exerted on this projectile.
	 * 
	 * @effect super(position)
	 * 
	 * @post this.getAngle() == angle
	 * @post this.getForceTime() == forceTime
	 */
	public Projectile(Position position, double angle, double forceTime) {
		super(position);
		this.angle = angle;
		this.forceTime = forceTime;
	}
	
	

	/**
	 * Returns the time the force is exerted on this projectile.
	 */
	@Basic
	@Immutable
	public double getForceTime() {
		return forceTime;
	}

	private final double forceTime;

	/**
	 * Returns the angle the projectile is facing.
	 */
	@Basic
	@Immutable
	public double getAngle() {
		return angle;
	}

	private double angle;

	public double getRadius() {
		return Math.pow((this.getMass() * 3.0) / (getDensity() * 4.0 * Math.PI), (1/3));
	}

	/**
	 * Returns the force exerted on the projectile.
	 */
	public abstract double getForce(double propulsionYield);

	/**
	 * Returns the mass of the projectile.
	 */
	public abstract double getMass();

	/**
	 * Returns the density of the projectile.
	 */
	public abstract double getDensity();

	/**
	 * Returns the position where this projectile would be at a certain time whilst jumping.
	 * 
	 * @param time The time of when we return the position.
	 * 
	 * @return	When the time equals 0 or the angle of this worm is greater than Math.PI, the current position will be returned.
	 * 			| if((time == 0) || (this.getAngle() > Math.PI)) Then
	 * 			| result == this.getPosition(); 
	 * 
	 * @return Else return The position this worm has at a certain time in a jump.
	 * 			| Else
	 * 			| force = 5 * this.getCurrentActionPoints() + this.getMass() * EARTH_ACCELERATION
	 * 			| startSpeed = (force / this.getMass()) * FORCE_TIME
	 * 			| startSpeedX = startSpeed * Math.cos(this.getAngle())
	 * 			| startSpeedY = startSpeed * Math.sin(this.getAngle())
	 * 
	 * 			| x = this.getPosition().getX() + (startSpeedX * time)
	 * 			| y = this.getPosition().getY() + (startSpeedY * time - EARTH_ACCELERATION * Math.pow(time,2) / 2)
	 * 			| result == new Position(x,y)
	 * 
	 * @throws IllegalArgumentException
	 * 			When time exceeds the time required to jump or time is a negative value.
	 * 			| (time > this.jumpTime() || time < 0)
	 */
	public Position jumpStep(double time, double propulsionYield) throws IllegalArgumentException {
		if (!Util.fuzzyLessThanOrEqualTo(time, jumpTime(propulsionYield)))
			throw new IllegalArgumentException(
					"The time can't be greater than the time needed to perform the whole jump. Time: "
							+ time + " and jumpTime: " + jumpTime(propulsionYield));
		if (time < 0)
			throw new IllegalArgumentException("The time can't be negative.");

		if (time == 0) {
			return this.getPosition();
		}

		if (this.getAngle() > Math.PI) { // TODO Math.PI / 2 shouldn't change
			// position when jumping from this.
			return this.getPosition();
		}

		// Calculation
		double startSpeed = (getForce(propulsionYield) / this.getMass())
				* getForceTime();

		double startSpeedX = startSpeed * Math.cos(this.getAngle());
		double startSpeedY = startSpeed * Math.sin(this.getAngle());

		double x = this.getPosition().getX() + (startSpeedX * time);
		double y = this.getPosition().getY()
				+ (startSpeedY * time - Constants.EARTH_ACCELERATION
						* Math.pow(time, 2) / 2);

		// Return
		return new Position(x, y);
	}

	// TODO: anglerestriction? botsing grond in rekening brengen?
	/**
	 * Returns the jump time if jumped with this projectile's current angle.
	 * 
	 * @return The time used to jump. When this projectile's angle is greater than Math.PI, 0 is returned.
	 * 			| If this.getAngle() > Math.PI Then 
	 * 			| result == 0
	 * 			| Else
	 * 			| startSpeed = (getForce() / this.getProjectileMass()) * getForceTime()
	 * 			| time = Math.abs(2*startSpeed * Math.sin(this.getAngle()) / Constants.EARTH_ACCELERATION);
	 * 			| result == time
	 */
	public double jumpTime(double propulsionYield) {
		if (this.getAngle() > Math.PI) {
			return 0;
		}
		// sin(2X) = 2sin(X)cos(X); so 2sin(X)cos(X)/cos(X) => 2sin(X) => return
		// 0 => time can never be negative.
		double startSpeed = (getForce(propulsionYield) / this.getMass())
				* getForceTime();
		double time = Math.abs(2 * startSpeed * Math.sin(this.getAngle())
				/ Constants.EARTH_ACCELERATION);

		return time;
	}

}
