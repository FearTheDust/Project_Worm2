package worms.model.world.entity;

import be.kuleuven.cs.som.annotate.Immutable;
import worms.util.Position;

/**
 * Represents Food with a certain mass and radius.
 * 
 * @author Coosemans Brent
 * @author Derkinderen Vincent
 *
 */
public class Food extends SphericalGameObject {

	/**
	 * Initialize Food with a certain position.
	 * 
	 * @param position The position of this food.
	 * @effect super(position)
	 */
	public Food(Position position) {
		super(position);
	}

	/**
	 * Returns the radius of the food, always 0.2 m.
	 */
	@Override @Immutable
	public double getRadius() {
		return 0.2;
	}

	/**
	 * Returns the mass of this food, always 0.
	 */
	@Override @Immutable
	public double getMass() {
		return 0;
	}

}
