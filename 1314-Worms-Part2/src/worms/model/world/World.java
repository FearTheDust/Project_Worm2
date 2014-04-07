package worms.model.world;

import worms.util.Position;

public class World {
	

	public World(double width, double height, boolean[][] passableMap) {
		if(!isValidDimension(width, height))
				throw new IllegalArgumentException("The dimension provided isn't a valid dimension for a World");
		
		this.width = width;
		this.height = height;
		
		this.passableMap = passableMap;
	}
	
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
		for(double row = Math.floor(position.getY()-radius); row<=Math.ceil(position.getY()+radius) && row<=Integer.MAX_VALUE; row++){ //TODO: Double.MAX_VALUE
			//TODO: (vraag) -- [int][int] maar we praten over double met groter bereik???
			for(double column = Math.floor(position.getX()-radius); column<=Math.ceil(position.getX()+radius) && column<=Double.MAX_VALUE; column++){
				if(passableMap[(int) row][(int) column] && Math.pow(row-position.getY(),2) + Math.pow(column-position.getX(),2)<=Math.pow(radius,2))
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
		
		for(double row = startRow; row <= endRow && row<=Integer.MAX_VALUE; row++){ //TODO: Double.MAX_VALUE
			//TODO: (vraag) -- [int][int] maar we praten over double met groter bereik???
			for(double column = startColumn; column <= endColumn && column<=Double.MAX_VALUE; column++){
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
	

}
