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
	 * @throws NullPointerException
	 * 			When position is null.
	 * 			| position == null
	 */
	protected void setPosition(Position position) throws IllegalArgumentException {
		if(!isValidPosition(position))
			throw new IllegalArgumentException();
		
		this.position = position;
	}
	
	/**
	 * Returns whether the position is a valid position/
	 * 
	 * @return False when position == null
	 * 			| if position == null
	 * 			| then result == false
	 * 
	 * TODO the doc down there is wrong, remove.
	 * 
	 * 
	 *@return False if the x & y aren't in the boundaries of the world.
	 * 			| if(!(world.getHeight() >= position.getY() && position.getY() >= 0 && world.getWidth >= position.getX() && position.getX() >= 0))
	 * 			| then result == false
	 */
	public boolean isValidPosition(Position position) { //TODO (vraag) static? Maar static overerft toch niet?
		if(position == null)
			return false;
		/*if(!(this.getWorld().getHeight() >= position.getY() && position.getY() >= 0))
			return false;
		if(!(this.getWorld().getWidth() >= position.getX() && position.getX() >= 0))
			return false;*/
			
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
	
	protected World world;

	
	/**
	 * Return the mass of this GameObject.
	 */
	public abstract double getMass();
	
	/**
	 * Returns whether or not this GameObject is alive in the world it's in.
	 */
	public abstract boolean isAlive();
	
	/**
	 * The radius of the GameObject
	 */
	public abstract double getRadius();
	
	/**
	 * Returns whether this worm can fall.
	 * @return False if this gameObject has no world.
	 * 			| if(this.getWorld() == null
	 * 			| result == false
	 */
	public boolean canFall() {
		if (this.getWorld() == null)
			return false;

		/* 
		 * This code actually checked if there was an impassable block beneath the worm only instead of adjacent in any direction.
		 * 
		 * for (double x = Math.max(Math.floor(this.getPosition().getX() - this.getRadius()), 0); 
				x <= Math.ceil(this.getPosition().getX() + this.getRadius())
				&& x <= this.getWorld().getWidth() && x / this.getWorld().getScale() <= Integer.MAX_VALUE; 
					x++) {
			
			for(double testRadius = this.getRadius(); testRadius <= 1.1*this.getRadius(); testRadius += this.getWorld().getScale()) {
				if (!this.getWorld().isPassableTile(
						new Position(x, this.getPosition().getY() - testRadius)))
				return false;
			}
		}
		return true;*/
		
		return !this.getWorld().isAdjacent(this.getPosition(), this.getRadius()) && !this.getWorld().isImpassable(this.getPosition(), this.getRadius());
	}
	
	/**
	 * Let this gameObject fall down until it leaves the world boundaries or is !canFall() during the fall.
	 * Only if this gameObject has a world.
	 * 
	 * @post The new Y-coordinate of this gameObject will be equal to or less than the current Y.
	 * 			| new.getPosition().getY() <= this.getPosition().getY()
	 * @post The new gameObject can not fall or isn't alive because it left worl boundaries or it hasn't got a worl.
	 * 			| !new.canFall || !new.isAlive() || (new.getWorld() == null && this.getWorld() == null)
	 * 
	 */
	public void fall() {
		if(this.getWorld() != null) {
			while(canFall()) { //-2, to be sure it doesn't end before but doesn't go on forever either.
				if(this.getPosition().getY() - this.getRadius()*0.1 >= -2)
					this.setPosition(new Position(this.getPosition().getX(), this.getPosition().getY() - this.getRadius()*0.1)); // fall with a little bit 
				else
					break; //TODO can be shortened down by including a condition in the while(..) liesWithinBoundaries?? + isAlive to liesWithinBoundaries?
			}
		}
	}
	

}
