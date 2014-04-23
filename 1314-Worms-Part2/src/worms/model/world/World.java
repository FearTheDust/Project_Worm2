package worms.model.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import worms.model.*;
import worms.model.world.entity.*;
import worms.util.*;
import be.kuleuven.cs.som.annotate.*;

/**
 * A two dimensional world with a certain height and width. The world may or may not contain any teams and GameObjects.
 * All GameObjects in this world will be found in this.getGameObjects(). Any GameObject not in that list will not count as in this world.
 * 
 * REMARKS/RULES:
 * - When a GameObject has to be added to the world, first set the world of the GameObject to this 
 * before trying to add it to the list of GameObjects inside this world.
 * - When an instance of a Projectile has to be added the Projectile must first be set as the Living Projectile in this world.
 * 
 * @author Derkinderen Vincent
 * @author Coosemans Brent
 * 
 * @invar The amount of teams in a world is less than or equal to 10.
 * 			| this.getTeamAmount() <= Constants.MAX_TEAM_AMOUNT
 * @invar	When there is an active worm it is either dead or its world is set to this world.
 * 			| this.getActiveWorm() == null || !this.getActiveWorm().isAlive() || this.getActiveWorm().getWorld() == this
 */
public class World {

	/**
	 * Initialize a world with a certain width & height, a certain map (boolean[][]) and a certain instance of Random.
	 * 
	 * @param width The width of this world.
	 * @param height The height of this world.
	 * @param passableMap The Terrain of this world.
	 * @param random The Random of this world to e.g create GameObjects on a random position.
	 * 
	 * @post	The width of this world is width.
	 * 			| new.getWidth() == width
	 * @post	The height of this world is height.
	 * 			| new.getHeight() == height
	 * @post	The Random of this world is random
	 * 			| new.getRandom() == random
	 * @post	The amount of GameObjects in this world is zero.
	 * 			| new.getGameObjects().size() == 0
	 * 
	 * @throws IllegalArgumentException
	 * 			When the dimension isn't valid for a world.
	 * 			| !isValidDimension(width, height)
	 * @throws IllegalArgumentException
	 * 			When random or passableMap is a null reference
	 * 			| random == null || passableMap == null
	 * @throws IllegalArgumentException
	 * 			When the 2 dimensional boolean array isn't 'rectangle shaped' aka when a row hasn't got the same length as another one.
	 * 			| !isRectangleDimension(passableMap)
	 */
	@Raw
	public World(double width, double height, boolean[][] passableMap,
			Random random) throws IllegalArgumentException {
		if (!isValidDimension(width, height))
			throw new IllegalArgumentException("The dimension provided isn't a valid dimension for a World");
		if(random == null)
			throw new IllegalArgumentException("The random parameter was a null reference, which isn't allowed.");
		if(passableMap == null)
			throw new IllegalArgumentException("The passableMap musn't be a null reference.");
		if(!isRectangleDimension(passableMap))
			throw new IllegalArgumentException("The passableMap must be a rectangle shaped dimension.");
		
		this.width = width;
		this.height = height;
		//Cloned because we don't want anyone to modify our world while we're playing.
		this.passableMap = getInvertedMap(Util.deepClone(passableMap));
		this.random = random;
		gameObjList = new ArrayList<GameObject>();
		teamList = new ArrayList<Team>();
	}
	
	/**
	 * Check whether the lengths of the 2-Dimensional array are rectangle shaped. aka The lengths of every row should be equal.
	 * 
	 * @param matrix The 2-dimensional array to check.
	 * 
	 * @return False if the matrix provided is a null reference.
	 * 			| if(matrix == null)
	 * 			|	result == false
	 * @return False if a length of a row isn't the same as another row.
	 * 			| int width = matrix[0].length
	 * 			| for row = 0 to (incl) matrix.length - 1 with step 1
	 * 			|	if(width != matrix[row].length) 
	 * 			|		result == false
	 */
	public static boolean isRectangleDimension(boolean[][] matrix) {
		if(matrix == null)
			return false;
		
		int width = matrix[0].length;
		for(int row = 0; row < matrix.length; row++) {
			if(width != matrix[row].length)
				return false;
		}
		return true;
	}

