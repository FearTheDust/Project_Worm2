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
	 * 
	 * @param position
	 * @param radius
	 * @return
	 */
	public boolean isImpassable(Position position, double radius) {
		if(isImpassable(position))
			return true;
		
		for()
			for()
	}
	
	//TODO: Change to isImpassable(position, 0)?
	private boolean isImpassable(Position position) {
		
	}
	
	
	

}
