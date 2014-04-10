package worms.model.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import be.kuleuven.cs.som.annotate.*;
import worms.model.Team;
import worms.model.world.entity.Food;
import worms.model.world.entity.GameObject;
import worms.model.world.entity.Projectile;
import worms.model.world.entity.GameObject;
import worms.model.world.entity.Worm;
import worms.util.Position;

public class World {

	/**
	 * The maximum amount of teams allowed on a world.
	 */
	public static final int MAX_TEAM_AMOUNT = 10;

	// TODO Documentation
	public World(double width, double height, boolean[][] passableMap,
			Random random) {
		if (!isValidDimension(width, height))
			throw new IllegalArgumentException(
					"The dimension provided isn't a valid dimension for a World");

		this.width = width;
		this.height = height;

		this.passableMap = passableMap;
		this.random = random;

		teamList = new ArrayList<Team>();
	}

	private Random random;

	/**
	 * Returns whether the width and height form a valid dimension to be a World.
	 * @param width The width of the dimension to check.
	 * @param height The height of the dimension to check.
	 * @return  | if (width < 0 || height < 0)
	 * 			| then result == false
	 * 			| if (width > Double.MAX_VALUE || height > Double.MAX_VALUE)
	 * 			| then result == false
	 */
	public static boolean isValidDimension(double width, double height) {
		if (width < 0 || height < 0)
			return false;

		if (width > Double.MAX_VALUE || height > Double.MAX_VALUE)
			return false;

		return true;
	}

	/**
	 * Scale of the world (in worm-meter per map pixel)
	 * @return The scale of the map.
	 * 			| result == this.getHeight / passableMap.length
	 */
	public double getScale() {
		return height / passableMap.length;
	}

	@Basic
	@Immutable
	public double getWidth() {
		return this.width;
	}

