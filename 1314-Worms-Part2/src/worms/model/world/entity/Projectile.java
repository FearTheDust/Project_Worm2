package worms.model.world.entity;

import java.util.ArrayList;

import worms.model.Constants;
import worms.model.world.World;
import worms.util.Position;
import be.kuleuven.cs.som.annotate.*;

public abstract class Projectile extends GameObject {

	// TODO isValidAngle -- nominal -- add to constructor to check

	/**
	 * Initialize a projectile with a certain position, angle and force time.
	 * 
	 * @param world The world of this projectile.
	 * @param position The start position of this projectile.
	 * @param angle The angle of this projectile.
	 * @param forceTime The time a force is exerted on this projectile.
	 * @param propulsionYield The propulsionYield on this projectile.
	 * 
	 * @effect super(world, position)
	 * 
	 * @post | new.getAngle() == angle
	 * @post | new.getForceTime() == forceTime
	 * @post | new.getPropulsionYield() == propulsionYield
	 * 
	 * @throws IllegalArgumentException
	 * 			When the propulsionYield isn't a valid propulsionYield.
	 * 			| !isValidPropulsionYield(propulsionYield)
	 */
	public Projectile(World world, Position position, double angle, double forceTime, double propulsionYield) throws IllegalArgumentException {
		super(world, position);
		
		if(!isValidPropulsionYield(propulsionYield))
			throw new IllegalArgumentException("The propulsionYield must be a valid propulsionYield");
		
		this.angle = angle;
		this.forceTime = forceTime;
		this.propulsionYield = propulsionYield;
	}

	/**
	 * Returns the force exerted on the projectile.
	 */
	@Basic @Immutable
	public abstract double getForce();

	/**
	 * Returns the mass of the projectile.
	 */
	public abstract double getMass();

	/**
	 * Returns the density of the projectile.
	 */
	public abstract double getDensity();
	
	
	/**
	 * Checks whether the provided propulsionYield is valid.
	 * @param propulsionYield The propulsionYield to check.
	 * @return Whether the propulsionYield is between (inclusive) 0 and 100.
	 * 			| result == (propulsionYield >= 0 && propulsionYield <= 100)
	 */
	public static boolean isValidPropulsionYield(double propulsionYield) {
		return (propulsionYield >= 0 && propulsionYield <= 100);
	}
	
	/**
	 * Returns the propulsionYield on this projectile.
	 */
	@Basic @Immutable
	public double getPropulsionYield() {
		return this.propulsionYield;
	}
	
	private double propulsionYield;

	/**
	 * Returns whether this Projectile is alive.
	 * This will return false:
	 * when an object isn't in a world.
	 * When an object isn't in the boundaries of a world.
	 * When the livingProjectile in the world it lives in isn't this projectile.
	 * 
	 * @return Whether the worm is alive.
	 * TODO continue the formal documentation.
	 */
	public final boolean isAlive() {
		if(this.getWorld() == null)
			return false;
		
		if(!(this.getPosition().getX() >= 0 && this.getPosition().getX() <= this.getWorld().getWidth() &&
			this.getPosition().getY() >= 0 && this.getPosition().getY() <= this.getWorld().getHeight()))
				return false;
			
		if(this.getWorld().getLivingProjectile() != this)
			return false;
				
		return true;
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

	/**
	 * Returns the radius of this Projectile.
	 */
	public double getRadius() {
		return Math.pow((this.getMass() * 3.0) / (getDensity() * 4.0 * Math.PI),(1 / 3));
	}

	/**
	 * Returns the position where this worm would be at a certain time whilst jumping.
	 * 
	 * @param time The time of when we return the position.
	 * 
	 * @return	When the time equals 0 the current position will be returned.
	 * 			| if(time == 0) Then
	 * 			| result == this.getPosition(); 
	 * 
	 * @return Else return The position this worm has at a certain time in a jump.
	 * 			| Else
	 * 			| startSpeed = (this.getForce() / this.getMass()) * this.getForceTime()
	 * 			| startSpeedX = startSpeed * Math.cos(this.getAngle())
	 * 			| startSpeedY = startSpeed * Math.sin(this.getAngle())
	 * 
	 * 			| x = this.getPosition().getX() + (startSpeedX * time)
	 * 			| y = this.getPosition().getY() + (startSpeedY * time - Constants.EARTH_ACCELERATION * Math.pow(time,2) / 2)
	 * 			| result == new Position(x,y)
	 * 
	 * @throws IllegalArgumentException
	 * 			When time is a negative value.
	 * 			| (time < 0)
	 */
	public Position jumpStep(double time) throws IllegalArgumentException {
		if (time < 0)
			throw new IllegalArgumentException("The time can't be negative.");

		if (time == 0) {
			return this.getPosition();
		}

		// Calculation
		double startSpeed = (this.getForce() / this.getMass()) * this.getForceTime();

		double startSpeedX = startSpeed * Math.cos(this.getAngle());
		double startSpeedY = startSpeed * Math.sin(this.getAngle());

		double x = this.getPosition().getX() + (startSpeedX * time);
		double y = this.getPosition().getY()
				+ (startSpeedY * time - Constants.EARTH_ACCELERATION
						* Math.pow(time, 2) / 2);

		// Return
		return new Position(x, y);
	}

	/**
	 * Returns the jump time if jumped with this projectile's current angle and timeStep.
	 */
	public abstract double jumpTime(double timeStep);
	
	/**
	 * This worm jumps to a certain position calculated by a formula.
	 * 
	 * @effect The new position of this worm is calculated and set.
	 * 			| this.setPosition(this.jumpStep(this.jumpTime()))
	 */
	public void jump(double timeStep) {
			this.setPosition(this.jumpStep(this.jumpTime(timeStep)));
	}

}