	/**
	 * Invert the passableMap. This was needed as the map provided has the (0,0) in the bottom left.
	 * 
	 * @param passableMap The 2-dimensional boolean array to invert.
	 * 
	 * @return	The same map but with every row switched to [passableMap.length-1-row]
	 * 			| boolean[][] resultArr = new boolean[passableMap.length][];
	 * 			| for each row (index rowIndex) in the passableMap
	 * 			|	resultArr[rowIndex] = passableMap[passableMap.length-rowIndex-1]
	 * 			| result = resultArr
	 */
	private static boolean[][] getInvertedMap(boolean[][] passableMap) {
		if(passableMap == null)
			return null;
		
		boolean[][] result = new boolean[passableMap.length][];
		for(int row = 0; row < result.length; row++)
			result[row] = passableMap[passableMap.length-row-1];
		return result;
	}

	/**
	 * Returns whether the width and height form a valid dimension to be a World.
	 * 
	 * @param width The width of the dimension to check.
	 * @param height The height of the dimension to check.
	 * 
	 * @return  False if width or height is less than zero.
	 * 			| if (width < 0 || height < 0)
	 * 			| 	result == false
	 * @return	False when width or height are greater than Double.MAX_VALUE.
	 * 			| if (width > Double.MAX_VALUE || height > Double.MAX_VALUE)
	 * 			| 	result == false
	 * @return False when width or height aren't a number.
	 * 			| if(Double.isNaN(width) || Double.isNaN(height))
	 * 			| 	result == false
	 */
	public static boolean isValidDimension(double width, double height) {
		if (width < 0 || height < 0)
			return false;
		if (width > Double.MAX_VALUE || height > Double.MAX_VALUE)
			return false;
		if(Double.isNaN(width) || Double.isNaN(height))
			return false;
		return true;
	}

	/**
	 * Returns the instance of Random which we use to do our random operations with.
	 */
	@Basic @Immutable
	public Random getRandom() {
		return this.random;
	}
	
	private Random random;

	/**
	 * Scale of the world (in worm-meter per map pixel)
	 * 
	 * @return The scale of the map.
	 * 			| result == this.getHeight() / passableMap.length
	 */
	@Immutable
	public double getScale() {
		return height / passableMap.length;
	}

	/**
	 * Returns the width of this world.
	 */
	@Basic @Immutable
	public double getWidth() {
		return this.width;
	}

	/**
	 * Returns the height of this world.
	 */
	@Basic @Immutable
	public double getHeight() {
		return this.height;
	}
	
	private final double width;
	private final double height;

	/**
	 * The terrain map, a clone of the provided one.
	 */
	@Model
	private final boolean[][] passableMap;

	/**
	 * Returns a copy of the list holding all teams currently in this world.
	 * When there are no teams an empty List will be returned.
	 */
	public List<Team> getTeams() {
		return new ArrayList<Team>(teamList);
	}

	/**
	 * Returns the size of the list holding all teams currently in this world.
	 */
	public int getTeamAmount() {
		return teamList.size();
	}

	/**
	 * Add a team to this world.
	 * 
	 * @param team The team to add.
	 * 
	 * @post The team will be added to the List of teams present in this world.
	 * 			| new.getTeams().contains(team)
	 * 
	 * @throws IllegalArgumentException
	 * 			When team is a null reference.
	 * 			| team == null
	 * @throws IllegalArgumentException
	 * 			When the amount of teams in this world is already at its limit.
	 * 			| getTeams().size() >= this.MAX_TEAM_AMOUNT
	 * @throws IllegalArgumentException
	 * 			When the team is already added in this world.
	 * 			| this.getTeams().contains(team)
	 * @throws IllegalStateException
	 * 			When the state of the world isn't INITIALISATION.
	 * 			| this.getState()!=WorldState.INITIALISATION
	 */
	public void add(Team team) throws IllegalArgumentException,
	IllegalStateException {
		if (team == null)
			throw new IllegalArgumentException("Can't add a team with a null reference to this world.");
		if (this.getState() != WorldState.INITIALISATION)
			throw new IllegalStateException("Team can only be added during the initialisation of this world.");
		if (teamList.size() >= Constants.MAX_TEAM_AMOUNT)
			throw new IllegalArgumentException("The list of teams for this world is full, can't add more teams.");
		if(teamList.contains(team))
			throw new IllegalArgumentException("The team was already present in this world.");
		teamList.add(team);
	}
	
	/**
	 * A list holding all teams currently in this world.
	 */
	private final List<Team> teamList;

