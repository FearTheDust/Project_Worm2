package worms.model.world.entity;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.som.annotate.*;
import worms.gui.GUIConstants;
import worms.model.*;
import worms.model.equipment.weapons.*;
import worms.model.world.World;
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
 * A class representing worms with a position, a direction it's facing, a radius, a mass,
 * 			 an amount of action points, an amount of hit points, a name, a team and a set of weapons.
 * 
 * @author Derkinderen Vincent - Bachelor Informatica - R0458834
 * @author Coosemans Brent - Bachelor Informatica - R0376498
 * 
 * @Repository https://github.com/FearTheDust/Project_Worm2.git
 * 
 * @invar	This worm's action points amount is at all times less than or equal to the maximum amount of action points allowed and greater than or equal to 0.
 * 			| 0 <= this.getCurrentActionPoints() <= this.getMaximumActionPoints()
 * 
 * @invar	This worm's hit points amount is at all times less than or equal to the maximum amount of hit points allowed and greater than or equal to 0.
 * 			| 0 <= this.getCurrentHitPoints() <= this.getMaximumHitPoints()
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
 *
 * @invar	This worm is a member of the team it is in.
 *			| this.getTeam().isMember(this)
 */

public class Worm extends GameObject {

	/**
	 * Initialize a new worm with a certain position, angle, radius, name,
	 * 	 a certain amount of action points, a certain amount of hit points and give him a set of weapons.
	 * 
	 * @param world The world of the new worm.
	 * @param position The position of the new worm.
	 * @param angle The angle of the new worm.
	 * @param radius The radius of the new worm.
	 * @param name The name of the new worm.
	 * @param actionPoints The amount of action points of the new worm.
	 * @param hitPoints The amount of hit points of the new worm.
	 * 
	 * @effect	This worm will be granted a provided position when valid.
	 * 			| super(world, position)
	 * 
	 * @post	The angle of the new worm is equal to angle.
	 * 			| new.getAngle() == angle
	 * @post	The radius of the new worm is equal to radius.
	 * 			| new.getRadius() == radius
	 * @post	The name of the new worm is equal to name.
	 * 			| new.getName() == name
	 * 
	 * @effect	The current amount of action points for the new worm is equal to actionPoints. 
	 * 			In the case that actionPoints is greater than the amount of action points allowed, the maximum amount will be set.
	 * 			| this.setCurrentActionPoints(actionPoints)
	 * @effect The current amount of hit points for the new worm is equal to hitPoints.
	 * 			In the case that hitPoints is greater than the amount of hit points allowed, the maximum amount will be set.
	 * 			| this.setCurrentHitPoints(hitPoints)
	 * @effect This worm is added to the list of GameObjects in world.
	 * 			| world.add(this)
	 */
	@Raw
	public Worm(World world, Position position, double angle, double radius,
			String name, int actionPoints, int hitPoints) {
		super(world, position);
		this.setAngle(angle);
		this.setRadius(radius);
		this.setName(name);
		this.setCurrentActionPoints(actionPoints);
		this.setCurrentHitPoints(hitPoints);

		world.add(this);

		// Add & set weapons.
		this.add(new Rifle(this));
		this.add(new Bazooka(this));
		//this.add(new BrentsWeaponOfDoom(this));
		this.setCurrentWeapon(this.getNextWeapon());
	}

	/**
	 * Initialize a new worm with a maximum amount of action points possible for this worm as well as the maximum amount of possible hit points for this worm.
	 * 
	 * @param world The world of the new worm.
	 * @param position The position of the new worm.
	 * @param angle The angle of the new worm.
	 * @param radius The radius of the new worm.
	 * @param name The name of the new worm.
	 * 
	 * @effect	A new worm will be initialized with a position, angle, radius, name, the maximum amount of action points possible for the new worm 
	 * 				and the maximum amount of hit points possible for the new worm.
	 * 			| this(world, position, angle, radius, name, Integer.MAX_VALUE, Integer.MAX_VALUE)
	 */
	@Raw
	public Worm(World world, Position position, double angle, double radius,
			String name) {
		this(world, position, angle, radius, name, Integer.MAX_VALUE,
				Integer.MAX_VALUE);
	}

