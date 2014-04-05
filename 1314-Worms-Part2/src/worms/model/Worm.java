package worms.model;

import be.kuleuven.cs.som.annotate.*;
import worms.model.world.GameObject;
import worms.util.*;

/**
 *
 * Defensive
 * Position DONE
 * Shape DONE
 * Mass DONE
 * Name DONE
 * Jumping
 * Moving
 * 
 * Nominal
 * Direction DONE
 * Turning
 * 
 * Total
 * ActionPoint DONE
 * HitPoints DONE
 */

/**
 * A class representing worms with a position, a direction it's facing, a radius, a mass, an amount of action points and a name.
 * 
 * @author Derkinderen Vincent - Bachelor Informatica - R0458834
 * @author Coosemans Brent - Bachelor Informatica - R0376498
 * 
 * @Repository https://github.com/FearTheDust/Worm.git TODO Update to new project repository! --------------------------------
 * 
 * @invar	This worm's action points amount is at all times less than or equal to the maximum amount of action points allowed and greater than or equal to 0.
 * 			| 0 <= this.getCurrentActionPoints() <= this.getMaximumActionPoints()
 * 
 * @invar	| This worm's name is a valid name.
 * 			| isValidName(this.getName())
 * 
 * @invar	This worm's radius is higher than or equal to the minimum radius.
 * 			| this.getRadius() >= this.getMinimumRadius()
 * 
 * @invar	This worm's angle is at all times a valid angle.
 * 			| isValidAngle(this.getAngle())
 * 
 * @invar 	The mass of this worm follows, at all times, the formula:
 * 			| getDensity() * (4.0/3.0) * Math.PI * Math.pow(this.getRadius(),3) == this.getMass()
 *
 * @invar	The position of this worm is never null.
 *			| this.getPosition() != null 
 */
public class Worm extends GameObject {

	/**
	 * Acceleration on earth while falling.
	 */
	public static final double EARTH_ACCELERATION = 9.80665;
	
	/**
	 * The time a worm will exert a force on its body.
	 */
	public static final double FORCE_TIME = 0.5;
	
	/**
	 * Initialize a new worm with a certain position, angle, radius, name, a certain amount of action points and a certain amount of hit points.
	 * 
	 * @param position The position of the new worm.
	 * @param angle The angle of the new worm.
	 * @param radius The radius of the new worm.
	 * @param name The name of the new worm.
	 * @param actionPoints The amount of action points of the new worm.
	 * @param hitPoints The amount of hit points of the new worm.
	 * 
	 * @effect	This worm will be granted a provided position when valid.
	 * 			super(position)
	 * 
	 * @post	The angle of the new worm is equal to angle.
	 * 			| new.getAngle() == angle
	 * 
	 * @post	The radius of the new worm is equal to radius.
	 * 			| new.getRadius() == radius
	 * 
	 * @post	The name of the new worm is equal to name.
	 * 			| new.getName() == name
	 * 
	 * @effect	The current amount of action points for the new worm is equal to actionPoints. 
	 * 			In the case that actionPoints is greater than the amount of action points allowed, the maximum amount will be set.
	 * 			| this.setCurrentActionPoints(actionPoints)
	 * 
	 * @effect The current amount of hit points for the new worm is equal to hitPoints.
	 * 			In the case that hitPoints is greater than the amount of hit points allowed, the maximum amount will be set.
	 * 			| this.setCurrentHitPoints(hitPoints)
	 */
	@Raw
	public Worm(Position position, double angle, double radius, String name, int actionPoints, int hitPoints) {
		super(position);
		this.setAngle(angle);
		this.setRadius(radius);
		this.setName(name);
		this.setCurrentActionPoints(actionPoints);
		this.setCurrentHitPoints(hitPoints);
	}
	
	/**
	 * Initialize a new worm with a maximum amount of action points possible for this worm as well as the maximum amount of possible hit points for this worm.
	 * 
	 * @param position The position of the new worm.
	 * @param angle The angle of the new worm.
	 * @param radius The radius of the new worm.
	 * @param name The name of the new worm.
	 * 
	 * @effect	A new worm will be initialized with a position, angle, radius, name, the maximum amount of action points possible for the new worm 
	 * 				and the maximum amount of hit points possible for the new worm.
	 * 			| this(position, angle, radius, name, Integer.MAX_VALUE, Integer.MAX_VALUE)
	 */
	@Raw
	public Worm(Position position, double angle, double radius, String name) {
		this(position, angle, radius, name, Integer.MAX_VALUE, Integer.MAX_VALUE);
	}
	
