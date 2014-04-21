package worms.model;

/**
 * 
 * @author Coosemans Brent
 * @author Derkinderen Vincent
 *
 */
public class Constants {
	
	/**
	 * To prevent anyone to make an instance.
	 */
	private Constants() {
	}
	
	/**
	 * Acceleration on earth while falling.
	 */
	public static final double EARTH_ACCELERATION = 9.80665;
	
	/**
	 * The time a force is exerted on some GameObjects whilst jumping.
	 */
	public static final double FORCE_TIME = 0.5;
	
	/**
	 * The radius of food.
	 */
	public static final double FOOD_RADIUS = 0.2;
	
	/**
	 * The growth of the radius when this food is eaten.
	 * E.g newRadius = oldRadius*FOOD_RADIUS_GROWTH
	 */
	public static final double FOOD_RADIUS_GROWTH = 1.1;
	
}