	@Basic
	@Immutable
	public double getHeigth() {
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
	 * 			| !liesWithinBoundaries(gameObject)
	 * 			| !gameObject.isAlive()
	 * 			| (gameObject instanceof Projectile && gameObject != this.getLivingProjectile())
	 * @throws IllegalStateException
	 * 			| !(gameObject instanceof Projectile) && this.getState()!=WorldState.INITIALISATION
	 * 			| (gameObject instanceof Projectile) && this.getState() != WorldState.PLAYING
	 */
	public void add(GameObject gameObject)
			throws IllegalArgumentException, IllegalStateException {
		if (gameObject == null)
			throw new IllegalArgumentException(
					"The GameObject to add to this world was a null reference.");
		if (!(gameObject instanceof Projectile)
				&& this.getState() != WorldState.INITIALISATION)
			throw new IllegalStateException(
					"Only projectiles can be added after the initialisation of this world");
		if ((gameObject instanceof Projectile)
				&& this.getState() != WorldState.PLAYING)
			throw new IllegalStateException(
					"Projectiles can only be added during the PLAYING state of this world");
		if (!liesWithinBoundaries(gameObject))
			throw new IllegalArgumentException(
					"This object doesn't lie within the boundaries of this world.");
		if (!gameObject.isAlive())
			throw new IllegalArgumentException(
					"The object to add must be alive.");

		if (gameObject instanceof Projectile
				&& gameObject != this.getLivingProjectile()) {
			throw new IllegalArgumentException(
					"The projectile must be set as the living projectile of this world first.");
		}

		gameObjList.add(gameObject);
	}

	/**
	 * Returns whether a given object lies within the boundaries of this world.
	 * 
	 * @param gameObject the object to check.
	 * @return 	| if(!((gameObject.getPosition().getX()-gameObject.getRadius()>=0) && gameObject.getPosition().getX()+gameObject.getRadius()<=this.getWidth()))
	 *			| 	result == false;
	 *			| if(!((gameObject.getPosition().getY()-gameObject.getRadius()>=0) && gameObject.getPosition().getY()+gameObject.getRadius()<=this.getHeigth()))
	 *			|	return false;
	 *			| else
	 *			|	result == true;
	 */
	public boolean liesWithinBoundaries(GameObject gameObject) {
		if (!((gameObject.getPosition().getX() - gameObject.getRadius() >= 0) && gameObject
				.getPosition().getX() + gameObject.getRadius() <= this
					.getWidth()))
			return false;
		if (!((gameObject.getPosition().getY() - gameObject.getRadius() >= 0) && gameObject
				.getPosition().getY() + gameObject.getRadius() <= this
					.getHeigth()))
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
	 * If there is a living projectile at the moment, do nothing.
	 * 
	 * @post if the current state of this world isn't "playing", do nothing.
	 * 		| if(this.getState() != WorldState.PLAYING)
	 *		| return
	 *
	 * @effect if the game has ended, set the current state of this world to "ended".
	 * 		else set active worm to the next worm if there isn't any living projectile at this moment.
	 * 		| if(gameEnded())
	 *		| 	new.getState() = WorldState.ENDED;
	 *		| else if(this.getLivingProjectile() == null)
	 *		| 	setActiveWorm(getNextWorm());
	 *		| 	this.getActiveWorm().giveTurnPoints()
	 */
	public void nextTurn() {
		if (this.getState() != WorldState.PLAYING)
			return;
		if (gameEnded())
			this.state = WorldState.ENDED;
		else if (this.getLivingProjectile() == null) {
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
	 * @return  | if(this.getState() == WorldState.INITIALISATION)
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

			for (GameObject gameObject : this.getGameObjects()) {
				if (gameObject instanceof Worm && ((Worm) gameObject).isAlive()
						&& !firstWormFound) {
					firstWormFound = true;
					firstTeam = ((Worm) gameObject).getTeam();
				}

				if (gameObject instanceof Worm && ((Worm) gameObject).isAlive()
						&& firstWormFound) {
					if (firstTeam == null)
						return false;

					if (firstTeam != ((Worm) gameObject).getTeam())
						return false;
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

			// Found current active worm but reached end of list, go back to the
			// start.
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
		for (double row = Math.max(
				Math.floor((position.getY() - radius) * this.getScale()), 0); row <= Math
				.min(Math.ceil((position.getY() + radius) * this.getScale()),
						height)
				&& row <= Integer.MAX_VALUE; row++) {

			// TODO: Double.MAX_VALUE
			// TODO: (vraag) -- [int][int] maar we praten over double met groter
			// bereik???
			for (double column = Math
					.max(Math.floor((position.getX() - radius)
							* this.getScale()), 0); column <= Math.min(
					Math.ceil((position.getX() + radius) * this.getScale()),
					width) && column <= Integer.MAX_VALUE; column++) { // Double.MAX_VALUE
				if (!passableMap[(int) row][(int) column]
						&& Math.pow(row - position.getY(), 2)
								+ Math.pow(column - position.getX(), 2) <= Math
									.pow(radius, 2))
					return true;
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
		radius *= 0.1;

		boolean adjacentFound = false;

		double startRow = Math.floor((position.getY() - radius - 1)
				* this.getScale());
		double startColumn = Math.floor((position.getX() - radius - 1)
				* this.getScale());

		double endRow = Math.ceil((position.getY() + radius + 1)
				* this.getScale());
		double endColumn = Math.ceil((position.getX() + radius + 1)
				* this.getScale());

		for (double row = startRow; row <= endRow && row <= Integer.MAX_VALUE
				&& row <= height; row++) { // TODO: Double.MAX_VALUE
			// TODO: (vraag) -- [int][int] maar we praten over double met groter
			// bereik???
			for (double column = startColumn; column <= endColumn
					&& column <= Integer.MAX_VALUE && column <= width; column++) { // TODO:
				// Double.MAX_VALUE
				if (!passableMap[(int) row][(int) column]) {

					if (Math.pow(row - position.getY(), 2)
							+ Math.pow(column - position.getX(), 2) >= Math
								.pow(radius, 2)
							&& Math.pow(row - position.getY(), 2)
									+ Math.pow(column - position.getX(), 2) <= Math
										.pow(radius + 1, 2)) {
						// Outside the inner circle and inside the outern circle
						adjacentFound = true;
					} else if (Math.pow(row - position.getY(), 2)
							+ Math.pow(column - position.getX(), 2) <= Math
								.pow(radius, 2)) {
						// Inside the inner circle
						return false;
					}
				}
			}
		}

		return adjacentFound;
	}

	/**
	 * Returns whether the position ((int) Math.floor(position.getY()), (int) Math.floor(position.getX()) is a passable 'Tile'
	 * 
	 * @param position The position to check.
	 * @return Whether the tile is passable.
	 * 			| if(passableMap[(int) Math.floor(position.getY())][(int) Math.floor(position.getX())])
	 * 			| result == true;
	 * 			| else
	 * 			| result == false
	 */
	public boolean isPassableTile(Position position) {
		if (passableMap[(int) Math.floor(position.getY() * this.getScale())][(int) Math
				.floor(position.getX() * this.getScale())])
			return true;

		return false;
	}

	/**
	 * Returns a random adjacent position on this world.
	 * If none is found it will return null.
	 * 
	 * @param radius The radius of the object.
	 * 
	 * @return  | Position middlePos = new Position(this.getWidth() / 2, this.getHeigth() / 2);
	 * 			| Position pos = new Position(this.random.nextDouble() * this.getWidth(), this.random.nextDouble() * this.getHeigth());
	 * 			| for attempt = 0 to 10 with step 1
	 * 			| 	if(this.isAdjacent(pos, radius)
	 * 			|		return pos
	 * 			|	else
	 * 			|		pos = new Position((middlePos.getX() - pos.getX())/2 + pos.getX(), (middlePos.getY() - pos.getY())/2 + pos.getY())
	 * 			| return null
	 */
	public Position getRandomAdjacentPos(double radius) {
		Position middlePos = new Position(this.getWidth() / 2,
				this.getHeigth() / 2);
		Position pos = new Position(this.random.nextDouble() * this.getWidth(),
				this.random.nextDouble() * this.getHeigth());

		for (int attempt = 0; attempt < 10; attempt++) {
			if (this.isAdjacent(pos, radius)) {
				return pos;
			} else {
				pos = new Position((middlePos.getX() - pos.getX()) / 2
						+ pos.getX(), (middlePos.getY() - pos.getY()) / 2
						+ pos.getY());
			}
		}
		// ------------------------------------------------
		return null;
	}

	/**
	 * Returns the name of a single worm if that worm is the winner, or the name
	 * of a team if that team is the winner or null if there is no winner or the game hasn't ended yet.
	 * @return The winner's name
	 * 			| if(this.getState() == WorldState.ENDED) then
	 * 			|  	for each GameObject gameObject in this.getGameObjects()
	 * 			|		if (gameObject instanceof Worm && ((Worm) gameObject).isAlive())
	 * 			|			if(((Worm) gameObject).getTeam()==null)
	 *			|				result = ((Worm) gameObject).getName()
	 *			|			else
	 *			|				result = ((Worm) gameObject).getTeam().getName()
	 *			| else
	 *			|	result = null
	 */
	public String getWinner() {
		if (this.getState() == WorldState.ENDED) {
			for (GameObject gameObject : this.getGameObjects()) {
				if (gameObject instanceof Worm && ((Worm) gameObject).isAlive())
					if (((Worm) gameObject).getTeam() == null)
						return ((Worm) gameObject).getName();
					else
						return ((Worm) gameObject).getTeam().getName();
			}
		}
		return null;
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
	 */
	public Collection<Food> getFood() {
		List<Food> result = new ArrayList<Food>();
		for (GameObject obj : getObjectsOfType(Food.class))
			result.add((Food) obj);

		return result;
	}

	/**
	 * Delete objects that aren't alive anymore in this world.
	 */
	private void cleanDeadObjects() {
		for (GameObject obj : this.getGameObjects()) {
			if (obj instanceof Projectile && obj != this.getLivingProjectile())
				this.gameObjList.remove(obj);
			else if (!obj.isAlive())
				this.gameObjList.remove(obj);
		}
	}

}