	/**
	 * This worm jumps to a certain position calculated by a formula.
	 * 
	 * @post	The current amount of action points is 0 if the current angle isn't greater than Math.PI.
	 * 			| if(!(this.getAngle() > Math.PI)) Then
	 * 			| new.getCurrentActionPoints() == 0
	 * 
	 * @effect The new position of this worm is calculated and set if the current angle isn't greater than Math.PI.
	 * 			| if(!(this.getAngle() > Math.PI)) Then
	 * 			| this.setPosition(this.jumpStep(this.jumpTime()))
	 */
	public void jump() {
		if(!(this.getAngle() > Math.PI)) {
			this.setPosition(this.jumpStep(this.jumpTime()));
			this.setCurrentActionPoints(0);
		}
	}
	
	/**
	 * Returns the position where this worm would be at a certain time whilst jumping.
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
	public Position jumpStep(double time) throws IllegalArgumentException {
		if(!Util.fuzzyLessThanOrEqualTo(time, jumpTime()))
			throw new IllegalArgumentException("The time can't be greater than the time needed to perform the whole jump. Time: " + time + " and jumpTime: " + jumpTime());
		if(time < 0)
			throw new IllegalArgumentException("The time can't be negative.");
		
		if(time == 0) {
			return this.getPosition();
		}
		
		if(this.getAngle()> Math.PI) { //TODO Math.PI / 2  shouldn't change position when jumping from this.
			return this.getPosition();
		}
		
		//Calculation
		double force = 5 * this.getCurrentActionPoints() + this.getMass() * EARTH_ACCELERATION;
		double startSpeed = (force / this.getMass()) * FORCE_TIME;
		
		double startSpeedX = startSpeed * Math.cos(this.getAngle());
		double startSpeedY = startSpeed * Math.sin(this.getAngle());
		
		double x = this.getPosition().getX() + (startSpeedX * time);
		double y = this.getPosition().getY() + (startSpeedY * time - EARTH_ACCELERATION * Math.pow(time,2) / 2);
		
		//Return
		return new Position(x,y);
	}
	
	/**
	 * Returns the jump time if jumped with this worm's current angle.
	 * 
	 * @return The time used to jump. When this worm's angle is greater than Math.PI, 0 is returned.
	 * 			| If this.getAngle() > Math.PI Then 
	 * 			| result == 0
	 * 			| Else
	 * 			| force = 5 * this.getCurrentActionPoints() + this.getMass() * EARTH_ACCELERATION
	 * 			| startSpeed = (force / this.getMass()) * FORCE_TIME
	 * 			| time = Math.abs(2*startSpeed * Math.sin(this.getAngle()) / EARTH_ACCELERATION);
	 * 			| result == time
	 */
	public double jumpTime() {
		//this.getAngle() will automatically be less than 2*Math.PI because of the invariant.
		if(this.getAngle() > Math.PI) {
			return 0;
		}
		//sin(2X) = 2sin(X)cos(X); so 2sin(X)cos(X)/cos(X) => 2sin(X) => return 0   => time can never be negative.
		double force = 5 * this.getCurrentActionPoints() + this.getMass() * EARTH_ACCELERATION;
		double startSpeed = (force / this.getMass()) * FORCE_TIME;
		//double distance = (Math.pow(startSpeed, 2) * Math.sin(2 * this.getAngle())) / EARTH_ACCELERATION;
		//double time = Math.abs(distance / (startSpeed * Math.cos(this.getAngle()))); division by zero -- actually this works non the less it seems @see Project 1 testWorm
		double time = Math.abs(2*startSpeed * Math.sin(this.getAngle()) / EARTH_ACCELERATION);

		return time;
	}
	
