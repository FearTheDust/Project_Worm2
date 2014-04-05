package worms.model.equipment.weapons;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;

/**
 * Abstract class representing a Weapon with a certain mass, damage and cost to shoot.
 * 
 * @author Derkinderen Vincent
 * @author Coosemans Brent
 * 
 * @invar	The mass of this Weapon is at all times valid.
 * 			| isValidMass(this.getMass())
 *
 */
public abstract class Weapon {
	
	//TODO Propelling Force, AMMO (an Object??, MAX, MIN(0), etc)
	
	
	//TODO: We waren aan 't werken aan baseForce & maxForce (+PropulsionYield in constructor!!)
	
	/**
	 * Initialize a weapon with a certain mass, damage and a certain cost to shoot it.
	 * @param mass 
	 * @param damage
	 * @param cost
	 */
	protected Weapon(double mass, int damage, int cost, double baseForce, double maxForce) throws IllegalArgumentException {
		if(!isValidMass(mass))
			throw new IllegalArgumentException("The provided mass for this Weapon isn't valid.");
		
		this.mass = mass;
		this.damage = damage;
		this.cost = cost;
		this.baseForce = baseForce;
		this.maxForce = maxForce;
	}
	
	
	/**
	 * @return The damage this weapon can inflict by default.
	 */
	@Basic @Immutable
	public int getDamage() {
		return damage;
	}
	
	/**
	 * The amount of hit points this weapon can deduce.
	 */
	private final int damage;

	
	/**
	 * Check if the provided mass is a valid mass for this Weapon.
	 * @return	False if the mass is less than zero.
	 * 			| if(mass < 0)
	 * 			| then result == false
	 */
	public static boolean isValidMass(double mass) {
		if(mass < 0)
			return false;
		
		return true;
	}
	
	/**
	 * @return The mass of this weapon.
	 */
	@Basic @Immutable
	public double getMass() {
		return mass;
	}
	
	/**
	 * The mass of this weapon.
	 */
	private final double mass;
	
	
	
	/**
	 * @return The amount of action points it costs to shoot with this weapon.
	 */
	@Basic @Immutable
	public int getCost() {
		return cost;
	}
	
	/**
	 * The amount of action points it costs to shoot with this weapon.
	 */
	private final int cost;
	
	/**
	 * The force which the weapon exerts on the projectile.
	 * @param propulsionYield
	 * @return | baseForce + (maxForce-baseForce) * propulsionYield / 100;
	 * 
	 * @throws IllegalArgumentException
	 * 			| !isValidPropulsionYield(propulsionYield)
	 */
	public double getForce(double propulsionYield) throws IllegalArgumentException {
		if(!isValidPropulsionYield(propulsionYield)) {
			throw new IllegalArgumentException();
		}
		
		return baseForce + (maxForce-baseForce) * propulsionYield / 100;
	}
	
	/**
	 * The base force of this weapon.
	 */
	private final double baseForce;
	
	/**
	 * The maximum force of this weapon.
	 */
	private final double maxForce;
	
	/**
	 * Checks whether the provided propulsionYield is valid.
	 * @param propulsionYield The propulsionYield to check.
	 * @return Whether the propulsionYield is between (inclusive) 0 and 100.
	 * 			| result == (propulsionYield >= 0 && propulsionYield <= 100)
	 */
	public static boolean isValidPropulsionYield(double propulsionYield) {
		return (propulsionYield >= 0 && propulsionYield <= 100);
	}
	

}
