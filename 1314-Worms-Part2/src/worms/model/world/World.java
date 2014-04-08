package worms.model.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import worms.model.Team;
import worms.model.world.entity.GameObject;
import worms.model.world.entity.SphericalGameObject;
import worms.model.world.entity.Worm;
import worms.util.Position;

public class World {
	
	/**
	 * The maximum amount of teams allowed on a world.
	 */
	public static final int MAX_TEAM_AMOUNT = 10;
	
	//TODO Documentation
	public World(double width, double height, boolean[][] passableMap, Random random) {
		if(!isValidDimension(width, height))
				throw new IllegalArgumentException("The dimension provided isn't a valid dimension for a World");
		
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
		if(width < 0 || height < 0)
			return false;
		
		if(width > Double.MAX_VALUE || height > Double.MAX_VALUE)
			return false;
		
		return true;
	}
	
	/**
	 * World Dimension
	 */
	private final double width;
	private final double height;
	
	/**
	 * The terrain map
	 */
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
	 */
	public void add(Team team) throws IllegalArgumentException {
		if(team == null)
			throw new IllegalArgumentException("Can't add a team with a null reference to this world.");
		
		if(teamList.size() >= MAX_TEAM_AMOUNT)
			throw new IllegalArgumentException("The list of teams for this world is full, can't add more teams.");
		
		teamList.add(team);
	}
	
	
	/**
	 * Add a SphericalGameObject to this world.
	 * @param gameObject The SphericalGameObject to add.
	 * @post | getGameObjects().contains(gameObject)
	 * @throws IllegalArgumentException
	 * 			| gameObject == null ||
	 * 			| //TODO: Voeg check Formeel toe hier
	 */
	public void add(SphericalGameObject gameObject) throws IllegalArgumentException {
		if(gameObject == null)
			throw new IllegalArgumentException("The SphericalGameObject to add to this world was a null reference.");
			
		//TODO: Add check = If such an entity is located in a world, then the circle must
		//TODO: lie fully within the bounds of that world and may overlap with other entities.
		
		gameObjList.add(gameObject);
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
	//TODO: Gaan we Worm in een aparte lijst houden om er makkelijk over te loopen?
	
	
	
	/**
	 * Checks whether the given circular region of this world,
	 * defined by the given center coordinates and radius,
	 * is impassable. 
	 * 
	 * @param position The position of the center of the circle to check  
	 * @param radius The radius of the circle to check
	 * 
	 * @return True if the given region is impassable, false otherwise.
	 * TODO: (vraag) formeel? gewoon die code kopiëren?
	 */
	public boolean isImpassable(Position position, double radius) {
		for(double row = Math.floor(position.getY()-radius); row<=Math.ceil(position.getY()+radius) && row <= Integer.MAX_VALUE && row <= height; row++){ //TODO: Double.MAX_VALUE
			//TODO: (vraag) -- [int][int] maar we praten over double met groter bereik???
			for(double column = Math.floor(position.getX()-radius); column<=Math.ceil(position.getX()+radius) && column <= Integer.MAX_VALUE && column <= width; column++){ //Double.MAX_VALUE
				if(passableMap[(int) row][(int) column] && Math.pow(row-position.getY(),2) + Math.pow(column-position.getX(),2) <= Math.pow(radius,2))
					return false;
			}
		}
		return true;
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
	public boolean isAdjacent(Position position, double radius){
		boolean adjacentFound = false;
		
		double startRow = Math.floor(position.getY() - radius - 1);
		double startColumn = Math.floor(position.getX() - radius - 1);
		
		double endRow = Math.ceil(position.getY() + radius + 1);
		double endColumn = Math.ceil(position.getX() + radius + 1);
		
		for(double row = startRow; row <= endRow && row <= Integer.MAX_VALUE && row <= height; row++){ //TODO: Double.MAX_VALUE
			//TODO: (vraag) -- [int][int] maar we praten over double met groter bereik???
			for(double column = startColumn; column <= endColumn && column <= Integer.MAX_VALUE && column <= width; column++){ //TODO: Double.MAX_VALUE
				if(!passableMap[(int) row][(int) column]) {
					
					if(Math.pow(row-position.getY(),2) + Math.pow(column-position.getX(),2) >= Math.pow(radius,2) &&
							Math.pow(row-position.getY(),2) + Math.pow(column-position.getX(),2) <= Math.pow(radius+1,2)) {
						//Outside the inner circle and inside the outern circle
						adjacentFound = true;
					} else if(Math.pow(row-position.getY(),2) + Math.pow(column-position.getX(),2) <= Math.pow(radius,2)) {
						//Inside the inner circle
						return false;
					}					
				}
			}
		}
		
		return adjacentFound;
	}
	
	/**
	 * Returns a random adjacent position on this world.
	 */
	public Position getRandomAdjacentPos() {
		//TODO: Not yet implemented. ------------------------------------------------
		return null;
	}
	
	
	

}
