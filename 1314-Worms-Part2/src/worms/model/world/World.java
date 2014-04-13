package worms.model.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import worms.model.Team;
import worms.model.world.entity.Food;
import worms.model.world.entity.GameObject;
import worms.model.world.entity.Projectile;
import worms.model.world.entity.Worm;
import worms.util.Position;
import worms.util.Util;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Model;

public class World {

	/**
	 * The maximum amount of teams allowed on a world.
	 */
	public static final int MAX_TEAM_AMOUNT = 10;

	//TODO: Documentation
	public World(double width, double height, boolean[][] passableMap,
			Random random) {
		//To make this totally secure we should check the passableMap to be a nice square.
		if (!isValidDimension(width, height))
			throw new IllegalArgumentException(
					"The dimension provided isn't a valid dimension for a World");
		if(random == null)
			throw new IllegalArgumentException("The random parameter was a null reference, which isn't allowed.");
		
		if(passableMap == null)
			throw new IllegalArgumentException("The passableMap musn't be a null reference.");

		this.width = width;
		this.height = height;

		this.passableMap = getInvertedMap(passableMap);
		this.random = random;

		gameObjList = new ArrayList<GameObject>();
		teamList = new ArrayList<Team>();
	}

	private Random random;

	/**
	 * Invert the passableMap.
	 * @param passableMap
	 * @return
	 */
	private boolean[][] getInvertedMap(boolean[][] passableMap) {
		boolean[][] result = new boolean[passableMap.length][];

		for(int row = 0; row < result.length; row++) {
			result[row] = passableMap[passableMap.length-row-1];
		}

		return result;
	}

	/**
	 * Returns whether the width and height form a valid dimension to be a World.
	 * @param width The width of the dimension to check.
	 * @param height The height of the dimension to check.
	 * @return  | if (width < 0 || height < 0)
	 * 			| then result == false
	 * 			| if (width > Double.MAX_VALUE || height > Double.MAX_VALUE)
	 * 			| then result == false
	 * 			| if(Double.isNaN(width) || Double.isNaN(height))
	 * 			| then result == false
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

	public Random getRandom() {
		return this.random;
	}

	/**
	 * Scale of the world (in worm-meter per map pixel)
	 * @return The scale of the map.
	 * 			| result == this.getHeight() / passableMap.length
	 */
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

	/**
	 * World Dimension
	 */
	private final double width;
	private final double height;

	/**
	 * The terrain map
	 */
	@Model
	private final boolean[][] passableMap;

	/**
	 * A list holding all teams currently in this world.
	 */
	private final List<Team> teamList;

	/**
	 * Returns a copy of the list holding all teams currently in this world.
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
	 * @param team The team to add
	 * 
	 * @post | new.getTeams().contains(team)
	 * @throws IllegalArgumentException
	 * 			| team == null ||
	 * 			| getTeams().size() >= MAX_TEAM_AMOUNT
	 * @throws IllegalStateException
	 * 			| this.getState()!=WorldState.INITIALISATION
	 */
	public void add(Team team) throws IllegalArgumentException,
	IllegalStateException {
		if (team == null)
			throw new IllegalArgumentException(
					"Can't add a team with a null reference to this world.");
		if (this.getState() != WorldState.INITIALISATION)
			throw new IllegalStateException(
					"Team can only be added during the initialisation of this world.");
		if (teamList.size() >= MAX_TEAM_AMOUNT)
			throw new IllegalArgumentException(
					"The list of teams for this world is full, can't add more teams.");

		teamList.add(team);
	}

	/**
	 * Add a GameObject to this world.
	 * @param gameObject The GameObject to add.
	 * @post | getGameObjects().contains(gameObject)
	 * @throws IllegalArgumentException
	 * 			| gameObject == null ||
	 * 			| !liesWithinBoundaries(gameObject) ||
	 * 			| !gameObject.isAlive() ||
	 * 			| (gameObject instanceof Projectile && gameObject != this.getLivingProjectile()) ||
	 * 			| (this.getGameObjects().contains(gameObject))
	 * 
	 * @throws IllegalStateException
	 * 			| !(gameObject instanceof Projectile) && this.getState()!=WorldState.INITIALISATION || 
	 * 			| (gameObject instanceof Projectile) && this.getState() != WorldState.PLAYING
	 */
	public void add(GameObject gameObject)
			throws IllegalArgumentException, IllegalStateException {
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
		
		gameObjList.add(gameObject);
	}