	/**
	 * Add a GameObject to this world.
	 * 
	 * @param gameObject The GameObject to add.
	 * 
	 * @post The world will contain the gameObject.
	 * 			| new.getGameObjects().contains(gameObject)
	 * 
	 * @throws IllegalArgumentException
	 * 			When the gameObject is a null reference.
	 * 			| gameObject == null
	 * @throws IllegalArgumentException
	 * 			When the GameObject with its radius and position isn't within the world boundaries.
	 * 			| !liesWithinBoundaries(gameObject)
	 * @throws IllegalArgumentException
	 * 			When the gameObject isn't alive.
	 * 			| !gameObject.isAlive()
	 * @throws IllegalArgumentException
	 * 			When the GameObject is an instance of a Projectile and the gameObject isn't the LivingProjectile.
	 * 			| (gameObject instanceof Projectile && gameObject != this.getLivingProjectile())
	 * @throws IllegalArgumentException
	 * 			When this world already contains the gameObject.
	 * 			| (this.getGameObjects().contains(gameObject))
	 * @throws IllegalArgumentException
	 * 			When the world of the gameObject isn't this world.
	 * 			| (gameObject.getWorld() != this)
	 * 
	 * @throws IllegalStateException
	 * 			When the GameObject isn't a Projectile and the worldState is INITIALISATION.
	 * 			| !(gameObject instanceof Projectile) && this.getState() != WorldState.INITIALISATION || 
	 * @throws IllegalStateException
	 * 			When the GameObject is an instance of a Projectile and the worldState isn't  PLAYING.
	 * 			| (gameObject instanceof Projectile) && this.getState() != WorldState.PLAYING
	 */
	public void add(GameObject gameObject) throws IllegalArgumentException, IllegalStateException {
		if (gameObject == null)
			throw new IllegalArgumentException("The GameObject to add to this world was a null reference.");
		
		if (!(gameObject instanceof Projectile)
				&& this.getState() != WorldState.INITIALISATION)
			throw new IllegalStateException("Only projectiles can be added after the initialisation of this world");
		if ((gameObject instanceof Projectile)
				&& this.getState() != WorldState.PLAYING)
			throw new IllegalStateException("Projectiles can only be added during the PLAYING state of this world");
		
		if (!liesWithinBoundaries(gameObject))
			throw new IllegalArgumentException("This object doesn't lie within the boundaries of this world.");
		if (!gameObject.isAlive())
			throw new IllegalArgumentException("The object to add must be alive.");
		if (gameObject instanceof Projectile && gameObject != this.getLivingProjectile())
			throw new IllegalArgumentException("The projectile must be set as the living projectile of this world first.");
		if(gameObjList.contains(gameObject))
			throw new IllegalArgumentException("The object is already in the world.");
		if(gameObject.getWorld() != this)
			throw new IllegalArgumentException("The object to be added musn't be in another world.");
		
		gameObjList.add(gameObject);
	}

	/**
	 * Returns whether a given object lies within the boundaries of this world.
	 * 
	 * @param gameObject the object to check.
	 * 
	 * @return 	Returns whether the object with the position and radius lies within the world boundaries.
	 * 			| result == this.liesWithinBoundaries(gameObject.getPosition(), gameObject.getRadius());
	 */
	public boolean liesWithinBoundaries(GameObject gameObject) {
		return this.liesWithinBoundaries(gameObject.getPosition(), gameObject.getRadius());
	}
	
	/**
	 * Returns whether a circle with a position and radius lies within the boundaries of this world.
	 * 
	 * @param position The position to check around.
	 * @param radius The radius to check within.
	 * 
	 * @return 	If the x-coordinate - radius isn't greater than or equal to 0 or the x-coordinate + radius isn't less than or equal to this world's width.
	 * 			| if(!((position.getX() - radius >= 0) && position.getX() + radius <= this.getWidth()))
	 *			| 	result == false;
	 * @return	If the y-coordinate - radius isn't greater than or equal to 0 or the y-coordinate + radius isn't less than or equal to this world's height.
	 * 			| if(!((position.getY() - radius >= 0) && position.getY() + radius <= this.getHeight()))
	 *			|	result == false
	 * @return 	else
	 *			|	else result == true;
	 */
	public boolean liesWithinBoundaries(Position position, double radius) {
		if (!((position.getX() - radius >= 0) && position.getX() + radius <= this.getWidth()))
			return false;
		if (!((position.getY() - radius >= 0) && position.getY() + radius <= this.getHeight()))
			return false;
		return true;
	}

	/**
	 * Returns a clone of the list of GameObjects on this world.
	 */
	public List<GameObject> getGameObjects() {
		return new ArrayList<GameObject>(gameObjList);
	}

	/**
	 * The list containing our GameObjects
	 */
	private List<GameObject> gameObjList;

	/**
	 * Returns the current active worm on this world.
	 */
	@Basic
	public Worm getActiveWorm() {
		return activeWorm;
	}