	/**
	 * This worm jumps to a certain position calculated by a formula.
	 * 
	 * @post	The current amount of action points is 0.
	 * 			| new.getCurrentActionPoints() == 0
	 * 
	 * @effect The new position of this worm is calculated and set if the current amount of actionPoints is higher than 0.
	 * 			| if(this.getCurrentActionpoints() > 0)
	 * 			| this.setPosition(this.jumpStep(this.jumpTime()))
	 */
	public void jump(double timeStep) {
		if(this.getCurrentActionPoints() > 0) {
			this.setPosition(this.jumpStep(this.jumpTime(timeStep)));
			this.setCurrentActionPoints(0);
		}
	}

	/**
	 * Returns the position where this worm would be at a certain time whilst jumping.
	 * 
	 * @param time The time of when we return the position.
	 * 
	 * @return	When the time equals 0 the current position will be returned,
	 * 			else the position this worm has at a certain time in a jump will be returned.
	 * 			| if(time == 0) then
	 * 			| 	result == this.getPosition()
	 * 			| Else
	 * 			| 	force = 5 * this.getCurrentActionPoints() + this.getMass() * Constants.EARTH_ACCELERATION
	 * 			| 	startSpeed = (force / this.getMass()) * FORCE_TIME
	 * 			| 	startSpeedX = startSpeed * Math.cos(this.getAngle())
	 * 			| 	startSpeedY = startSpeed * Math.sin(this.getAngle())
	 * 			| 	x = this.getPosition().getX() + (startSpeedX * time)
	 * 			| 	y = this.getPosition().getY() + (startSpeedY * time - Constants.EARTH_ACCELERATION * Math.pow(time,2) / 2)
	 * 			| 	result == new Position(x,y)
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
		double force = 5 * this.getCurrentActionPoints() + this.getMass()
				* Constants.EARTH_ACCELERATION;
		double startSpeed = (force / this.getMass()) * this.getForceTime();

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
	 * Returns the time a force is exerted on a worm's body.
	 */
	@Basic @Immutable
	public double getForceTime() {
		return Constants.FORCE_TIME;
	}

	/**
	 * Returns the jump time if jumped with this worm's current angle.
	 * 
	 * @param timeStep the time between two positions on the traject of the jump.
	 * 
	 * @return the time needed to jump from the worm's current position, with the worm's current angle and in the world he is located in.
	 * 			| double loopTime = 0
	 *			| Position calculatedPosition = this.getPosition()
	 *			| while(this.getWorld().liesWithinBoundaries(calculatedPosition, this.getRadius()) && (!this.getWorld().isAdjacent(calculatedPosition, this.getRadius()) || this.getPosition().distance(calculatedPosition) <= this.getRadius()) && 
	 *			|	!this.getWorld().isImpassable(calculatedPosition, this.getRadius()))
	 *			|		loopTime += timeStep
	 *			|		calculatedPosition = this.jumpStep(loopTime)
	 *			| loopTime -= timeStep
	 *			| result == Math.max(loopTime,0)
	 */
	public double jumpTime(double timeStep) {
		double loopTime = 0;
		Position calculatedPosition = this.getPosition();
		
		while(this.getWorld().liesWithinBoundaries(calculatedPosition, this.getRadius()) && (!this.getWorld().isAdjacent(calculatedPosition, this.getRadius()) || this.getPosition().distance(calculatedPosition) <= this.getRadius()) && 
				!this.getWorld().isImpassable(calculatedPosition, this.getRadius())) {
			loopTime += timeStep;
			calculatedPosition = this.jumpStep(loopTime);
		}
		loopTime -= timeStep; //one step back
		
		return Math.max(loopTime,0);
	}

	/**
	 * Returns the cost to move for this worm if this would be a legal position to move to.
	 * 
	 * @param The position to go to.
	 * 
	 * @return 	The cost to move.
	 * 			| s = Math.atan((this.getPosition().getY() - finalPosition.getY()) / (this.getPosition().getX() - finalPosition.getX()));
	 * 			| result ==  (int) (Math.ceil(Math.abs(Math.cos(s)) + Math.abs(4 * Math.sin(s))));
	 */
	public int getMoveCost(Position finalPosition) {
		double s = Math.atan((this.getPosition().getY() - finalPosition.getY())
				/ (this.getPosition().getX() - finalPosition.getX()));
		return (int) (Math.ceil(Math.abs(Math.cos(s))
				+ Math.abs(4 * Math.sin(s))));
	}

	/**
	 * Returns the angle of this worm.
	 */
	@Basic
	public double getAngle() {
		return angle;
	}
	
