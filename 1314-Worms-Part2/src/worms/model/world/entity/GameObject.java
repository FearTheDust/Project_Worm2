package worms.model.world.entity;

import worms.model.world.World;
import worms.util.Position;
import be.kuleuven.cs.som.annotate.*;

/**
 * Represents a GameObject, an entity with a certain Position.
 * 
 * @author Coosemans Brent
 * @author Derkinderen Vincent
 * 
 * @invar The position of this GameObject is always valid.
 * 			| isValidPosition(this.getPosition())
 */
public abstract class GameObject {

	/**
	 * Initialize a GameObject with a certain Position in a world.
	 * 
	 * @param world The world where this GameObject is in.
	 * @param position The position of the GameObject.
	 * 
	 * @throws IllegalArgumentException
	 * 			When position isn't valid or when the world is null.
	 * 			| !this.isValidPosition(position) || world == null
	 */
	@Raw
	public GameObject(World world, Position position) throws IllegalArgumentException {
		if(world == null)
			throw new IllegalArgumentException();
		
		this.world = world;
		this.setPosition(position);
	}
	
	/**
	 * Returns the position of this GameObject.
	 */
	@Basic @Raw
	public Position getPosition() {
		return position;
	}
	
	/**
	 * Set the new position of this GameObject.
	 * 
	 * @param position The new position of this GameObject.
	 * 
	 * @post This GameObject's position is equal to the given position.
	 * 		 | new.getPosition() == position
	 * 
	 * @throws NullPointerException
	 * 			When position is not a valid position.
	 * 			| !isValidPosition(position)
	 */
	protected void setPosition(Position position) throws IllegalArgumentException {
		if(!isValidPosition(position))
			throw new IllegalArgumentException();
		this.position = position;
	}
	
	/**
	 * Returns whether the position is a valid position
	 * 
	 * @return False when position == null
	 * 			| if position == null
	 * 			| then result == false
	 */
	public boolean isValidPosition(Position position) {
		if(position == null)
			return false;	
		return true;
	}
	
	private Position position;
	
	/**
	 * Returns the world the worm is in.
	 * If the worm is dead this will automatically return null.
	 */
	public World getWorld() {
		return world;
	}

	private World world;

	/**
	 * Returns the mass of this GameObject.
	 */
	public abstract double getMass();
	
	/**
	 * Returns whether or not this GameObject is alive in the world it is in.
	 */
	public abstract boolean isAlive();
	
	/**
	 * Returns the radius of this GameObject.
	 */
	public abstract double getRadius();
	
	/**
	 * Returns whether this GameObject can fall.
	 * 
	 * @return False if this gameObject has no world.
	 * 			| if(this.getWorld() == null)
	 * 			| 	result == false
	 */
	public boolean canFall() {
		if (this.getWorld() == null)
			return false;
		return !this.getWorld().isAdjacent(this.getPosition(), this.getRadius()) && !this.getWorld().isImpassable(this.getPosition(), this.getRadius());
	}
	
	/**
	 * Let this gameObject fall down until it leaves the world boundaries or can't fall anymore,
	 * only if this gameObject has a world.
	 * 
	 * @post The new Y-coordinate of this gameObject will be equal to or less than the current Y.
	 * 			| new.getPosition().getY() <= this.getPosition().getY()
	 * @post The new gameObject can not fall or isn't alive because it left world boundaries or it hasn't got a world.
	 * 			| !new.canFall || !new.isAlive() || (new.getWorld() == null && this.getWorld() == null)
	 * 
	 */
	public void fall() {
		if(this.getWorld() != null) {
			while(canFall()) { 
				if(this.getPosition().getY() - this.getRadius()*0.1 >= -2) //-2, to be sure it doesn't end before but doesn't go on forever either.
					this.setPosition(new Position(this.getPosition().getX(), this.getPosition().getY() - this.getRadius()*0.1)); // fall with a little bit 
				else
					break; //TODO can be shortened down by including a condition in the while(..) liesWithinBoundaries?? + isAlive to liesWithinBoundaries?
			}
		}
	}

}
