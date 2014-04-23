package worms.model.equipment.weapons;

import worms.model.Constants;
import worms.model.world.entity.*;
import worms.util.Position;
import be.kuleuven.cs.som.annotate.*;

/**
 * Abstract class representing a Weapon with a certain owner, projectile mass, damage, cost to shoot and base & maximum force.
 * 
 * @author Derkinderen Vincent
 * @author Coosemans Brent
 * 
 * @invar	The mass of this Weapon projectile is at all times valid.
 * 			| isValidProjectileMass(this.getProjectileMass())
 * @invar	The owner of this Weapon is never null.
 * 			| this.getOwner() != null
 *
 * REMARK, if AMMO has to be added, use an object, might be easier.
 */
public abstract class Weapon {
	
	/**
	 * Initialize a weapon with a certain owner, projectile mass, damage, a certain cost to shoot it and a baseForce/maxForce.
	 * 
	 * @param owner The owner of this weapon.
	 * @param mass The mass of this weapon's projectiles
	 * @param damage The damage this weapon inflict.
	 * @param cost The cost to use this weapon.
	 * @param baseForce The base force used to shoot projectiles.
	 * @param maxForce The max force used to shoot projectiles.
	 * 
	 * @post The owner of the new Weapon is owner.
	 * 			| new.getOwner() == owner
	 * @post The ProjectileMass of the new Weapon is mass.
	 * 			| new.getProjectileMass() == mass
	 * @post The force with propulsionYield == 0 is equal to baseForce.
	 * 			| new.getForce(0) == baseForce
	 * @post The force with propulsionYield == 100 is equal to maxForce.
	 * 			| new.getForce(100) == maxForce
	 * 
	 * @throws IllegalArgumentException 
	 * 			When mass isn't a valid ProjectileMass.
	 * 			| !isValidProjectileMass(mass)
	 * @throws IllegalArgumentException
	 * 			When the owner is a null reference or isn't alive.
	 * 			| (owner == null || !owner.isAlive())
	 */
	@Raw
	public Weapon(Worm owner, double mass, int damage, int cost, double baseForce, double maxForce) throws IllegalArgumentException {
		if(!isValidProjectileMass(mass))
			throw new IllegalArgumentException("The provided mass for this Weapon isn't valid.");
		if(owner == null || !owner.isAlive())
			throw new IllegalArgumentException("The owner of this weapon musn't be a null reference and it must be alive.");
		
		this.owner = owner;
		this.mass = mass;
		this.damage = damage;
		this.cost = cost;
		this.baseForce = baseForce;
		this.maxForce = maxForce;
	}
	
	/**
	 * Returns the owner of this weapon.
	 */
	@Basic @Immutable @Raw
	public Worm getOwner() {
		return this.owner;
	}
	
	private Worm owner;
	
	/**
	 * Returns the damage this weapon can inflict by default.
	 */
	@Basic @Immutable
	public int getDamage() {
		return damage;
	}
	
	private final int damage;

	
	/**
	 * Check if the provided mass is a valid mass for this Weapon.
	 * 
	 * @return	False if the mass is less than zero.
	 * 			| if(mass < 0) then 
	 * 			|	result == false
	 */
	public static boolean isValidProjectileMass(double mass) {
		if(mass < 0)
			return false;	
		return true;
	}
	
	/**
	 * Returns the mass of this weapon's projectile.
	 */
	@Basic @Immutable
	public double getProjectileMass() {
		return mass;
	}
	
	private final double mass;
	
	/**
	 * Returns The amount of action points it costs to shoot with this weapon.
	 */
	@Basic
	public int getCost() {
		return cost;
	}

	private final int cost;
	
	/**
	 * The force which the weapon exerts on the projectile.
	 * 
	 * @param propulsionYield
	 * 
	 * @return The force calculated based on the propulsionYield.
	 * 			| baseForce + (maxForce-baseForce) * propulsionYield / 100;
	 * 
	 * @throws IllegalArgumentException
	 * 			When the propulsionYield isn't a valid propulsionYield.
	 * 			| !Projectile.isValidPropulsionYield(propulsionYield)
	 */
	public double getForce(double propulsionYield) throws IllegalArgumentException {
		if(!Projectile.isValidPropulsionYield(propulsionYield))
			throw new IllegalArgumentException();
		return baseForce + (maxForce-baseForce) * propulsionYield / 100.0;
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
	 * Returns the name of this weapon.
	 */
	@Basic
	public abstract String getName();
	
	/**
	 * Create a projectile for this weapon for the owner of this weapon.
	 * The projectile will be placed right outside the radius of the owner depending on the angle of the owner.
	 * The projectile will have a force time of Constants.FORCE_TIME, the angle of the owner, the world of the owner and a provided propulsionYield.
	 * The owner will pay an AP cost to shoot this.
	 * If the owner does not have enough AP to shoot, null will return and no projectile will be created.
	 * 
	 * @param propulsionYield The propulsionYield for this projectile.
	 * 
	 * @post 	The owner of this weapon will have an amount of AP less than or equal to the amount of AP before.
	 * 			| new.getCurrentActionPoints() <= this.getCurrentActionPoints()
	 * 
	 * @return/@effect The projectile created.
	 * 			| double x = this.getOwner().getPosition().getX() + this.getOwner().getRadius() * Math.cos(this.getOwner().getAngle());
	 * 			| double y = this.getOwner().getPosition().getY() + this.getOwner().getRadius() * Math.sin(this.getOwner().getAngle());
	 * 			| Position proPosition = new Position(x,y);
	 * 			| WeaponProjectile projectile = new WeaponProjectile(this.getOwner().getWorld(), proPosition, 
	 * 			|	this.getOwner().getAngle(), Constants.FORCE_TIME, propulsionYield, this);
	 * 			| result = projectile
	 * @return When not enough AP are available or the owner isn't alive, null will be returned
	 * 			| if(!this.getOwner().isAlive() || this.getOwner().getCurrentActionPoints() < this.getCost())
	 * 			|	result = null
	 * 
	 * @throws IllegalStateException
	 * 			When the owner doesn't seem to be alive.
	 * 			| !(this.getOwner().isAlive())
	 */
	public WeaponProjectile createProjectile(double propulsionYield) throws IllegalStateException {
		if(this.getOwner().getCurrentActionPoints() < this.getCost())
			return null;
		
		if(!this.getOwner().isAlive())
			throw new IllegalStateException("The owner of this weapon seems to be dead, oops.");
		
		double x = this.getOwner().getPosition().getX() + this.getOwner().getRadius() * Math.cos(this.getOwner().getAngle());
		double y = this.getOwner().getPosition().getY() + this.getOwner().getRadius() * Math.sin(this.getOwner().getAngle());
		Position proPosition = new Position(x,y);
		WeaponProjectile projectile = new WeaponProjectile(proPosition, this.getOwner().getAngle(), Constants.FORCE_TIME, propulsionYield, this);
		this.getOwner().decreaseActionPointsBy(this.getCost());
		
		return projectile;
	}
	
}