	/**
	 * Returns whether a given object lies within the boundaries of this world.
	 * 
	 * @param gameObject the object to check.
	 * 
	 * @return 	Returns whether the object with the position and radius lies within the world boundaries.
	 * 			| @effect this.liesWithinBoundaries(gameObject.getPosition(), gameObject.getRadius());
	 */
	public boolean liesWithinBoundaries(GameObject gameObject) {
		return this.liesWithinBoundaries(gameObject.getPosition(), gameObject.getRadius());
	}
	
	/**
	 * Returns whether a circle with a position and radius lies within the boundaries of this world.
	 * 
	 * @param gameObject the object to check.
	 * @return 	| if(!((position.getX() - radius >= 0) && position.getX() + radius <= this.getWidth()))
	 *			| 	result == false;
	 *			| if(!((position.getY() - radius >= 0) && position.getY() + radius <= this.getHeight()))
	 *			|	return false;
	 *			| else
	 *			|	result == true;
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
	public Worm getActiveWorm() {
		return activeWorm;
	}

	/**
	 * Set the active worm to worm.
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
			throw new IllegalArgumentException();
		if (!worm.isAlive())
			throw new IllegalArgumentException();
		this.activeWorm = worm;
	}

	/**
	 * Initialize next turn if possible.
	 * If the worldState() isn't WorldState.PLAYING, do nothing.
	 * If the game ended. Change the worldState to WorldState.ENDED
	 * 
	 * @post if the current state of this world isn't "playing", do nothing.
	 * 		| if(this.getState() != WorldState.PLAYING)
	 *		| return
	 *
	 * @effect if the game has ended, set the current state of this world to "ended".
	 * 		else set active worm to the next worm if there isn't any living projectile at this moment.
	 * 		| if(gameEnded())
	 *		| 	new.getState() = WorldState.ENDED;
	 *		| else
	 *		| 	setActiveWorm(getNextWorm());
	 *		|	cleanDeadObjects()
	 *		| 	this.getActiveWorm().giveTurnPoints()
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
	 * @post new.getActiveWorm() == this.getNextWorm()
	 * @post (new.getState() == WorldState.PLAYING) || (new.getState() == WorldState.ENDED && this.gameEnded())
	 */
	public void startGame() {
		this.state = WorldState.PLAYING;
		this.nextTurn();
	}

	/**
	 * Returns whether or not the game ended.
	 * @return  | if(this.getState() == WorldState.INITIALISATION) //TODO: change documentation
	 * 			| result == false
	 * 			| if(this.getState() == WordState.ENDED)
	 * 			| result == true
	 * 			| if(this.getState() == WorldState.PLAYING) then
	 * 				| if(getNextWorm() == null
	 * 				| result == true
	 * 				|
	 * 				| boolean firstWormFound = false
	 * 				| Team firstTeam == null
	 * 				| for each GameObject gameObject in this.getGameObjects() {
	 * 					| if(gameObject instanceof Worm && ((Worm) gameObject).isAlive() && !firstWormFound) then
	 * 						| firstWormFound = true
	 * 						| firstTeam = ((Worm) gameObject).getTeam()
	 * 					| if(gameObject instanceof Worm && ((Worm) gameObject).isAlive() && firstWormFound)
	 * 						| if(firstTeam == null)
	 * 						| result == false
	 * 						| if(firstTeam != ((Worm) gameObject).getTeam())
	 * 						| result == false
	 * 				| result == true
	 * 			| return false;
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
	public WorldState getState() {
		return this.state;
	}

	private WorldState state = WorldState.INITIALISATION;

	/**
	 * Returns the Projectile currently alive in this world.
	 * If there is non, returns null.
	 */
	@Basic
	public Projectile getLivingProjectile() {
		return livingProjectile;
	}

