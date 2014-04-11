package worms.model.world.entity;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.som.annotate.*;
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
 * A class representing worms with a position, a direction it's facing, a radius, a mass, an amount of action points and a name.
 * 
 * @author Derkinderen Vincent - Bachelor Informatica - R0458834
 * @author Coosemans Brent - Bachelor Informatica - R0376498
 * 
 * @Repository https://github.com/FearTheDust/Project_Worm2.git
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
 *
 * @invar	This worm is a member of the team it is in.
 *			| this.getTeam().isMember(this)
 */
public class Worm extends GameObject {

	/**
	 * Initialize a new worm with a certain position, angle, radius, name, a certain amount of action points and a certain amount of hit points.
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
	 * 			super(world, position)
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
	 * 
	 * @effect This is added to the list of GameObjects in world.
	 * 			| world.Add(this)
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
	 * @effect The new position of this worm is calculated and set.
	 * 			| this.setPosition(this.jumpStep(this.jumpTime()))
	 */
	public void jump(double timeStep) {
			this.setPosition(this.jumpStep(this.jumpTime(timeStep)));
			this.setCurrentActionPoints(0);
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
	 * 			| force = 5 * this.getCurrentActionPoints() + this.getMass() * Constants.EARTH_ACCELERATION
	 * 			| startSpeed = (force / this.getMass()) * FORCE_TIME
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
	 * TODO: Documentation formally
	 */
	public double jumpTime(double timeStep) {
		double loopTime = timeStep;
		Position calculatedPosition = this.getPosition();
		
		while((!this.getWorld().isAdjacent(calculatedPosition, this.getRadius()) || this.getPosition().distance(calculatedPosition) <= this.getRadius()) && 
				!this.getWorld().isImpassable(calculatedPosition, this.getRadius())) {
			calculatedPosition = this.jumpStep(loopTime);
			loopTime += timeStep;
		}
		loopTime -= timeStep; //one step back
		
		return loopTime;
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
		assert isValidAngle(Math.abs(2 * angle))
				|| Util.fuzzyEquals(Math.abs(angle), Math.PI);
		assert this.getCurrentActionPoints() >= getTurnCost(angle);

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
	@Basic
	@Raw
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
		if (!Util.fuzzyGreaterThanOrEqualTo(radius, getMinimumRadius()))
			throw new IllegalArgumentException(
					"The radius has to be greater than or equal to the minimum radius "
							+ minRadius);
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
		return getDensity() * (4.0 / 3.0) * Math.PI
				* Math.pow(this.getRadius(), 3);
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
	@Basic
	@Raw
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
			if (!(ch == ' ' || ch == '\'' || ch == '\"' || Character
					.isLetterOrDigit(ch)))
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
	@Raw
	@Model
	private void setCurrentHitPoints(int hitPoints) {
		this.currentHitPoints = (hitPoints < 0) ? 0 : Math.min(hitPoints,
				getMaximumHitPoints());
	}

	/**
	 * Return the current amount of hit points in valid form.
	 */
	public int getCurrentHitPoints() {
		// Call set to make sure we're in valid bounds.
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
	 * @post	If actionPoints is greater than or equal to zero,
	 * 			The new action point amount will be set to the minimum value, being actionPoints or getMaximumActionPoints()
	 * 			| if(actionPoints >= 0)
	 * 			| new.getCurrentActionPoints() == Math.min(actionPoints, this.getMaximumActionPoints)
	 * 
	 * @post	If the actionPoints is less than zero, zero will be set.
	 * 			| if(actionPoints < 0)
	 * 			| new.getCurrentActionPoints() == 0
	 */
	@Raw
	@Model
	private void setCurrentActionPoints(int actionPoints) {
		this.currentActionPoints = (actionPoints < 0) ? 0 : Math.min(
				actionPoints, getMaximumActionPoints());
	}

	/**
	 * Returns the current amount of action points in valid form.
	 */
	@Basic
	@Raw
	public int getCurrentActionPoints() {
		// Call set to make sure we're in valid bounds.
		setCurrentActionPoints(currentActionPoints);
		return currentActionPoints;
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
			throw new IllegalArgumentException(
					"This worm also has to be a member of the team.");
		this.team = team;
	}

	private Team team;

	/**
	 * Returns this worm's team.
	 */
	public Team getTeam() {
		return team;
	}

	/**
	 * Returns whether this worm's hit points equals 0.
	 */
	public boolean isAlive() {
		if (this.getWorld() == null)
			return false;
		
		//TODO: Replace this beneath with a full check? (incl radius) -> if(this.getWorld().liesWithinBoundaries(this)));

		if (this.getPosition().getX() > this.getWorld().getWidth()
				|| this.getPosition().getX() < 0
				|| this.getPosition().getY() > this.getWorld().getHeight()
				|| this.getPosition().getY() < 0)
			return false;

		if (this.getCurrentHitPoints() == 0)
			return false;
		return true;
	}

	/**
	 * Returns the Weapon the worm is currently having equipped.
	 * If it hasn't got a Weapon equipped it will return null.
	 */
	@Raw
	@Basic
	public Weapon getCurrentWeapon() {
		if (currentWeaponIndex == -1)
			return null;

		return weaponList.get(currentWeaponIndex);
	}

	/**
	 * Set the current weapon to weapon.
	 * @param weapon The weapon to set to.
	 * @post The current Weapon of the worm will be equal to weapon.
	 * 			| new.getCurrentWeapon() == weapon
	 * @throws IllegalArgumentException
	 * 			When the weapon provided is equal to null.
	 * 			| (weapon == null)
	 */
	@Raw
	public void setCurrentWeapon(Weapon weapon) throws IllegalArgumentException {
		if (weapon == null)
			throw new IllegalArgumentException(
					"The weapon provided to set to isn't allowed to be a null reference.");
		if (!weaponList.contains(weapon))
			throw new IllegalArgumentException(
					"The weapon must be in our weaponList.");

		this.currentWeaponIndex = weaponList.indexOf(weapon);
	}

	private int currentWeaponIndex = -1;

	/**
	 * Returns the next weapon available for the worm.
	 * @throws IllegalStateException
	 * 			When the list of weapons available is empty.
	 * 			| this.getWeaponList().size() == 0
	 */
	@Raw
	public Weapon getNextWeapon() throws IllegalStateException {
		if (currentWeaponIndex == -1 && weaponList.size() == 0)
			throw new IllegalStateException(
					"Next Weapon isn't available since the list doesn't contain any weapons.");

		if (currentWeaponIndex == weaponList.size() - 1)
			return weaponList.get(0);
		else
			return weaponList.get(currentWeaponIndex + 1);
	}

	/**
	 * Add a weapon to the weapons available to this worm, if it isn't yet.
	 * Doesn't do anything if the worm already has this weapon.
	 * @param weapon The weapon to add.
	 * @post The weapon will be available for the worm to access.
	 * 			| this.getWeaponList().contains(weapon);
	 * 
	 * @throws IllegalArgumentException
	 * 			When weapon is equal to null.
	 * 			| weapon == null
	 */
	public void add(Weapon weapon) {
		if (weapon == null)
			throw new IllegalArgumentException(
					"Can't add a weapon that is a null reference.");

		if (!this.hasGot(weapon))
			weaponList.add(weapon);
	}

	/**
	 * Returns whether the worm has access to a weapon of the same class than weapon.getClass().
	 * @param weapon The weapon to check the class from.
	 * @return Whether the worm has access to a weapon of the same class than weapon.getClass()
	 * 			| for each Weapon aWeapon of this.getWeaponList()
	 * 				| if(aWeapon.getClass() == weapon.getClass())
	 * 				| result == true
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
	 * @post Set the maximum action points for this worm.
	 * 		| new.getCurrentActionPoints == this.getMaximumActionPoints()
	 * @post Set the hit points to the current amount + 10
	 * 		| new.getCurrentHitPoints == this.getCurrentHitPoints() + 10
	 * @throws IllegalStateException
	 * 			When this worm isn't alive.
	 * 			| !this.isAlive()
	 */
	public void giveTurnPoints() throws IllegalStateException {
		if (!this.isAlive())
			throw new IllegalStateException(
					"The worm must be alive in order to grant its turn points.");

		this.setCurrentActionPoints(this.getMaximumActionPoints());
		if (this.getCurrentHitPoints() + 10 < this.getCurrentHitPoints())
			this.setCurrentHitPoints(getMaximumHitPoints());
		else
			this.setCurrentHitPoints(this.getCurrentHitPoints() + 10);
	}

	/**
	 * Returns the position after we would move.
	 * TODO: Formal & informal documentation!
	 * @return
	 */
	public Position getMovePosition() {
		if (this.getWorld() == null)
			return null;

		double minimize = 8.0;
		double bestAngle;
		Position bestPos = this.getPosition();

		for (double currentAngle = this.getAngle() - 0.7875; currentAngle <= this
				.getAngle() + 0.7875; currentAngle += 0.0175) {

			double distance = 0.1;
			boolean found = false;

			while (distance <= this.getRadius() && !found) {
				double PosX = distance * Math.cos(currentAngle)
						+ this.getPosition().getX();
				double PosY = distance * Math.sin(currentAngle)
						+ this.getPosition().getY();
				Position Pos = new Position(PosX, PosY);

				if (!this.getWorld().isImpassable(Pos, this.getRadius())) { //isAdjacent fout, Brent had gelijk, ze bedoelen dat niet zo.
					distance += 0.1;
				} else {
					found = true;
				}
			}
			
			/*if(Util.fuzzyEquals(currentAngle, this.getAngle(), 1E-2)) {
				System.out.println("a was " + distance + " with angle " + currentAngle + " and currentAngle " + this.getAngle());
			}*/

			distance -= 0.1;
			Position newPos = new Position(distance * Math.cos(currentAngle)
					+ this.getPosition().getX(), distance * Math.sin(currentAngle)
					+ this.getPosition().getY());
			
			if (distance >= 0.1) {
				double compare = Math.abs(this.getAngle() - currentAngle) / distance;
				if (compare < minimize) {
					minimize = compare;
					bestPos = newPos;
					bestAngle = currentAngle;
				}
			}
		}

		return bestPos;
	}

	/**
	 * Move the worm to a position as specified in the assignment.
	 * If it isn't in a world, do nothing.
	 * @effect The current action points is the previous - the cost.
	 * 			| this.setCurrentActionPoints(this.getCurrentActionpoints() - this.getMoveCost(movePosition))
	 * 
	 * @effect The position is changed to the calculated position
	 * 			| this.setPosition(this.getMovePosition())
	 * 
	 * @throws IllegalArgumentException
	 * 			When the cost to jump is greater than the current amount of AP.
	 * 			| getMoveCost(this.getMovePosition()) > getCurrentActionPoints()
	 */
	public void move() throws IllegalArgumentException {
		if (this.getWorld() == null)
			return;

		Position movePosition = this.getMovePosition();
		if (getMoveCost(movePosition) > getCurrentActionPoints())
			throw new IllegalArgumentException(
					"You don't have enough Action Points");

		this.setCurrentActionPoints(this.getCurrentActionPoints()
				- this.getMoveCost(movePosition));
		this.setPosition(movePosition);
	}

	/**
	 * Returns whether this worm can fall.
	 * @return False if the worm has no world.
	 * 			| if(this.getWorld() == null
	 * 			| result == false
	 * @return False if there is no passable'Tile' below him.
	 * 			| for x from Math.max(Math.floor(this.getPosition().getX() - this.getRadius(),0) until 
	 * 			| (x <= Math.ceil(this.getPosition().getX() + this.getRadius()) && x <= this.getWorld().getWidth() && x <= Integer.MAX_VALUE)
	 * 			| isn't true with step 1
	 * 			|		if(!this.getWorld().isPassableTile(new Position(x,this.getPosition().getY() - this.getRadius())))
	 *			|			result == false;
	 *			| result == true
	 */
	public boolean canFall() {
		if (this.getWorld() == null)
			return false;

		/*for (double x = Math.max(Math.floor(this.getPosition().getX() - this.getRadius()), 0); 
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
		
		return !this.getWorld().isAdjacent(this.getPosition(), this.getRadius());
	}
	
	/**
	 * Let this worm fall down until it leaves the world boundaries or is adjacent to impassable terrain, at one moment when falling down,
	 * as defined by canFall.
	 * 
	 * @post The new Y-coordinate of this worm will be equal to or less than the current Y.
	 * 			| new.getPosition().getY() <= this.getPosition().getY()
	 * @post The new worm can not fall or isn't alive because he left game boundaries.
	 * 			| !new.canFall || !new.isAlive()
	 * 
	 */
	public void fall() {
		Position oldPosition = this.getPosition();
		
		while(canFall()) { //-1, to be sure it doesn't end before but doesn't go on forever either.
			if(this.getPosition().getY() - this.getWorld().getScale() >= -2)
				this.setPosition(new Position(this.getPosition().getX(), this.getPosition().getY() - this.getRadius()*0.1)); // fall with 1 pixel
			else
				break;
		}
		
		double fallenMeters = oldPosition.getY() - this.getPosition().getY();
		int cost = (int) Math.floor(3*fallenMeters);
		this.setCurrentHitPoints(this.getCurrentHitPoints() - cost);
	}

}
