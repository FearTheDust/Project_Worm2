package worms.model.world.entity;

import be.kuleuven.cs.som.annotate.Immutable;
import worms.model.equipment.weapons.Weapon;
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
	 * @param position The position where the Weapon Projectile starts from.
	 * @param angle The angle representing the orientation where the Weapon Projectile is fired at.
	 * @param forceTime The time a force is exerted on the Weapon Projectile.
	 * @param usedWeapon The weapon used to fire this Weapon Projectile.
	 * 
	 * @effect super(position, angle, forceTime)
	 * @throws IllegalArgumentException
	 * 			| !isValidWeapon(usedWeapon)
	 */
	public WeaponProjectile(Position position, double angle, double forceTime, Weapon usedWeapon) throws IllegalArgumentException {
		super(position, angle, forceTime);
		
		if(!isValidWeapon(usedWeapon))
			throw new IllegalArgumentException("Invalid weapon to create a WeaponProjectile.");
		
		this.usedWeapon = usedWeapon;
	}
	
	private final Weapon usedWeapon;

	@Override
	public double getForce(double propulsionYield) {
		return usedWeapon.getForce(propulsionYield);
	}

	@Override
	public double getMass() {
		return usedWeapon.getProjectileMass();
	}

	@Override @Immutable
	public double getDensity() {
		return 7800;
	}
	
	/**
	 * Returns whether or not the weapon is a valid weapon.
	 * @return  | if (weapon == null)
	 * 			| then result == false
	 */
	public static boolean isValidWeapon(Weapon weapon) {
		if(weapon == null)
			return false;
		
		return true;
	}

}
