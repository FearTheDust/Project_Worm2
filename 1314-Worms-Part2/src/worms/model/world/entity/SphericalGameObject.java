package worms.model.world.entity;

import be.kuleuven.cs.som.annotate.Raw;
import worms.util.Position;

/**
 * A GameObject with a spherical figure with a certain radius.
 * 
 * @author Derkinderen Vincent
 * @author Coosemans Brent
 *
 */
public abstract class SphericalGameObject extends GameObject {

	/**
	 * Initialize a SpericalGameObject with a certain Position.
	 * 
	 * @param position The position of the GameObject.
	 * @effect super(position);
	 */
	@Raw
	public SphericalGameObject(Position position) {
		super(position);
	}
	
	/**
	 * The radius of the SphericalGameObject
	 */
	public abstract double getRadius();

}
