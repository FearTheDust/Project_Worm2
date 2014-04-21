package worms.util;

import worms.util.Util;
import be.kuleuven.cs.som.annotate.*;

/**
 * A class representing a position in a 2 dimensional space, represented by 2 coordinates x and y. 
 * 
 * @author Derkinderen Vincent
 * @author Coosemans Brent 
 * 
 * @invar 	The x- or y-coordinates are never "Not a Number".
 * 			| !Double.isNaN(this.getX()) && !Double.isNaN(this.getY())
 */
public class Position {

	/**
	 * Initialize this new position with the given x and y coordinates.
	 * 
	 * @param x The x-coordinate of this new position.
	 * @param y The y-coordinate of this new position.
	 * 
	 * @post	The x-coordinate of this new position is equal to the given x.
	 * 			| new.getX() == x
	 * @post	The y-coordinate of this new position is equal to the given y.
	 * 			| new.getY() == y
	 * 
	 * @throws	IllegalArgumentException
	 * 			When x- or y-coordinate aren't valid coordinates.
	 * 			| Double.isNaN(x) || Double.isNaN(y)
	 */
	public Position(double x, double y) throws IllegalArgumentException {
		if(Double.isNaN(x) || Double.isNaN(y))
			throw new IllegalArgumentException("A coordinate was Not a Number.");
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Return the X-coordinate of this position.
	 */
	@Basic @Immutable
	public double getX() {
		return x;
	}
	
	private final double x;
	
	/**
	 * Return the Y-coordinate of this position.
	 */
	@Basic @Immutable
	public double getY() {
		return y;
	}

	private final double y;
	
	/**
	 * Returns the distance between this position and the given position.
	 * Returns 0 when the given position is null.
	 * 
	 * @param position The position to calculate the distance for.
	 * 
	 * @return The distance between the 2 positions.
	 * 			| result ==  Math.sqrt(Math.pow(this.getX() - position.getX(), 2) + Math.pow(this.getY() - position.getY(), 2));
	 */
	public double distance(Position position) {
		if(position == null)
			return 0;
		return Math.sqrt(Math.pow(this.getX() - position.getX(), 2) + Math.pow(this.getY() - position.getY(), 2));
	}
	
	/**
	 * If otherObject isn't an instanceof Position the 2 objects aren't equal.
	 * If it is then the objects are the same if the X and Y coordinates are the same. 
	 * (Within a certain offset provided by Util.fuzzyEquals(double, double)
	 */
	@Override
	public boolean equals(Object otherObject) {
		if(otherObject instanceof Position) 
			return (Util.fuzzyEquals(this.getX(), ((Position) otherObject).getX()) &&
					Util.fuzzyEquals(this.getY(),((Position) otherObject).getY()));
		return false;
	}
	
	/**
	 * Hashcode provided:
	 * Integer.parseInt(this.getX() + "" + this.getY()) % 97
	 */
	@Override
	public int hashCode() {
		return Integer.parseInt(this.getX() + "" + this.getY()) % 97;
	}
	
}
