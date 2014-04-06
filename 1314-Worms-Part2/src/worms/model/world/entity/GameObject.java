package worms.model.world.entity;

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
	 * Initialize a GameObject with a certain Position.
	 * 
	 * @param position The position of the GameObject.
	 * @throws IllegalArgumentException
	 * 			When position isn't valid.
	 * 			| !this.isValidPosition(position);
	 */
	@Raw
	public GameObject(Position position) throws IllegalArgumentException {
		if(!isValidPosition(position))
			throw new IllegalArgumentException();
		
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
	 * @return False when position == null
	 * 			| if position == null
	 * 			| then result == false
	 */
	public boolean isValidPosition(Position position) { //TODO (vraag) static? Maar static overerft toch niet?
		if(position == null)
			return false;
		
		return true;
	}
	
	private Position position;

}