	/**
	 * Returns whether or not this worm can turn with the provided angle.
	 * 
	 * @param angle The angle to check for.
	 * 
	 * @return	Whether the worm has more or an equal amount of AP than the cost to turn with that angle.
	 * 			| result == Worm.getTurnCost(angle) <= this.getCurrentActionPoints();
	 */
	public boolean canTurn(double angle) {
		return Worm.getTurnCost(angle) <= this.getCurrentActionPoints();
	}

	/**
	 * Turn this worm with a given angle.
	 * 
	 * @param angle The angle to turn with.
	 * 
	 * @pre		The absolute value of twice the angle must be valid or equal to Math.abs(Math.PI).
	 * 			| isValidAngle(Math.abs(2*angle)) || Util.fuzzyEquals(Math.abs(angle), Math.PI)
	 * @pre		The cost to turn should be less than or equal to the amount of action points we have.
	 * 			| canTurn(angle)
	 * 
	 * @effect	This worm's new action points is set to the old amount of action points minus the cost to turn.
	 * 			| this.setCurrentActionPoints(this.getCurrentActionPoints() - getTurnCost(angle))
	 * @effect	This worm's new direction is set to the old angle plus the given angle plus 2*Math.PI modulo 2*Math.PI.
	 * 			| this.setAngle(Util.modulo(this.getAngle() + angle + 2*Math.PI, 2*Math.PI));
	 */
	public void turn(double angle) {
		assert isValidAngle(Math.abs(2 * angle)) || Util.fuzzyEquals(Math.abs(angle), Math.PI);
		assert canTurn(angle);
		this.setAngle(Util.modulo(this.getAngle() + angle + 2 * Math.PI,
				2 * Math.PI));
		this.setCurrentActionPoints(this.getCurrentActionPoints()
				- getTurnCost(angle));
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
	public static boolean isValidAngle(double angle) {
		return Util.fuzzyGreaterThanOrEqualTo(angle, 0)
				&& (angle < 2 * Math.PI);
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
	 * 
	 * @param radius The new radius of this worm.
	 * 
	 * @post	The radius of this worm is equal to the given radius.
	 * 			| new.getRadius() == radius
	 * 
	 * @throws IllegalArgumentException
	 * 			When the given radius is less than the minimum radius.
	 * 			| radius < this.getMinimumRadius()
	 * @throws IllegalArgumentException
	 * 			When the radius isn't a number.
	 * 			| Double.isNaN(radius)
	 */
	@Raw
	public void setRadius(double radius) throws IllegalArgumentException {
		if (!Util.fuzzyGreaterThanOrEqualTo(radius, getMinimumRadius()))
			throw new IllegalArgumentException("The radius has to be greater than or equal to the minimum radius " + this.minRadius);
		if (Double.isNaN(radius))
			throw new IllegalArgumentException("The radius must be a number.");
		this.radius = radius;
	}

	/**
	 * Returns the minimum radius of this worm.
	 */
	@Basic
	@Immutable
	public double getMinimumRadius() {
		return minRadius;
	}

	private double radius;

	private final double minRadius = 0.25; // Initialize in constructor later
											// on.

	/**
	 * Returns the mass of this worm.
	 */
	@Basic
	public double getMass() {
		return getDensity() * (4.0 / 3.0) * Math.PI * Math.pow(this.getRadius(), 3);
	}

	/**
	 * Returns this worm's density.
	 */
	@Basic
	@Immutable
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
	 * 			[A-Z] || [a-z] || a space || ' || " || [0-9]
	 * 			| result != ((name == null) &&
	 * 			| (name.length() < 2) &&
	 * 			| (!Character.isUpperCase(name.charAt(0)) &&
	 * 			| (for each index i in 0..name.toCharArray().length-1:
	 *       	|   (!(Character.isLetterOrDigit(name.toCharArray[i]) || name.toCharArray[i] == ' ' || name.toCharArray[i] == '\'' || name.toCharArray[i] == '\"'))))
	 */
	public static boolean isValidName(String name) {
		if (name == null)
			return false;
		if (name.length() < 2)
			return false;
		if (!Character.isUpperCase(name.charAt(0)))
			return false;
		for (Character ch : name.toCharArray()) {
			if (!(ch == ' ' || ch == '\'' || ch == '\"' || Character.isLetterOrDigit(ch)))
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
	 * 			the new amount of hit points will be set to the minimum of hitPoints and getMaximumHitPoints()
	 * 			| if(hitPoints >= 0)
	 * 			| new.getCurrentHitPoints() == Math.min(hitPoints, this.getMaximumHitPoints)
	 * @post	If the hitPoints is less than zero, zero will be set for the new Worm's HP.
	 * 			| if(hitPoints < 0)
	 * 			| new.getCurrentHitPoints() == 0
	 * @effect If hitPoints is less than or equal to zero, the currentHP is different and the active worm in the world is this one, call this.getWorld().nextTurn().
	 * 			| if(hitPoints <= 0 && this.getCurrentHitPoints() > hitPoints && this.getWorld() != null && this.getWorld().getActiveWorm() == this)
	 * 			|		this.getWorld().nextTurn()
	 */
	@Raw @Model
	private void setCurrentHitPoints(int hitPoints) {
		int oldHP = this.currentHitPoints;
		this.currentHitPoints = (hitPoints <= 0) ? 0 : Math.min(hitPoints, getMaximumHitPoints());
		if (hitPoints <= 0 && oldHP > hitPoints && this.getWorld() != null && this.getWorld().getActiveWorm() == this) //so this doesn't get called by this.getCurrentHP()
				this.getWorld().nextTurn();
	}
	
	/**
	 * Inflict damage upon this worm.
	 * @post	The new hit points amount is less than or equal to the old amount.
	 * 			| new.getCurrentHitPoints() <= this.getCurrentHitPoints()
	 */
	public void inflictHitDamage(int damage) {
		if(damage < 0)
			damage = 0;
		this.setCurrentHitPoints(this.getCurrentHitPoints() - damage);
	}

	/**
	 * Return the current amount of hit points in valid form.
	 */
	public int getCurrentHitPoints() {
		// Call setCurrentHitPoints to make sure we're in valid bounds.
		//(if the radius changed, meaning the mass changed, meaning the maximum amount of hit points changed)
		setCurrentHitPoints(currentHitPoints);
		return currentHitPoints;
	}

	/**
	 * Returns this worm's maximum amount of hit points.
	 */
	public int getMaximumHitPoints() {
		double mass = this.getMass();
		if (mass > Integer.MAX_VALUE)
			return Integer.MAX_VALUE;
		return (int) Math.round(mass);
	}

	private int currentHitPoints;

	/**
	 * Set the current action points.
	 * 
	 * @param actionPoints The new amount of action points.
	 * 
	 * @post If actionPoints is less than or equal to zero the new AP will be 0.
	 * 			| if(actionPoints <= 0) 
	 * 			| 	new.getCurrentActionPoints() == 0
	 * @post If actionPoints is greater than zero, The new amount will be set to the minimum value of
	 * 			actionPoints and getMaximumActionPoints() 
	 *      	| if(actionPoints > 0)
	 *      	| 	new.getCurrentActionPoints() == Math.min(actionPoints,this.getMaximumActionPoints)
	 * 
	 * @effect If actionPoints is less than or equal to zero, the currentAP is different and the active worm is this one, call this.getWorld().nextTurn().
	 * 			| if(actionPoints <= 0 && this.getCurrentActionPoints() > actionPoints && this.getWorld() != null && this.getWorld().getActiveWorm() == this)
	 * 			|		this.getWorld().nextTurn()
	 */
	@Raw @Model
	private void setCurrentActionPoints(int actionPoints) {
		int oldAP = this.currentActionPoints;
		this.currentActionPoints = (actionPoints <= 0) ? 0 : Math.min(actionPoints, getMaximumActionPoints());
		
		if (actionPoints <= 0 && oldAP > actionPoints && this.getWorld() != null && this.getWorld().getActiveWorm() == this) //so this doesn't get called by this.getCurrentAP()
				this.getWorld().nextTurn();
	}

	/**
	 * Returns the current amount of action points in valid form.
	 */
	@Basic @Raw
	public int getCurrentActionPoints() {
		// Call setCurrentActionPoints to make sure we're in valid bounds.
		//(if the radius changed, meaning the mass changed, meaning the maximum amount of hit points changed)
		setCurrentActionPoints(currentActionPoints);
		return currentActionPoints;
	}
	
	/**
	 * Decrease the current AP of this worm.
	 * 
	 * @param cost The cost to decrease the AP with.
	 * 
	 * @post The amount of action points of the new worm is less than or equal to the old one.
	 * 			| new.getCurrentActionPoints() <= this.getCurrentActionPoints()
	 * @post The amount of action points of the new worm is the same as before when the cost <= 0
	 * 			| if(cost <= 0)
	 * 			|	new.getCurrentActionpoints() == this.getCurrentActionpoints()
	 */
	public void decreaseActionPointsBy(int cost) {
		if(cost < 0)
			cost = 0;
		this.setCurrentActionPoints(this.getCurrentActionPoints() - cost);
	}

	/**
	 * Returns the maximum amount of action points.
	 */
	public int getMaximumActionPoints() {
		double mass = this.getMass();
		if (mass > Integer.MAX_VALUE)
			return Integer.MAX_VALUE;
		return (int) Math.round(mass);
	}

	private int currentActionPoints;

	/**
	 * Set the team of this worm to team.
	 * 
	 * @param team The team to set for this worm.
	 * 
	 * @post The team of this worm will be equal to team.
	 * 		 | new.getTeam() == team
	 * 
	 * @throws IllegalArgumentException
	 * 			When this worm isn't a member of team.
	 * 			| !team.isMember(this)
	 */
	@Raw
	public void setTeam(Team team) throws IllegalArgumentException {
		if (!team.isMember(this))
			throw new IllegalArgumentException("This worm also has to be a member of the team.");
		this.team = team;
	}

	/**
	 * Returns this worm's team.
	 */
	public Team getTeam() {
		return team;
	}

	private Team team;
	
	/**
	 * Returns whether the worm is alive.
	 * 
	 * @return The worm isn't alive when the current hit points equal to 0.
	 * 			| if(this.getCurrentHitPoints() == 0)
	 * 			| 	result == false
	 * @return The worm isn't alive when its circle doesn't lie fully within the world boundaries
	 * 			| if(!this.getWorld().liesWithinBoundaries(this))
	 * 			|	result == false
	 * @return The worm isn't alive when he isn't in a world.
	 * 			| if(this.getWorld() == null)
	 * 			|	result == false
	 */
	public boolean isAlive() {
		if (this.getWorld() == null)
			return false;
		if(!this.getWorld().liesWithinBoundaries(this))
			return false;
		if (this.getCurrentHitPoints() == 0)
			return false;
		return true;
	}

	/**
	 * Returns the Weapon the worm is currently having equipped.
	 * If it hasn't got a Weapon equipped it will return null.
	 */
	@Raw @Basic
	public Weapon getCurrentWeapon() {
		if (currentWeaponIndex == -1)
			return null;

		return weaponList.get(currentWeaponIndex);
	}

	/**
	 * Set the current weapon to weapon.
	 * 
	 * @param weapon The weapon to set to.
	 * 
	 * @post The current Weapon of the worm will be equal to weapon.
	 * 			| new.getCurrentWeapon() == weapon
	 * 
	 * @throws IllegalArgumentException
	 * 			When the weapon provided is equal to null.
	 * 			| (weapon == null)
	 * 			When the worm doesn't have the weapon.
	 * 			| !this.getWeaponList().contains(weapon)
	 */
	@Raw
	public void setCurrentWeapon(Weapon weapon) throws IllegalArgumentException {
		if (weapon == null)
			throw new IllegalArgumentException("The weapon provided to set to isn't allowed to be a null reference.");
		if (!weaponList.contains(weapon))
			throw new IllegalArgumentException("The weapon must be in our weaponList.");
		this.currentWeaponIndex = weaponList.indexOf(weapon);
	}

	private int currentWeaponIndex = -1;

	/**
	 * Returns the next weapon available for the worm.
	 * 
	 * @return The weapon next to the current weapon.
	 * 			| if(this.getCurrentWeapon() == null)
	 * 			|	result == this.getWeaponList().get(0)
	 * 			| else
	 * 			|	index = this.getWeaponList().indexOf(this.getCurrentWeapon)
	 * 			|	if(index >= this.getWeaponList().size() - 1)
	 * 			|		result == this.getWeaponList().get(0)
	 * 			|	else
	 * 			|		result == this.getWeaponList().get(index+1)
	 * 
	 * @throws IllegalStateException
	 * 			When the list of weapons available is empty.
	 * 			| this.getWeaponList().size() == 0
	 */
	@Raw
	public Weapon getNextWeapon() throws IllegalStateException {
		if (currentWeaponIndex == -1 && weaponList.size() == 0)
			throw new IllegalStateException("Next Weapon isn't available since the list doesn't contain any weapons.");
		if (currentWeaponIndex == weaponList.size() - 1)
			return weaponList.get(0);
		else
			return weaponList.get(currentWeaponIndex + 1);
	}

	/**
	 * Add a weapon to the weapons available to this worm.
	 * Doesn't do anything if the worm already has this weapon.
	 * 
	 * @param weapon The weapon to add.
	 * 
	 * @post The weapon will be available for the worm to access.
	 * 			| this.getWeaponList().contains(weapon);
	 * 
	 * @throws IllegalArgumentException
	 * 			When weapon is equal to null.
	 * 			| weapon == null
	 */
	public void add(Weapon weapon) throws IllegalArgumentException {
		if (weapon == null)
			throw new IllegalArgumentException("Can't add a weapon that is a null reference.");

		if (!this.hasGot(weapon))
			weaponList.add(weapon);
	}

	/**
	 * Returns whether the worm has access to a weapon of the same class as the class of the weapon provided.
	 * @param weapon The weapon to check the class from.
	 * 
	 * @return Whether the worm has access to a weapon of the same class than weapon.getClass()
	 * 			| for each Weapon aWeapon of this.getWeaponList()
	 * 			| 	if(aWeapon.getClass() == weapon.getClass())
	 * 			| 		result == true
	 * 			| result == false
	 */
	public boolean hasGot(Weapon weapon) {
		for (Weapon aWeapon : weaponList) {
			if (aWeapon.getClass() == weapon.getClass())
				return true;
		}
		return false;
	}

	/**
	 * Returns a copy of the list of all weapon a worm has access to at this moment.
	 */
	public List<Weapon> getWeaponList() {
		return new ArrayList<Weapon>(weaponList);
	}

	private ArrayList<Weapon> weaponList = new ArrayList<Weapon>();

	/**
	 * Give this worm his turn points.
	 * 
	 * @post Set the action points to the maximum action points for this worm.
	 * 		| new.getCurrentActionPoints() == this.getMaximumActionPoints()
	 * @post Set the hit points to the current amount of hit points + 10 for this worm.
	 * 		| new.getCurrentHitPoints() == this.getCurrentHitPoints() + 10
	 * 
	 * @throws IllegalStateException
	 * 			When this worm isn't alive.
	 * 			| !this.isAlive()
	 */
	public void giveTurnPoints() throws IllegalStateException {
		if (!this.isAlive())
			throw new IllegalStateException("The worm must be alive in order to grant its turn points.");

		this.setCurrentActionPoints(this.getMaximumActionPoints());
		//This is to make sure Integer.MAX + 10 doesn't go to 0 HP.
		if (this.getCurrentHitPoints() + 10 < this.getCurrentHitPoints())
			this.setCurrentHitPoints(getMaximumHitPoints());
		else
			this.setCurrentHitPoints(this.getCurrentHitPoints() + 10);
	}

	/**
	 * Returns the position where the worm would be if he would move.
	 * 
	 * @return check for every angle between the worm's current angle +/- 0.7875 with a step of 0.0175
	 * 			what the maximum distance is and for all angles with equal maximum distances,
	 * 			search the angle with the minimum divergence between the angle and the worm's current angle.
	 * 			| if (this.getWorld() == null)
	 *			|	result == null
	 *			| double bestAngle = this.getAngle()
	 *			| double bestDistance = 0
	 *			| Position bestPos = this.getPosition()
	 *			| for double currentAngle = this.getAngle() - 0.7875 until currentAngle <= this.getAngle() + 0.7875 with step currentAngle += 0.0175
	 *			|	double distance = 0.1
	 *			|	boolean found = false
	 *			|	while (distance <= this.getRadius() && !found)
	 *			|		double posX = distance * Math.cos(currentAngle) + this.getPosition().getX()
	 *			|		double posY = distance * Math.sin(currentAngle) + this.getPosition().getY()
	 *			|		Position pos = new Position(posX, posY)
	 *			|		if (!this.getWorld().isImpassable(pos, this.getRadius()))
	 *			|			distance += 0.1*this.getRadius()
	 *			|		else
	 *			|			found = true
	 *			|	distance -= 0.1*this.getRadius()
	 *			| 	Position newPos = new Position(distance * Math.cos(currentAngle) + this.getPosition().getX(),
	 *			|							distance * Math.sin(currentAngle) + this.getPosition().getY())
	 *			|	if (distance >= 0.1)
	 *			|		if(distance > bestDistance)
	 *			|			bestDistance = distance
	 *			|			bestAngle = currentAngle
	 *			|			bestPos = newPos
	 *			|	else if(Util.fuzzyEquals(bestDistance, distance, 1E-4))
	 *			|		if(Math.abs(this.getAngle() - currentAngle) < Math.abs(this.getAngle() - bestAngle))
	 *			|			bestDistance = distance
	 *			|			bestAngle = currentAngle
	 *			|			bestPos = newPos
	 *			| result == bestPos
	 */
	public Position getMovePosition() {
		if (this.getWorld() == null)
			return null;
		double bestAngle = this.getAngle();
		double bestDistance = 0;
		Position bestPos = this.getPosition();
		for (double currentAngle = this.getAngle() - 0.7875; currentAngle <= this.getAngle() + 0.7875; currentAngle += 0.0175) {
			double distance = 0.1;
			boolean found = false;
			while (distance <= this.getRadius() && !found) {
				double posX = distance * Math.cos(currentAngle) + this.getPosition().getX();
				double posY = distance * Math.sin(currentAngle) + this.getPosition().getY();
				Position pos = new Position(posX, posY);
				if (!this.getWorld().isImpassable(pos, this.getRadius()))
					distance += 0.1*this.getRadius();
				else
					found = true;
			}
			distance -= 0.1*this.getRadius();
			Position newPos = new Position(distance * Math.cos(currentAngle) + this.getPosition().getX(),
											distance * Math.sin(currentAngle) + this.getPosition().getY());
			if (distance >= 0.1) {
				if(distance > bestDistance) {
					bestDistance = distance;
					bestAngle = currentAngle;
					bestPos = newPos;
				} else if(Util.fuzzyEquals(bestDistance, distance, 1E-4)) {
					if(Math.abs(this.getAngle() - currentAngle) < Math.abs(this.getAngle() - bestAngle)) {
						bestDistance = distance;
						bestAngle = currentAngle;
						bestPos = newPos;
					}
				}
			}
		}
		return bestPos;
	}

	/**
	 * Move the worm to a position by checking for every angle between the worm's current angle +/- 0.7875 with a step of 0.0175
	 * what the maximum distance is and for all angles with equal maximum distances,
	 * search the angle with the minimum divergence between the angle and the worm's current angle.
	 * If the worm isn't in a world, do nothing.
	 * 
	 * @effect The current action points is the previous - the cost.
	 * 			| this.setCurrentActionPoints(this.getCurrentActionpoints() - this.getMoveCost(movePosition))
	 * @effect The position is changed to the calculated position.
	 * 			| this.setPosition(this.getMovePosition())
	 * 
	 * @throws IllegalArgumentException
	 * 			When this worm can not move to the calculated position because he doesn't have enough action points.
	 * 			| !canMove(movePosition)
	 */
	public void move() throws IllegalArgumentException {
		if (this.getWorld() == null)
			return;
		Position movePosition = this.getMovePosition();
		if (!canMove(movePosition))
			throw new IllegalArgumentException("You don't have enough Action Points");
		this.setCurrentActionPoints(this.getCurrentActionPoints() - this.getMoveCost(movePosition));
		this.setPosition(movePosition);
	}
	
	/**
	 * Returns whether this worm can move to finalPosition.
	 * 
	 * @param finalPosition The position to check if we can reach.
	 * 
	 * @return Whether this worm is alive and whether he has enough AP to perform the move.
	 * 			| result == this.isAlive() && this.getMoveCost(this.getMovePosition()) <= this.getCurrentActionPoints();
	 */
	public boolean canMove(Position finalPosition) {
		return this.isAlive() && this.getMoveCost(this.getMovePosition()) <= this.getCurrentActionPoints();
	}

	/**
	 * Returns whether this worm can fall.
	 * 
	 * @return False if the worm has no world.
	 * 			| if(this.getWorld() == null
	 * 			| 	result == false
	 * @return False if the position of the worm with its radius is impassable.
	 * 			| if(this.getWorld().isImpassable(this.getPosition(), this.getRadius())
	 * 			|	result == false
	 * @return False if there is impassable terrain adjacent to this worm.
	 * 			| result == !this.getWorld().isAdjacent(this.getPosition(), this.getRadius())
	 */
	public boolean canFall() {
		if (this.getWorld() == null)
			return false;
		/* 
		 * This code actually checked if there was an impassable block beneath the worm only instead of adjacent in any direction.
		 * 
		 * for (double x = Math.max(Math.floor(this.getPosition().getX() - this.getRadius()), 0); 
				x <= Math.ceil(this.getPosition().getX() + this.getRadius())
				&& x <= this.getWorld().getWidth() && x / this.getWorld().getScale() <= Integer.MAX_VALUE; 
					x++) {
			
			for(double testRadius = this.getRadius(); testRadius <= 1.1*this.getRadius(); testRadius += this.getWorld().getScale()) {
				if (!this.getWorld().isPassableTile(
						new Position(x, this.getPosition().getY() - testRadius)))
				return false;
			}
		}
		return true;*/
		if(this.getWorld().isImpassable(this.getPosition(), this.getRadius()))
			return false;
				
		return !this.getWorld().isAdjacent(this.getPosition(), this.getRadius());
	}
	
	/**
	 * Let this worm fall down until it leaves the world boundaries or is adjacent to impassable terrain, while falling down
	 * and give the worm hit damage.
	 * 
	 * @effect Let the worm fall down.
	 * 			| super.fall()
	 * 
	 * @post	The amount of hit points after the fall is equal to or less than the previous amount.
	 * 			| new.getCurrentHitPoints() <= this.getCurrentHitPoints()
	 * 
	 */
	public void fall() {
		Position oldPosition = this.getPosition();
		super.fall();
		double fallenMeters = oldPosition.getY() - this.getPosition().getY();
		int cost = (int) (3*Math.floor(fallenMeters));
		this.setCurrentHitPoints(this.getCurrentHitPoints() - cost);
	}
	
	/**
	 * Let this worm fall down until it leaves the world boundaries or is adjacent to impassable terrain, while falling down.
	 * 
	 * @effect Let the worm fall down.
	 * 			| super.fall()
	 */
	public void softFall() {
		super.fall();
	}

	/**
	 * Set the position of this worm and eat food within its reach.
	 * When this worm is outside of the world boundaries and listed as active worm for that world, execute the nextTurn()
	 * of that world.
	 * 
	 * @post The new radius will be greater than or equal to the old radius.
	 * 		| new.getRadius() >= this.getRadius()
	 * @post All Food within the radius of this worm is eaten.
	 * 		| if(this.getWorld() != null)
	 * 		|	new.getWorld().eatableFood(new.getPosition(), this.getRadius()).size() == 0
	 * @post A worm outside of the world boundaries will not be the active worm in the new world.
	 * 		| if(this.getWorld() != null)
	 * 		|	new.getWorld().liesWithinBoundaries(new) || new.getWorld().getActiveWorm() != new
	 * 
	 * @effect set the position for the worm.
	 * 		| super.setPosition(position)
	 */
	public void setPosition(Position position) {
		super.setPosition(position);
		
		if(this.getWorld() != null) {
			for(Food food : this.getWorld().eatableFood(this.getPosition(), this.getRadius())) {
				this.setRadius(Constants.FOOD_RADIUS_GROWTH*this.getRadius());
				food.setToEaten();
			}
			
			if(!this.getWorld().liesWithinBoundaries(this) && this.getWorld().getActiveWorm() == this)
				this.getWorld().nextTurn();
		}
	}
	
	/**
	 * Shoot a projectile: create it and let it jump.
	 * 
	 * @param yield The propulsionYield with which we shoot.
	 * 
	 * @effect the living projectile of the world of this worm will be set to the created projectile.
	 * 			| this.getWorld().setLivingProjectile(projectile)
	 * @effect the created projectile will be added as a GameObject to the world of this worm.
	 * 			| this.getWorld.add(projectile)
	 * @effect the projectile will jump.
	 * 			| projectile.jump(GUIConstants.JUMP_TIME_STEP)
	 * 
	 * @throws IllegalStateException
	 * 			When the current weapon of this worm is a null reference.
	 * 			| this.getCurrentWeapon() == null
	 */
	public void shoot(int yield) throws IllegalStateException {
		if(this.getWorld() == null)
			return;
		
		if(this.getCurrentWeapon() == null)
			throw new IllegalStateException("The worm hasn't got a weapon equipped.");
		WeaponProjectile projectile = this.getCurrentWeapon().createProjectile(yield);
		if(projectile != null) {
			this.getWorld().setLivingProjectile(projectile);
			this.getWorld().add(projectile);
			//When we let the src-provided jump it doesn't seem to paint the end position although it does 
			//end up on the same end position it isn't painted like that.
			this.getWorld().getLivingProjectile().jump(GUIConstants.JUMP_TIME_STEP);
		}
	}
	
}
