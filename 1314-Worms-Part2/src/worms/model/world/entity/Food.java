package worms.model.world.entity;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Raw;
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
	 * @effect | super(position)
	 * @post | new.isAlive()
	 */
	@Raw
	public Food(Position position) {
		super(position);
		this.alive = true;
	}

	/**
	 * Returns the radius of the food, always 0.2 m.
	 */
	@Override @Immutable @Basic
	public double getRadius() {
		return 0.2;
	}

	/**
	 * Returns the mass of this food, always 0.
	 */
	@Override @Immutable @Basic
	public double getMass() {
		return 0;
	}

	@Override @Basic
	public boolean isAlive() {
		return alive;
	}
	
	/**
	 * Sets the isAlive state to false.
	 * @post | new.isAlive() == false
	 */
	public void setToEaten() {
		this.alive = false;
	}
	
	private boolean alive;

}
