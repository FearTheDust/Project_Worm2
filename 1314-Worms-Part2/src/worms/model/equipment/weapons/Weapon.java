package worms.model.equipment.weapons;

import worms.model.Constants;
import worms.model.world.entity.Projectile;
import worms.model.world.entity.WeaponProjectile;
import worms.model.world.entity.Worm;
import worms.util.Position;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * Abstract class representing a Weapon with a certain owner, projectile mass, damage, cost to shoot and base & maximum force.
 * 
 * @author Derkinderen Vincent
 * @author Coosemans Brent
 * 
 * @invar	The mass of this Weapon projectile is at all times valid.
 * 			| isValidProjectileMass(this.getProjectileMass())
 * 
 * @invar	The owner of this Weapon is never null.
 * 			| this.getOwner() != null
 *
 * REMARK, if AMMO has to be added, use an object.
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
	 * @post | new.getOwner() == owner
	 * @post | new.getProjectileMass() == mass
	 * @post | new.getForce(0) == baseForce
	 * @post | new.getForce(100) == maxForce
	 * 
	 * @throws IllegalArgumentException 
	 * 			| !isValidProjectileMass(mass)
	 * 			| (owner == null || !owner.isAlive())
	 */
	protected Weapon(Worm owner, double mass, int damage, int cost, double baseForce, double maxForce) throws IllegalArgumentException {
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
	 * 			| !Projectile.isValidPropulsionYield(propulsionYield)
	 */
	public double getForce(double propulsionYield) throws IllegalArgumentException {
		if(!Projectile.isValidPropulsionYield(propulsionYield)) {
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
	 * Returns the name of this weapon.
	 */
	public abstract String getName();
	
	/**
	 * Create a projectile for this weapon for the owner of this weapon.
	 * The projectile will be placed right outside the radius of the owner depending on the angle of the owner.
	 * The projectile will have a force time of Constants.FORCE_TIME, the angle of the owner, the world of the owner and a provided propulsionYield.
	 * 
	 * @param propulsionYield The propulsionYield for this projectile.
	 * @return The projectile created.
	 * 			| double x = this.getOwner().getPosition().getX() + this.getOwner().getRadius() * Math.cos(this.getOwner().getAngle());
	 * 			| double y = this.getOwner().getPosition().getY() + this.getOwner().getRadius() * Math.sin(this.getOwner().getAngle());
	 * 			| Position proPosition = new Position(x,y);
	 * 			| WeaponProjectile projectile = new WeaponProjectile(this.getOwner().getWorld(), proPosition, 
	 * 			|	this.getOwner().getAngle(), Constants.FORCE_TIME, propulsionYield, this);
	 * 			| result == projectile
	 * 
	 * @throws IllegalStateException
	 * 			When the owner doesn't seem to be alive.
	 * 			| !(this.getOwner().isAlive())
	 */
	public WeaponProjectile createProjectile(double propulsionYield) throws IllegalStateException {
		if(!this.getOwner().isAlive())
			throw new IllegalStateException("The owner of this weapon seems to be dead, oops.");
		
		double x = this.getOwner().getPosition().getX() + this.getOwner().getRadius() * Math.cos(this.getOwner().getAngle());
		double y = this.getOwner().getPosition().getY() + this.getOwner().getRadius() * Math.sin(this.getOwner().getAngle());
		Position proPosition = new Position(x,y);
		WeaponProjectile projectile = new WeaponProjectile(this.getOwner().getWorld(), proPosition, this.getOwner().getAngle(), Constants.FORCE_TIME, propulsionYield, this);
		
		return projectile;
	}
	
	
	

}