	/**
	 * Set the Projectile that is alive in this world to livingProjectile.
	 * @post | new.getLivingProjectile() == livingProjectile
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
	 * @return True if the given region is impassable, false otherwise.
	 * TODO: (vraag) formeel? gewoon die code kopiëren?
	 */
	public boolean isImpassable(Position position, double radius) {
		double step = 0.1 * radius;
		
		//scale = meter per pixel => row&column = pixels
		double startRow = (position.getY() - radius);
		double startColumn = (position.getX() - radius);

		double endRow = (position.getY() + radius);
		double endColumn = (position.getX() + radius);

		for (double row = Math.max(startRow, 0); Math.floor(row) <= Math.floor(endRow) && Math.floor(row/this.getScale()) < passableMap.length; row += step) { // TODO: Double.MAX_VALUE
			for (double column = Math.max(startColumn, 0); Math.floor(column) <= Math.floor(endColumn) && Math.floor(column/this.getScale()) < passableMap[0].length; column += step) {
				if (!passableMap[(int) Math.floor(row/this.getScale())][(int) Math.floor(column/this.getScale())]) {

					/*if (Math.pow(row*this.getScale() - position.getY(), 2)
							+ Math.pow(column*this.getScale() - position.getX(), 2) < Math
							.pow(radius, 2))
						// inside the inner circle
						return true;*/

					if(Util.fuzzyLessThanOrEqualTo(Math.pow(row - position.getY(), 2)
							+ Math.pow(column - position.getX(), 2), Math
							.pow(radius, 2), 1E-15) && !Util.fuzzyEquals(Math.pow(row - position.getY(), 2)
									+ Math.pow(column - position.getX(), 2), Math
									.pow(radius, 2), 1E-16)) {
						return true;
					}
				} else {
					//column = column + step*Math.floor((Math.floor((column+1)/this.getScale()) - column) / step);
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
	 * @return True if the given region is passable and adjacent to impassable terrain, false otherwise.
	 * TODO: (vraag) Formeel? gewoon die code kopiëren?
	 */
	public boolean isAdjacent(Position position, double radius) {
		//System.out.println(this.getScale()); //0.02604166666

		if(this.isImpassable(position, radius))
			return false;

		//scale = meter per pixel => row&column = pixels
		double step = 0.1 * radius;
		double checkingWidth = 1.1*radius;
		
		double startRow = (position.getY() - checkingWidth);
		double startColumn = (position.getX() - checkingWidth);

		double endRow = (position.getY() + checkingWidth);
		double endColumn = (position.getX() + checkingWidth);

		for (double row = Math.max(startRow, 0); Math.floor(row) <= Math.floor(endRow) && Math.floor(row/this.getScale()) < passableMap.length; row += step) {
			for (double column = Math.max(startColumn, 0); Math.floor(column) <= Math.floor(endColumn) && Math.floor(column/this.getScale()) < passableMap[0].length; column += step) { // TODO:
				if (!passableMap[(int) Math.floor(row/this.getScale())][(int) Math.floor(column/this.getScale())]) {
					if(Util.fuzzyGreaterThanOrEqualTo((Math.pow(row - position.getY(), 2)
							+ Math.pow(column - position.getX(), 2)),Math
							.pow(radius, 2), 1E-15)
							&& Util.fuzzyLessThanOrEqualTo(Math.pow(row - position.getY(), 2)
							+ Math.pow(column - position.getX(), 2), Math
							.pow(1.1*radius, 2), 1E-15)) {
						// Outside the inner circle and inside the outer circle
						return true;
					}
				} else {
					column = column + step*Math.floor((Math.ceil(column/this.getScale()) - column/this.getScale()) / step);
				}
			}
		}
		return false;
	}

	/**
	 * Returns whether the position ((int) Math.floor(position.getY() /this.getScale()), (int) Math.floor(position.getX() / this.getScale()) is a passable 'Tile'
	 * 
	 * @param position The position to check.
	 * 
	 * @return Whether the tile is passable.
	 * 			| if(Math.floor(position.getY() / this.getScale()) > passableMap.length || Math.floor(position.getX() / this.getScale()) > passableMap.length)
	 *			| result == false;
	 *
	 * 			| if(passableMap[(int) Math.floor(position.getY() / this.getScale())][(int) Math.floor(position.getX() / this.getScale())])
	 * 			| result == true;
	 * 			| else
	 * 			| result == false
	 */
	public boolean isPassableTile(Position position) {
		if(Math.floor(position.getY() / this.getScale()) > passableMap.length || Math.floor(position.getX() / this.getScale()) > passableMap.length)
			return false;

		if (passableMap[(int) Math.floor(position.getY() / this.getScale())][(int) Math
		                                                                     .floor(position.getX() / this.getScale())])
			return true;

		return false;
	}

	/**
	 * Returns a list of all worms which are hit in a certain radius on a certain position.
	 * @param position The position to check.
	 * @param radius The radius to check in.
	 * @return 
	 * 			| ArrayList<Worm> result = new ArrayList<Worm>();
	 * 			| for each worm in this.getWorms()
	 * 			| 	double distance = worm.getPosition().distance(position)
	 * 			| 	if(distance < worm.getRadius() + radius)
	 * 			|		result.add(worm)
	 * 			| return = result
	 */
	public ArrayList<Worm> hitsWorm(Position position, double radius) {
		ArrayList<Worm> result = new ArrayList<Worm>();
		for (Worm worm : this.getWorms()) {
			double distance = worm.getPosition().distance(position);
			if (distance < worm.getRadius() + radius) {
				result.add(worm);
			}
		}
		return result;
	}
	
	/**
	 * Returns a list of all the food within a certain radius on a certain position.
	 * @param position The position to check.
	 * @param radius The radius to check in.
	 * @return
	 * 			| ArrayList<Food> result = new ArrayList<Food>();
	 * 			| for each food in this.getFood()
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
	 * @return  | TODO: formal documentation
	 */
	public Position getRandomPassablePos(double radius) { //getRandomAdjacentPos(
		Position middlePos = new Position(this.getWidth() / 2,
				this.getHeight() / 2);
		Position pos = new Position(this.random.nextDouble() * this.getWidth(),
				this.random.nextDouble() * this.getHeight());

		for (int attempt = 0; attempt < 5; attempt++) {
			if(!this.isImpassable(pos, radius) && this.liesWithinBoundaries(pos, radius)) {
				return pos;
			} else {
				pos = new Position((middlePos.getX() - pos.getX()) / 2
						+ pos.getX(), (middlePos.getY() - pos.getY()) / 2
						+ pos.getY());
			}
		}
		return null;
	}

	/**
	 * Returns the name of a single worm if that worm is the winner, or the name
	 * of a team if that team is the winner or null if there is no winner.
	 * This assumes the game has ended and only 1 team or 1 worm is left standing.
	 * 
	 * @return The winner's name
	 * 			| ArrayList<Worm> list = new ArrayList<>(this.getWorms());
	 * 			| if(list.size() != 0)
	 * 			|	Worm worm = list.get(0)
	 * 			|	if(worm.getTeam() != null)
	 * 			|		return worm.getTeam().getName()
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
				return worm.getTeam().getName();
			else
				return worm.getName();
		} else {
			return null;
		}
	}

	/**
	 * Returns a collection<GameObject> of all objects in this world which are an instance of the given type gameObjType.
	 * 
	 * @param gameObjType The class type to check for instances.
	 * @return  | List<GameObject> result;
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
	 * @effect getObjectsOfType(Worm.Class) along with a cast to cast every instance of type GameObject to Worm.
	 */
	public Collection<Worm> getWorms() {
		List<Worm> result = new ArrayList<Worm>();
		for (GameObject obj : getObjectsOfType(Worm.class))
			result.add((Worm) obj);

		return result;
	}

	/**
	 * Returns all Food instances in this world.
	 * @effect getObjectsOfType(Food.Class) along with a cast to cast every instance of type GameObject to Food.
	 * TODO add formal cast in documentation
	 */
	public Collection<Food> getFood() {
		List<Food> result = new ArrayList<Food>();
		for (GameObject obj : getObjectsOfType(Food.class))
			result.add((Food) obj);

		return result;
	}

	/**
	 * Delete objects that aren't alive anymore in this world.
	 * TODO: Add formal documentation
	 */
	private void cleanDeadObjects() {
		for (GameObject obj : this.getGameObjects()) {
			if (obj instanceof Projectile && obj != this.getLivingProjectile()) {
				this.gameObjList.remove(obj);
			} else if (!obj.isAlive()) {
				this.gameObjList.remove(obj);
				if(obj == this.getLivingProjectile())
					this.setLivingProjectile(null);
			}
		}
	}

}
