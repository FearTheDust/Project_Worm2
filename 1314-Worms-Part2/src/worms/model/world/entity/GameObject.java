package worms.model.world.entity;

import worms.model.world.World;
import worms.util.Position;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

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
	 * 			When position isn't valid or When the world is null.
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
	 * Set the new position of this worm.
	 * 
	 * @param position The new position of this worm.
	 * 
	 * @post This GameObject's position is equal to the given position.
	 * 		 | new.getPosition() == position
	 * 
	 * @throws IllegalArgumentException
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
	 */
	public World getWorld() {
		return world;
	}
	
	private World world;

	
	/**
	 * Return the mass of this GameObject.
	 */
	public abstract double getMass();
	
	/**
	 * Returns whether or not this GameObject is alive in the world it's in.
	 */
	public abstract boolean isAlive();
	
	/**
	 * Returns the radius of the GameObject
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
			while(canFall() && this.getWorld().liesWithinBoundaries(this)) { 
					this.setPosition(new Position(this.getPosition().getX(), this.getPosition().getY() - this.getRadius()*0.1)); // fall with a little bit 
			}
		}
	}
	
	/**
	 * Remove the set world if needed.
	 * If a world has to be set to null, remove this Object in that world by calling world.remove(GameObject), that function will call
	 * this in order to clean both ends.
	 * 
	 * @post If the world set doesn't contain this GameObject, set the world to null.
	 * 			| if(this.getWorld() != null && !this.getWorld().getGameObjects().contains(this))
	 * 			|	new.getWorld() == null
	 */
	public void removeWorld() {
		if(this.getWorld() != null && !this.getWorld().getGameObjects().contains(this)) {
				this.world = null;
		}
	}
	

}