	/**
	 * Move this worm a certain amount of steps in the direction it's facing.
	 * 
	 * @param steps The amount of steps this worm takes.
	 * 
	 * @post	The new position is the old position plus the amount of steps proportional to the direction this worm is facing.
	 * 			| new.getPosition() == new Position(
	 * 				this.getPosition().getX() + steps * Math.cos(this.getAngle()) * this.getRadius(),
	 * 				this.getPosition().getY() + steps * Math.sin(this.getAngle()) * this.getRadius())
	 * 
	 * @effect	The new action points amount is set to the old amount minus the cost to move.
	 * 			| this.setCurrentActionPoints(getCurrentActionPoints() - getMoveCost(steps,getAngle()));
	 * 
	 * @throws IllegalArgumentException
	 * 			If steps is smaller than zero or if this worm doesn't have enough action points.
	 * 			| steps < 0 || this.getMoveCost(steps,this.getAngle()) > this.getCurrentActionPoints()
	 */
	public void move(int steps) throws IllegalArgumentException {
		if(steps < 0)
			throw new IllegalArgumentException("Steps must be higher than or equal to zero");
		if(getMoveCost(steps,getAngle()) > getCurrentActionPoints())
			throw new IllegalArgumentException("You don't have enough Action Points");
		
		double x= this.getPosition().getX() + steps * Math.cos(getAngle()) * getRadius();
		double y= this.getPosition().getY() + steps * Math.sin(getAngle()) * getRadius();
		
		setCurrentActionPoints(getCurrentActionPoints()-getMoveCost(steps,getAngle()));
		setPosition(new Position(x,y));
	}
	
	/**
	 * Returns the cost to move a certain amount of steps in a certain direction.
	 * 
	 * @param steps The amount of steps to move.
	 * @param angle Determines the direction to move in.
	 * 
	 * @return 	The cost to move.
	 * 			| result == (int) (steps*Math.ceil((Math.abs(Math.cos(angle)) + Math.abs(4*Math.sin(angle)))))
	 */
	public static int getMoveCost(int steps, double angle){
		return (int) (steps * Math.ceil(Math.abs(Math.cos(angle)) + Math.abs(4*Math.sin(angle))) );
	}

	/**
	 * Returns the angle of this worm.
	 */
	@Basic
	public double getAngle() {
		return angle;
	}
	
	/**
	 * Turn this worm with a given angle.
	 * 
	 * @param angle The angle to turn with.
	 * 
	 * @pre		The absolute value of twice the angle must be valid or equal to Math.abs(Math.PI).
	 * 			| isValidAngle(Math.abs(2*angle)) || Util.fuzzyEquals(Math.abs(angle), Math.PI)
	 * 		
	 * @pre		The cost to turn should be less than or equal to the amount of action points we have.
	 * 			| this.getCurrentActionPoints() >= getTurnCost(angle)
	 * 
	 * @effect	This worm's new action points is set to the old amount of action points minus the cost to turn.
	 * 			| this.setCurrentActionPoints(this.getCurrentActionPoints() - getTurnCost(angle))
	 * 
	 * @effect	This worm's new direction is set to the old angle plus the given angle plus 2*Math.PI modulo 2*Math.PI.
	 * 			| this.setAngle(Util.modulo(this.getAngle() + angle + 2*Math.PI, 2*Math.PI));
	 */
	public void turn(double angle) {
		assert isValidAngle(Math.abs(2*angle)) || Util.fuzzyEquals(Math.abs(angle), Math.PI);
		assert this.getCurrentActionPoints() >= getTurnCost(angle);
		
		this.setAngle(Util.modulo(this.getAngle() + angle + 2*Math.PI, 2*Math.PI));
		this.setCurrentActionPoints(this.getCurrentActionPoints() - getTurnCost(angle));
	}
	
	/**
	 * Returns the cost to change the orientation to the angle formed by adding the given angle to the current orientation.
	 * 
	 * @param angle The angle to turn.
	 * 
	 * @return The cost to turn.
	 * 			| result == (int) Math.ceil(Math.abs(30 * (angle / Math.PI)))
	 */
	public static int getTurnCost(double angle) {
		return (int) Math.ceil(Math.abs(30 * (angle / Math.PI)));
	}
	
	/**
	 * The angle provided has to be greater than or equal to 0 and less than 2*Math.PI.
	 * 
	 * @param angle The angle to check.
	 * 
	 * @return	Whether or not the given angle is valid.
	 * 			| result == Util.fuzzyGreaterThanOrEqualTo(angle, 0) && (angle < 2*Math.PI)
	 */
	public static boolean isValidAngle(double angle){
		return Util.fuzzyGreaterThanOrEqualTo(angle, 0) && (angle < 2*Math.PI);
	}
	
