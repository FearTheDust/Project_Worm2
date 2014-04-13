package worms.model;

import java.util.ArrayList;
import java.util.List;

import worms.model.world.entity.Worm;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * A team representing worms working together.
 * 
 * @author Coosemans Brent
 * @author Derkinderen Vincent
 *
 */
public class Team {
	
	/**
	 * Initialize a new empty team with a certain name.
	 * 
	 * @param name The name for this team.
	 * @post | new.getName() = name
	 */
	public Team(String name) {
		if(!isValidName(name))
			throw new IllegalArgumentException("This name is not a valid name for a team.");
		this.name=name;
		teamList = new ArrayList<Worm>();
	}

	/**
	 * Returns whether the name is a valid name.
	 * 
	 * @param name The name to be checked.
	 * 
	 * @return  True if the name is longer than or equal to 2 characters, starts with an uppercase and when every character is one from the following:
	 * 			[A-Z] || [a-z]
	 * 			| result != ((name == null) &&
	 * 			| (name.length() < 2) &&
	 * 			| (!Character.isUpperCase(name.charAt(0)) &&
	 * 			| (for each index i in 0..name.toCharArray().length-1:
     *       	|   (!(Character.isLetter(name.toCharArray[i])))
	 */
	public static boolean isValidName(String name) {
		if(name == null)
			return false;
		if(name.length() < 2)
			return false;
		if(!Character.isUpperCase(name.charAt(0)))
				return false;
		for(Character ch : name.toCharArray()) {
			if(!(Character.isLetter(ch)))
				return false;
		}
		return true;
	}
	/**
	 * Returns this team's name.
	 */
	@Basic @Raw
	public String getName() {
		return name;
	}
	
	private final String name;
	
	/**
	 * Checks whether a worm can be added to a team.
	 * 
	 * @param worm The worm to be checked.
	 * 
	 * @return 	| if(worm == null)
	 *			|	result == false;
	 *			| if(!worm.isAlive())
	 *			|	result == false;
	 *			| if(worm.getTeam() != null)
	 *			|	result == false;
	 *			| else
	 *			|	result == true;
	 */
	public static boolean isValidWorm(Worm worm){
		if(worm == null)
			return false;
		if(!worm.isAlive()) {
			return false;
		}
		if(worm.getTeam() != null)
			return false;
		return true;
	}
	
	/**
	 * Add a worm to this team.
	 * 
	 * @param worm the worm to add.
	 * 
	 * @post | worm.getTeam() == new
	 * @throws IllegalArgumentException
	 * 			| !isValidWorm(worm)
	 */
	public void add(Worm worm) throws IllegalArgumentException {
		if(!isValidWorm(worm))
			throw new IllegalArgumentException("This worm is not a valid worm for a team.");
		teamList.add(worm);
		worm.setTeam(this);
	}
	
	/**
	 * Returns all the living worms in this team.
	 */
	public List<Worm> getLivingWorms(){
		ArrayList<Worm> result = new ArrayList<Worm>();
		for(Worm worm : teamList){
			if(worm.isAlive())
				result.add(worm);
		}
		return result;
	}
	
	/**
	 * Returns a copy of the list of all the worms in this team.
	 */
	@SuppressWarnings("unchecked")
	public List<Worm> getWorms(){
		return (List<Worm>) teamList.clone();
	}
	
	private ArrayList<Worm> teamList;
	
	/**
	 * Returns whether the worm is a member of this team.
	 * 
	 * @param worm The worm to check.
	 */
	public boolean isMember(Worm worm){
		return teamList.contains(worm);
	}
	
}