	/**
	 * Set the active worm to worm.
	 * 
	 * @param worm The worm to set as active worm.
	 * 
	 * @post The new active worm will be equal to worm.
	 * 			| new.getActiveworm() == worm
	 * 
	 * @throws IllegalArgumentException
	 * 			When the worm is null or isn't alive.
	 * 			| worm == null || !worm.isAlive()
	 */
	@Model
	private void setActiveWorm(Worm worm) throws IllegalArgumentException {
		if (worm == null)
			throw new IllegalArgumentException("The worm isn't allowed to be a null reference.");
		if (!worm.isAlive() || worm.getWorld() != this)
			throw new IllegalArgumentException("The worm provided as active worm must be alive and in this world.");
		this.activeWorm = worm;
	}

	/**
	 * Initialize next turn if possible.
	 * If the worldState() isn't WorldState.PLAYING, do nothing.
	 * If the game ended. Change the worldState to WorldState.ENDED
	 * 
	 * @post if the current state of this world isn't "playing", do nothing.
	 * 			| if(this.getState() != WorldState.PLAYING)
	 *			| return
	 * @post If the gameEnded the new world's worldState will be ENDED.
	 * 			| if(gameEnded())
	 *			| 	new.getState() = WorldState.ENDED;
	 *
	 * @effect  Else set the next worm to active, clean DeadObjects and give the new worm its turn points.
	 * 			| else
	 *			| 	setActiveWorm(getNextWorm());
	 *			|	cleanDeadObjects()
	 *			| 	this.getActiveWorm().giveTurnPoints()
	 */
	public void nextTurn() {
		if (this.getState() != WorldState.PLAYING)
			return;
		
		if (gameEnded())
			this.state = WorldState.ENDED;
		else {
			this.setLivingProjectile(null);
			setActiveWorm(getNextWorm());
			cleanDeadObjects(); // Important we do this after.
			this.getActiveWorm().giveTurnPoints();
		} 
	}

	private Worm activeWorm;

	/**
	 * Start the game of this world.
	 * 
	 * @post The active worm of this world will be the next worm.
	 * 			| new.getActiveWorm() == this.getNextWorm()
	 * @post The new worldState will be either PLAYING or when the gameEnded will be ENDED.
	 * 			| (new.getState() == WorldState.PLAYING) || (new.getState() == WorldState.ENDED && this.gameEnded())
	 * 
	 * @effect Starts the next Turn.
	 * 			| this.nextTurn()
	 */
	public void startGame() {
		this.state = WorldState.PLAYING;
		this.nextTurn();
	}

	/**
	 * Returns whether or not the game has ended.
	 * 
	 * @return False if this world's state is INITIALISATION 
	 * 			| if(this.getState() == WorldState.INITIALISATION)
	 * 			| 	result == false
	 * @return True if this world's state is ENDED.
	 * 			| if(this.getState() == WordState.ENDED)
	 * 			| 	result == true
	 * @return If the worldstate is PLAYING, return whether there is only one worm or only one team left.
	 * 			| if(this.getState() == WorldState.PLAYING) then
	 * 			| 	if(getNextWorm() == null
	 * 			| 		result == true
	 * 			|
	 * 			| boolean firstWormFound = false
	 * 			| Team firstTeam == null
	 * 			| for each Worm worm in this.getWorms()
	 *			|	if (worm.isAlive() && firstWormFound)
	 *			|		if (firstTeam == null)
	 *			|			result == false
	 *			|	if (firstTeam != worm.getTeam() && firstWormFound)
	 *			|		result == false
	 *			|	if (worm.isAlive() && !firstWormFound) 
	 *			|		firstWormFound = true
 	 *			|		firstTeam = worm.getTeam()
	 *			|
	 *			| result == true
	 */
	public boolean gameEnded() {
		switch (this.getState()) {
		case INITIALISATION:
			return false;
		case ENDED:
			return true;
		case PLAYING:
			if (getNextWorm() == null)
				return true;
			boolean firstWormFound = false;
			Team firstTeam = null;
			
			for (Worm worm : this.getWorms()) {
				if (worm.isAlive() && firstWormFound)
					if (firstTeam == null)
						return false;
				if (firstTeam != worm.getTeam() && firstWormFound)
						return false;
				if (worm.isAlive() && !firstWormFound) {
					firstWormFound = true;
					firstTeam = worm.getTeam();
				}
			}
			return true;
		default:
			return false;
		}
	}