	/**
	 * Set the new angle of this worm.
	 * 
	 * @param angle The new angle of this worm.
	 * 
	 * @pre		The angle provided has to be a valid angle.
	 * 			| isValidAngle(angle)
	 * 
	 * @post	The new angle of this worm is equal to the given angle.
	 * 			| (new.getAngle() == angle)
	 */
	private void setAngle(double angle) {
		assert isValidAngle(angle);
		this.angle = angle;
	}
	
	private double angle;
	
	/**
	 * Returns the radius of this worm.
	 */
	@Basic @Raw
	public double getRadius() {
		return radius;
	}
	
	/**
	 * Set the new radius of this worm and update the action points accordingly.
	 * Updating the action points means the difference between current and maximum amount of action points remains the same.
	 * If this would mean the action points will be less than 0, the current amount will be set to 0.
	 * 
	 * @param radius The new radius of this worm.
	 * 
	 * @post	The radius of this worm is equal to the given radius.
	 * 			| new.getRadius() == radius
	 * 
	 * @effect	The amount of action points is set to a new amount calculated by the difference in old maximum and current amount along with with the new Maximum.
	 * 			| APdiff = this.getMaximumActionPoints() - this.getCurrentActionPoints();
	 * 			| //Radius change
	 *			| this.setCurrentActionPoints(this.getMaximumActionPoints() - APdiff);
	 * 
	 * @throws IllegalArgumentException
	 * 			When the given radius is less than the minimum radius.
	 * 			| radius < this.getMinimumRadius()
	 * 
	 * @throws IllegalArgumentException
	 * 			When the radius isn't a number.
	 * 			| Double.isNaN(radius)
	 */
	@Raw
	public void setRadius(double radius) throws IllegalArgumentException {
		if(! Util.fuzzyGreaterThanOrEqualTo(radius, getMinimumRadius()))
			throw new IllegalArgumentException("The radius has to be greater than or equal to the minimum radius " + minRadius);
		if(Double.isNaN(radius))
			throw new IllegalArgumentException("The radius must be a number.");
		
		this.radius = radius;
	}
	
	/**
	 * Returns the minimum radius of this worm.
	 */
	@Basic @Immutable
	public double getMinimumRadius() {
		return minRadius;
	}
	
	private double radius;
	
	private final double minRadius = 0.25; //Initialize in constructor later on.
	
	/**
	 * Returns the mass of this worm.
	 */
	@Basic
	public double getMass() {
		return getDensity() * (4.0/3.0) * Math.PI * Math.pow(this.getRadius(),3);
	}

// In case of later use.
//	/**
//	 * Sets the mass of this worm.
//	 * @post	The mass of this worm is equal to the result of the formula "Mass = (getDensity()) * (4/3) * Math.PI * (radius)^3"
//	 * 			| new.getMass() == getDensity() * (4.0/3.0) * Math.PI * Math.pow(this.getRadius(),3)
//	 * 
//	 * @post	see setCurrentActionPoints(this.getCurrentActionPoints()) //@effect?
//	 */
//	@Raw
//	private void setMass() /*throws IllegalArgumentException*/ {
//		//We'd better use an argument if we use this...
//		/*if(this.radius >= Math.pow(Double.MAX_VALUE * (3.0/4.0) / DENSITY / Math.PI, 1/3))
//			throw new IllegalArgumentException();*/
//			
//		this.mass = getDensity() * (4.0/3.0) * Math.PI * Math.pow(this.getRadius(),3);
//		this.setCurrentActionPoints(this.getCurrentActionPoints());
//	}
//	private double mass;
	
	/**
	 * Returns this worm's density.
	 */
	@Basic @Immutable
	public static final double getDensity() {
		return DENSITY;
	}

	private static final double DENSITY = 1062;
	
	/**
	 * Returns this worm's name.
	 */
	@Basic @Raw
	public String getName() {
		return name;
	}
	
	/**
	 * Set a new name for this worm.
	 * 
	 * @param name The new name of this worm.
	 * 
	 * @post	The name of this worm is equal to name.
	 * 			| new.getName() == name
	 * 
	 * @throws IllegalArgumentException
	 * 			When name isn't a valid name.
	 * 			| !isValidName(name)
	 */
	@Raw
	public void setName(String name) throws IllegalArgumentException {
		if (!isValidName(name))
			throw new IllegalArgumentException("Invalid name.");
		
		this.name = name;
	}
	
