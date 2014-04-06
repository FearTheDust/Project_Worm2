package worms.model.equipment.weapons;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;

/**
 * Abstract class representing a Weapon with a certain projectile mass, damage, cost to shoot and base & maximum force.
 * 
 * @author Derkinderen Vincent
 * @author Coosemans Brent
 * 
 * @invar	The mass of this Weapon projectile is at all times valid.
 * 			| isValidProjectileMass(this.getProjectileMass())
 *
 * REMARK, if AMMO has to be added, use an object.
 */
public abstract class Weapon {
	
	/**
	 * Initialize a weapon with a certain projectile mass, damage, a certain cost to shoot it and a baseForce/maxForce.
	 * 
	 * @param mass The mass of this weapon's projectiles
	 * @param damage The damage this weapon inflict.
	 * @param cost The cost to use this weapon.
	 * @param baseForce The base force used to shoot projectiles.
	 * @param maxForce The max force used to shoot projectiles.
	 * 
	 * @post | getProjectileMass() == mass
	 * @post | getForce(0) == baseForce
	 * @post | getForce(100) == maxForce
	 * 
	 * @throws IllegalArgumentException 
	 * 			| !isValidProjectileMass(mass)
	 */
	protected Weapon(double mass, int damage, int cost, double baseForce, double maxForce) throws IllegalArgumentException {
		if(!isValidProjectileMass(mass))
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
	public static boolean isValidProjectileMass(double mass) {
		if(mass < 0)
			return false;
		
		return true;
	}
	
	/**
	 * @return The mass of this weapon.
	 */
	@Basic @Immutable
	public double getProjectileMass() {
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