	/**
	 * Returns the next worm.
	 * If there is only one living worm left, returns null.
	 * 
	 * @return If the activeWorm is a null reference, find the first worm who is alive in this.getGameObject() and return it.
	 * 			If none is found return null.
	 * 			| if (this.getActiveWorm() == null) {
	 * 			| 	for each GameObject gameObject in this.getGameObjects()
	 *			|		if (gameObject instanceof Worm && ((Worm) gameObject).isAlive())
	 *			|			result == (Worm) gameObject
	 *			|	result == null
	 * @return If the activeWorm isn't a null reference, find the activeWorm and from there on find the next worm. (1)
	 * 			When the activeWorm is found and no other worm is next in the List, search the list from the start again for the first worm. (2)
	 * 			| (1)
	 * 			| boolean previousWormFound = false;
	 * 			| for each GameObject gameObject in this.getGameObjects()
	 *			|	if (previousWormFound && gameObject instanceof Worm && ((Worm) gameObject).isAlive())
	 *			|		result == (Worm) gameObject
	 *			|	if (!previousWormFound && gameObject == this.getActiveWorm())
	 *			|		previousWormFound = true
	 *			|
	 *			| (2)
	 *			| for each GameObject gameObject in this.getGameObjects())
	 *			|	if (gameObject instanceof Worm && ((Worm) gameObject).isAlive() && gameObject != this.getActiveWorm())
	 *			|		result == (Worm) gameObject
	 *			|	result == null;
	 */	
	public Worm getNextWorm() {
		//REMARK! Do not use getObjectOfType/getWorms/.. this would clean our previous worm if he was dead.
		if (this.getActiveWorm() == null) {
			for (GameObject gameObject : this.getGameObjects()) {
				if (gameObject instanceof Worm && ((Worm) gameObject).isAlive())
					return (Worm) gameObject;
			}
			return null;
		} else {
			boolean previousWormFound = false;
			// First Seek current active Worm, then find next
			for (GameObject gameObject : this.getGameObjects()) {
				if (previousWormFound && gameObject instanceof Worm
						&& ((Worm) gameObject).isAlive())
					return (Worm) gameObject;
				if (gameObject == this.getActiveWorm())
					previousWormFound = true;
			}

			// Found current active worm but reached end of list, back to the start.
			for (GameObject gameObject : this.getGameObjects()) {
				if (gameObject instanceof Worm && ((Worm) gameObject).isAlive()
						&& gameObject != this.getActiveWorm())
					return (Worm) gameObject;
			}
			return null;
		}
	}

	/**
	 * Returns the current state of this world.
	 */
	@Basic
	public WorldState getState() {
		return this.state;
	}

	private WorldState state = WorldState.INITIALISATION;

	/**
	 * Returns the Projectile currently alive in this world.
	 * If there is none, returns null.
	 */
	@Basic
	public Projectile getLivingProjectile() {
		return livingProjectile;
	}

	/**
	 * Set the Projectile that is alive in this world to livingProjectile.
	 * 
	 * @post The living Projectile for the new world is equal to livingProjectile.
	 * 			| new.getLivingProjectile() == livingProjectile
	 */
	public void setLivingProjectile(Projectile livingProjectile) {
		this.livingProjectile = livingProjectile;
	}

	private Projectile livingProjectile;