	/**
	 * Returns whether the name is a valid name.
	 * 
	 * @param name The name to be checked.
	 * 
	 * @return  True if the name is longer than or equal to 2 characters, starts with an uppercase and when every character is one from the following:
	 * 			[A-Z] || [a-z] || a space || ' || "
	 * 			| result != ((name == null) &&
	 * 			| (name.length() < 2) &&
	 * 			| (!Character.isUpperCase(name.charAt(0)) &&
	 * 			| (for each index i in 0..name.toCharArray().length-1:
     *       	|   (!(Character.isLetter(name.toCharArray[i]) || name.toCharArray[i] == ' ' || name.toCharArray[i] == '\'' || name.toCharArray[i] == '\"'))))
	 */
	public static boolean isValidName(String name) {
		if(name == null)
			return false;
		if(name.length() < 2)
			return false;
		if(!Character.isUpperCase(name.charAt(0)))
				return false;
		for(Character ch : name.toCharArray()) {
			if(!(ch == ' ' || ch == '\'' || ch == '\"' || Character.isLetterOrDigit(ch)))
				return false;
		}
		return true;
	}
	
	private String name;	
	
	/**
	 * Set the current amount of hit points.
	 * 
	 * @param hitPoints The amount of hit points to set the current amount to.
	 * 
	 * @post	If hitPoints is greater than or equal to zero,
	 * 			The new hit points amount will be set to the minimum value, being hitPoints or getMaximumHitPoints()
	 * 			| if(hitPoints >= 0)
	 * 			| new.getCurrentHitPoints() == Math.min(hitPoints, this.getMaximumHitPoints)
	 * 
	 * @post	If the hitPoints is less than zero, zero will be set.
	 * 			| if(hitPoints < 0)
	 * 			| new.getCurrentHitPoints() == 0
	 */
	@Raw @Model
	private void setCurrentHitPoints(int hitPoints) {
		this.currentHitPoints = (hitPoints < 0) ? 0 : Math.min(hitPoints, getMaximumHitPoints());
	}
	
	/**
	 * Return the current amount of hit points in valid form.
	 */
	public int getCurrentHitPoints() {
		//Call set to make sure we're in valid bounds.
		setCurrentHitPoints(currentHitPoints);
		return currentHitPoints;
	}
	
	/**
	 * Returns this worm's maximum amount of hit points.
	 */
	public int getMaximumHitPoints() {
		double mass = this.getMass();
		if(mass > Integer.MAX_VALUE)
			return Integer.MAX_VALUE;
		return (int) Math.round(mass);
	}

	private int currentHitPoints;
	
	
	/**
	 * Set the current action points.
	 * 
	 * @param actionPoints The new amount of action points.
	 * 
	 * @post	If actionPoints is greater than or equal to zero,
	 * 			The new action point amount will be set to the minimum value, being actionPoints or getMaximumActionPoints()
	 * 			| if(actionPoints >= 0)
	 * 			| new.getCurrentActionPoints() == Math.min(actionPoints, this.getMaximumActionPoints)
	 * 
	 * @post	If the actionPoints is less than zero, zero will be set.
	 * 			| if(actionPoints < 0)
	 * 			| new.getCurrentActionPoints() == 0
	 */
	@Raw @Model
	private void setCurrentActionPoints(int actionPoints) {
		this.currentActionPoints = (actionPoints < 0) ? 0 : Math.min(actionPoints, getMaximumActionPoints());
	}
	
	/**
	 * Returns the current amount of action points in valid form.
	 */
	@Basic @Raw
	public int getCurrentActionPoints() {
		//Call set to make sure we're in valid bounds.
		setCurrentActionPoints(currentActionPoints);
		return currentActionPoints;
	}
	
	/**
	 * Returns the maximum amount of action points.
	 */
	public int getMaximumActionPoints() {
		double mass = this.getMass();
		if(mass > Integer.MAX_VALUE)
			return Integer.MAX_VALUE;
		return (int) Math.round(mass);
	}
	
	private int currentActionPoints;
	
}
