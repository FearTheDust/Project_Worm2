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
	 * @param position The position of the GameObject.
	 * @throws IllegalArgumentException
	 * 			When position isn't valid.
	 * 			| !this.isValidPosition(position);
	 * 			When the world is null.
	 * 			| world == null
	 */
	@Raw
	public GameObject(World world, Position position) throws IllegalArgumentException {
		if(!isValidPosition(position))
			throw new IllegalArgumentException();
		
		if(world == null)
			throw new IllegalArgumentException();
		
		this.setPosition(position);
		this.world = world;
	}
	
	/**
	 * Initialize a GameObject with a certain Position in a world.
	 * 
	 * @param position The position of the GameObject.
	 * @throws IllegalArgumentException
	 * 			When position isn't valid.
	 * 			| !this.isValidPosition(position);
	 * 			When the world is null.
	 * 			| world == null
	 */
	@Raw
	public GameObject(World world) throws IllegalArgumentException{
		if(world == null)
			throw new IllegalArgumentException();
		
		this.setPosition(position);
		this.world = world;
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
	 * @return False if the x & y aren't in the boundaries of the world.
	 * 			| if(!(world.getHeight() <= position.getY() && position.getY() >= 0 && world.getWidth <= position.getX() && position.getX() >= 0))
	 * 			| then result == false
	 */
	public boolean isValidPosition(Position position) { //TODO (vraag) static? Maar static overerft toch niet?
		if(position == null)
			return false;
		if(!(world.getHeigth() <= position.getY() && position.getY() >= 0))
			return false;
		if(!(world.getWidth() <= position.getX() && position.getX() >= 0))
			return false;
			
		return true;
	}
	
	private Position position;
	
	
	/**
	 * Returns the world the worm is in.
	 * If the worm is dead this will automatically return null.
	 */
	public World getWorld() {
		if(this.isAlive()) {
			return world;
		}
		return null;
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

}