	/**
	 * Checks whether the given circular region of this world,
	 * defined by the given center coordinates and radius,
	 * is impassable. This means that if any position in that circular region is impassable, the region is impassable.
	 * 
	 * @param position The position of the center of the circle to check  
	 * @param radius The radius of the circle to check
	 * 
	 * @return True if an impassable tile was found within radius distance of the position, false otherwise.
	 * 			| double step = 0.1 * radius
	 *			| double startRow = (position.getY() - radius)
	 *			| double startColumn = (position.getX() - radius)
	 *			| double endRow = (position.getY() + radius)
	 *			| double endColumn = (position.getX() + radius)
	 *			| for double row = Math.max(startRow, 0) as long as Math.floor(row) <= Math.floor(endRow) && Math.floor(row/this.getScale()) < passableMap.length with step step.
	 *			|	for double column = Math.max(startColumn, 0) as long as Math.floor(column) <= Math.floor(endColumn) && Math.floor(column/this.getScale()) < passableMap[0].length with step step.
	 *			|		if (!passableMap[(int) Math.floor(row/this.getScale())][(int) Math.floor(column/this.getScale())])
	 *			|			if(Util.fuzzyLessThanOrEqualTo(Math.pow(row - position.getY(), 2)
	 *			|				+ Math.pow(column - position.getX(), 2), Math.pow(radius, 2), 1E-15) && 
	 *			|				!Util.fuzzyEquals(Math.pow(row - position.getY(), 2)
	 *			|						+ Math.pow(column - position.getX(), 2), Math.pow(radius, 2), 1E-16))
	 *			|				result == true
	 *			|		else
	 *			|			column = column + step*Math.floor((Math.ceil(column/this.getScale()) - column/this.getScale()) / step)
	 *			| result == false
	 */
	public boolean isImpassable(Position position, double radius) {
		double step = 0.1 * radius;
		//scale = meter per pixel => row&column = pixels
		double startRow = (position.getY() - radius);
		double startColumn = (position.getX() - radius);
		double endRow = (position.getY() + radius);
		double endColumn = (position.getX() + radius);

		for (double row = Math.max(startRow, 0); Math.floor(row) <= Math.floor(endRow) && Math.floor(row/this.getScale()) < passableMap.length; row += step) {
			for (double column = Math.max(startColumn, 0); Math.floor(column) <= Math.floor(endColumn) && Math.floor(column/this.getScale()) < passableMap[0].length; column += step) {
				if (!passableMap[(int) Math.floor(row/this.getScale())][(int) Math.floor(column/this.getScale())]) {
					if(Util.fuzzyLessThanOrEqualTo(Math.pow(row - position.getY(), 2)
							+ Math.pow(column - position.getX(), 2), Math.pow(radius, 2), 1E-15) 
								&& !Util.fuzzyEquals(Math.pow(row - position.getY(), 2)
									+ Math.pow(column - position.getX(), 2), Math.pow(radius, 2), 1E-16)) {
						return true;
					}
				} else {
					//Skip to the next 'tile'.
					column = column + step*Math.floor((Math.ceil(column/this.getScale()) - column/this.getScale()) / step);
				}
			}
		}
		return false;
	}

	/**
	 * Checks whether the given circular region of this world,
	 * defined by the given center coordinates and radius,
	 * is passable and adjacent to impassable terrain. 
	 * 
	 * @param position The position of the center of the circle to check  
	 * @param radius The radius of the circle to check
	 * 
	 * @return False if the provided position is impassable for the provided radius.
	 * 			| if(this.isImpassable(position, radius))
	 *			|	result == false
	 * @return True if an impassable tile is found within radius & radius*1.1 distance around the position, false otherwise.
	 *			| double step = 0.1 * radius
	 *			| double checkingWidth = 1.1*radius
	 *			| double startRow = (position.getY() - checkingWidth)
	 *			| double startColumn = (position.getX() - checkingWidth)
	 *			| double endRow = (position.getY() + checkingWidth)
	 *			| double endColumn = (position.getX() + checkingWidth)
	 *			|
	 *			| for double row = Math.max(startRow, 0) as long as Math.floor(row) <= Math.floor(endRow) && Math.floor(row/this.getScale()) < passableMap.length with step step.
	 *			|	for double column = Math.max(startColumn, 0) as long as Math.floor(column) <= Math.floor(endColumn) && Math.floor(column/this.getScale()) < passableMap[0].length with step step.
	 *			|		if (!passableMap[(int) Math.floor(row/this.getScale())][(int) Math.floor(column/this.getScale())]
	 *			|			if(Util.fuzzyGreaterThanOrEqualTo((Math.pow(row - position.getY(), 2)
	 *			|				+ Math.pow(column - position.getX(), 2)),Math.pow(radius, 2), 1E-15)
	 *			|				&& Util.fuzzyLessThanOrEqualTo(Math.pow(row - position.getY(), 2)
	 *			|					+ Math.pow(column - position.getX(), 2), Math.pow(1.1*radius, 2), 1E-15))
	 *			|				result == true
	 *			| 		else
	 *			|			column = column + step*Math.floor((Math.ceil(column/this.getScale()) - column/this.getScale()) / step)
	 *			| result == false
	 */
	public boolean isAdjacent(Position position, double radius) {
		if(this.isImpassable(position, radius))
			return false;
		
		double step = 0.1 * radius;
		double checkingWidth = 1.1*radius;
		double startRow = (position.getY() - checkingWidth);
		double startColumn = (position.getX() - checkingWidth);
		double endRow = (position.getY() + checkingWidth);
		double endColumn = (position.getX() + checkingWidth);

		for (double row = Math.max(startRow, 0); Math.floor(row) <= Math.floor(endRow) && Math.floor(row/this.getScale()) < passableMap.length; row += step) {
			for (double column = Math.max(startColumn, 0); Math.floor(column) <= Math.floor(endColumn) && Math.floor(column/this.getScale()) < passableMap[0].length; column += step) {
				if (!passableMap[(int) Math.floor(row/this.getScale())][(int) Math.floor(column/this.getScale())]) {
					if(Util.fuzzyGreaterThanOrEqualTo((Math.pow(row - position.getY(), 2)
							+ Math.pow(column - position.getX(), 2)),Math.pow(radius, 2), 1E-15)
								&& Util.fuzzyLessThanOrEqualTo(Math.pow(row - position.getY(), 2)
										+ Math.pow(column - position.getX(), 2), Math.pow(1.1*radius, 2), 1E-15)) {
						// Outside the inner circle and inside the outer circle
						return true;
					}
				} else {
					//skip to the next 'tile'.
					column = column + step*Math.floor((Math.ceil(column/this.getScale()) - column/this.getScale()) / step);
				}
			}
		}
		return false;
	}

