package worms.model.world.entity;

import java.util.ArrayList;

import be.kuleuven.cs.som.annotate.Immutable;
import worms.model.equipment.weapons.Weapon;
import worms.model.world.World;
import worms.util.Position;

/**
 * Represents a projectile shot from a certain weapon.
 * 
 * @author Derkinderen Vincent
 * @author Coosemans Brent
 *
 */
public class WeaponProjectile extends Projectile {

	/**
	 * Initialize a Weapon Projectile of a certain weapon, with a certain angle, starting from a certain position with a certain time the force is exerted.
	 * 
	 * @param world The world of this WeaponProjectile.
	 * @param position The position where the Weapon Projectile starts from.
	 * @param angle The angle representing the orientation where the Weapon Projectile is fired at.
	 * @param forceTime The time a force is exerted on the Weapon Projectile.
	 * @param propulsionYield The propulsionYield where this WeaponProjectile is shot with.
	 * @param usedWeapon The weapon used to fire this Weapon Projectile.
	 * 
	 * @effect super(world, position, angle, forceTime, propulsionYield, usedWeapon)
	 * @post	| new.getUsedWeapon() == usedWeapon
	 * @post	| new.getMass() == usedWeapon.getProjectileMass()
	 * 
	 * @throws IllegalArgumentException
	 * 			| !isValidWeapon(usedWeapon)
	 */
	public WeaponProjectile(World world, Position position, double angle, double forceTime, double propulsionYield, Weapon usedWeapon) throws IllegalArgumentException {
		super(world, position, angle, forceTime, propulsionYield);
		
		if(!isValidWeapon(usedWeapon))
			throw new IllegalArgumentException("Invalid weapon to create a WeaponProjectile.");
		
		this.usedWeapon = usedWeapon;
	}
	
	private final Weapon usedWeapon;

	@Override
	public double getForce() {
		return usedWeapon.getForce(this.getPropulsionYield());
	}

	@Override
	public final double getMass() {
		return usedWeapon.getProjectileMass();
	}
	
	/**
	 * Returns the weapon used to shot this WeaponProjectile.
	 */
	public final Weapon getUsedWeapon() {
		return usedWeapon;
	}

	@Override @Immutable
	public double getDensity() {
		return 7800;
	}
	
	/**
	 * Returns whether or not the weapon is a valid weapon.
	 * @return  | if (weapon == null) then 
	 * 			| result == false
	 */
	public static boolean isValidWeapon(Weapon weapon) {
		if(weapon == null)
			return false;
		
		return true;
	}
	
	/**
	 * Returns the jump time if jumped with this projectile's current angle.
	 * 
	 * TODO: Documentation formally
	 */
	public double jumpTime(double timeStep) {
		double loopTime = timeStep;
		Position calculatedPosition = this.getPosition();
		ArrayList<Worm> hits = new ArrayList<Worm>();
		
		while(this.getWorld().liesWithinBoundaries(calculatedPosition, this.getRadius()) &&
				//(!this.getWorld().isAdjacent(calculatedPosition, this.getRadius()) 
						//|| this.getPosition().distance(calculatedPosition) <= this.getRadius()) &&
				!this.getWorld().isImpassable(calculatedPosition, this.getRadius())
				&& !(hits.size()>1) &&
				!(hits.size()==1 && !hits.contains(this.usedWeapon.getOwner()))) {
			
			calculatedPosition = this.jumpStep(loopTime);
			loopTime += timeStep;
			hits = this.getWorld().hitsWorm(calculatedPosition, this.getRadius());
		}
	
		return loopTime;
	}
	
	/**
	 *TODO: Formal documentation
	 */
	public void jump(double jumpStep) {
		super.jump(jumpStep);
		
		ArrayList<Worm> hitList = this.getWorld().hitsWorm(this.getPosition(), this.getRadius());
		for(Worm shotWorm : hitList) {
			shotWorm.inflictHitDamage(this.getUsedWeapon().getDamage());
		}
	}

}