	/**
	 * Returns a list of all worms which are hit in a certain radius on a certain position.
	 * 
	 * @param position The position to check.
	 * 
	 * @param radius The radius to check in.
	 * 
	 * @return The list that contains all worms to who the distance to, from the position, is less than the radius + their radius.
	 * 			| ArrayList<Worm> result = new ArrayList<Worm>();
	 * 			| for each Worm worm in this.getWorms()
	 * 			| 	double distance = worm.getPosition().distance(position)
	 * 			| 	if(distance < worm.getRadius() + radius)
	 * 			|		result.add(worm)
	 * 			| return = result
	 */
	public ArrayList<Worm> hitsWorm(Position position, double radius) {
		ArrayList<Worm> result = new ArrayList<Worm>();
		for (Worm worm : this.getWorms()) {
			double distance = worm.getPosition().distance(position);
			if (distance < worm.getRadius() + radius)
				result.add(worm);
		}
		return result;
	}
	
	/**
	 * Returns a list of all the food within a certain radius on a certain position.
	 * 
	 * @param position The position to check.
	 * @param radius The radius to check in.
	 * 
	 * @return The list that contains all Food to who the distance to, from the position, is less than the radius + their radius.
	 * 			| ArrayList<Food> result = new ArrayList<Food>()
	 * 			| for each Food food in this.getFood()
	 * 			|	double distance = food.getPosition().distance(position)
	 * 			|	if((distance < food.getRadius() + radius)
	 * 			|		result.add(food)
	 * 			| return = result
	 */
	public ArrayList<Food> eatableFood(Position position, double radius) {
		ArrayList<Food> result = new ArrayList<Food>();
		for(Food food: this.getFood()) {
			double distance = food.getPosition().distance(position);
			if(distance < food.getRadius() + radius) {
				result.add(food);
			}
		}
		return result;
	}
	
	/**
	 * Returns a random adjacent position on this world.
	 * If none is found it will return null.
	 * 
	 * @param radius The radius of the object.
	 * 
	 * @return  The position found. This is done by getting a random position and if it ain't passable it tries the position 
	 * 			which is half as far from the middle position as the current tried position.
	 * 			After 5 attempts to the middle it will return null if no passable position was found.
	 * 			| Position middlePos = new Position(this.getWidth() / 2, this.getHeight() / 2)
	 *			| Position pos = new Position(this.random.nextDouble() * this.getWidth(), this.random.nextDouble() * this.getHeight())
	 *			| for int attempt = 0 to attempt == 4 with step 1
	 *			|	if(!this.isImpassable(pos, radius) && this.liesWithinBoundaries(pos, radius))
	 *			|		result == pos
	 *			|	else
	 *			|		pos = new Position((middlePos.getX() - pos.getX()) / 2 + pos.getX(), 
	 *			|			(middlePos.getY() - pos.getY()) / 2 + pos.getY())
	 *			| result == null
	 */
	public Position getRandomPassablePos(double radius) {
		Position middlePos = new Position(this.getWidth() / 2,
				this.getHeight() / 2);
		Position pos = new Position(this.random.nextDouble() * this.getWidth(),
				this.random.nextDouble() * this.getHeight());

		for (int attempt = 0; attempt < 5; attempt++) {
			if(!this.isImpassable(pos, radius) && this.liesWithinBoundaries(pos, radius))
				return pos;
			else
				pos = new Position((middlePos.getX() - pos.getX()) / 2+ pos.getX(), 
						(middlePos.getY() - pos.getY()) / 2 + pos.getY());
		}
		return null;
	}

	/**
	 * Returns the name of a single worm if that worm is the winner, or the name
	 * of a team if that team is the winner or null if there is no winner.
	 * This assumes the game has ended and only 1 team or 1 worm is left standing.
	 * 
	 * @return The winner's name
	 * 			| ArrayList<Worm> list = new ArrayList<>(this.getWorms())
	 * 			| if(list.size() != 0)
	 * 			|	Worm worm = list.get(0)
	 * 			|	if(worm.getTeam() != null)
	 * 			|		return "Team " + worm.getTeam().getName()
	 * 			|	else
	 * 			|		return worm.getName()
	 *			| else
	 *			| 	result = null
	 */
	public String getWinner() {
		ArrayList<Worm> list = new ArrayList<>(this.getWorms());
		
		if(list.size() != 0) {
			Worm worm = list.get(0);
			if(worm.getTeam() != null)
				return "Team " + worm.getTeam().getName();
			else
				return worm.getName();
		} else {
			return null;
		}
	}

	/**
	 * Returns a collection<GameObject> of all objects in this world which are an instance of the given type gameObjType.
	 * 
	 * @param gameObjType The class type to check for instances of.
	 * 
	 * @return  | List<GameObject> result
	 * 			| for each GameObject obj in this.getGameObjects()
	 * 			| 	if(gameObjType.isInstance(obj))
	 * 			|		result.add(obj)
	 * 			| return == result
	 */
	public Collection<GameObject> getObjectsOfType(Class<?> gameObjType) {
		cleanDeadObjects();
		ArrayList<GameObject> resultList = new ArrayList<GameObject>();
		for (GameObject obj : this.getGameObjects()) {
			if (gameObjType.isInstance(obj))
				resultList.add(obj);
		}
		return resultList;
	}

	/**
	 * Returns all worms in this world.
	 * 
	 * @effect (Collection<Worm>) getObjectsOfType(Worm.Class) along with a cast to cast every instance of type GameObject to Worm.
	 * 			| (Collection<Worm>) getObjectsOfType(Worm.Class)
	 */
	public Collection<Worm> getWorms() {
		List<Worm> result = new ArrayList<Worm>();
		for (GameObject obj : getObjectsOfType(Worm.class))
			result.add((Worm) obj);
		return result;
	}

	/**
	 * Returns all Food instances in this world.
	 * 
	 * @effect getObjectsOfType(Food.Class) along with a cast to cast every instance of type GameObject to Food.
	 * 			| (Collection<Food>) getObjectsOfType(Food.Class)
	 */
	public Collection<Food> getFood() {
		List<Food> result = new ArrayList<Food>();
		for (GameObject obj : getObjectsOfType(Food.class))
			result.add((Food) obj);
		return result;
	}

	/**
	 * Delete objects that aren't alive anymore in this world or left our world Boundaries.
	 * If the object is a Projectile different from the current livingProjectile, it is removed as well.
	 * 
	 * @post	Every GameObject in this world is alive.
	 * 			| for each GameObject gameObj in new.getGameObjects()
	 * 			|	gameObj.isAlive() && new.liesWithinBoundaries(gameObj)
	 * 			|	if(gameObj instanceof Projectile)
	 * 			|		this.getLivingProjectile() == gameObj
	 */
	@Model
	private void cleanDeadObjects() {
		for (GameObject obj : this.getGameObjects()) {
			if (obj instanceof Projectile && obj != this.getLivingProjectile()) {
				this.gameObjList.remove(obj);
			} else if (!obj.isAlive() || !this.liesWithinBoundaries(obj)) {
				this.gameObjList.remove(obj);
				if(obj == this.getLivingProjectile())
					this.setLivingProjectile(null);
			}
		}
	}
	
	/**
	 * Remove a gameObject from the GameObjects in this world.
	 * 
	 * @param gameObject The GameObject to remove from this world.
	 * 
	 * @post The GameObject will not be found in the list of GameObjects in this world.
	 * 			| !new.getGameObjects().contains(gameObject)
	 * 
	 * @throws IllegalArgumentException
	 * 			When gameObject is a null reference.
	 * 			| gameObject == null
	 * @throws IllegalArgumentException
	 * 			When gameObject isn't in this world as specified by getGameObjects()
	 * 			| !this.getGameObjects().contains(gameObject)
	 */
	public void remove(GameObject gameObject) throws IllegalArgumentException {
		if(gameObject == null)
			throw new IllegalArgumentException("The gameObject to remove musn't be a null reference");
		if(!this.gameObjList.contains(gameObject))
			throw new IllegalArgumentException("The GameObject wasn't in this world's List.");
		
		this.gameObjList.remove(gameObject);
		gameObject.removeWorld();
	}

}
