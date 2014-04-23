package worms.model.world.entity;

import java.util.ArrayList;

import worms.model.equipment.weapons.Weapon;
import worms.util.Position;
import be.kuleuven.cs.som.annotate.*;

/**
 * Represents a projectile shot from a certain weapon.
 * 
 * @author Derkinderen Vincent
 * @author Coosemans Brent
 * 
 * @invar The usedWeapon is always a valid Weapon
 * 			| this.isValidWeapon(this.getUsedWeapon())
 */
public class WeaponProjectile extends Projectile {

	/**
	 * Initialize a Weapon Projectile of a certain weapon, with a certain angle, starting from a certain position with a certain time the force is exerted.
	 * 
	 * @param position The position where the Weapon Projectile starts from.
	 * @param angle The angle representing the orientation where the Weapon Projectile is fired at.
	 * @param forceTime The time a force is exerted on the Weapon Projectile.
	 * @param propulsionYield The propulsionYield where this WeaponProjectile is shot with.
	 * @param usedWeapon The weapon used to fire this Weapon Projectile.
	 * 
	 * @effect super(usedWeapon().getOwner().getWorld(), position, angle, forceTime, propulsionYield)
	 * 
	 * @post The used weapon of this projectile is usedWeapon.
	 * 		| new.getUsedWeapon() == usedWeapon
	 * @post The mass of this projectile is the projectilemass of the used weapon.
	 * 		| new.getMass() == usedWeapon.getProjectileMass()
	 * @post The world of this projectile is the world of the owner of the used weapon.
	 * 		| new.getWorld() == usedWeapon.getOwner().getWorld()
	 * 
	 * @throws IllegalArgumentException
	 * 			| !isValidWeapon(usedWeapon)
	 */
	@Raw
	public WeaponProjectile(Position position, double angle, double forceTime, double propulsionYield, Weapon usedWeapon) throws IllegalArgumentException {
		super(usedWeapon.getOwner().getWorld(), position, angle, forceTime, propulsionYield);
		if(!isValidWeapon(usedWeapon))
			throw new IllegalArgumentException("Invalid weapon to create a WeaponProjectile.");
		this.usedWeapon = usedWeapon;
	}

	@Override
	public double getForce() {
		return usedWeapon.getForce(this.getPropulsionYield());
	}

	@Override @Basic
	public final double getMass() {
		return usedWeapon.getProjectileMass();
	}
	
	/**
	 * Returns the weapon used to shot this WeaponProjectile.
	 */
	@Basic @Immutable
	public final Weapon getUsedWeapon() {
		return usedWeapon;
	}
	
	private final Weapon usedWeapon;

	@Override @Basic @Immutable
	public double getDensity() {
		return 7800;
	}
	
	/**
	 * Returns whether or not the weapon is a valid weapon.
	 * 
	 * @return  False if the weapon is null.
	 * 			| if (weapon == null) then 
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
	 * @param timeStep The time Step to check the position for.
	 * 
	 * @return The time to reach an impassable location or to leave the world boundaries or to hit a worm (excl itself).
	 * 			| double loopTime = timeStep;
	 * 			| Position calculatedPosition = this.getPosition();
	 * 			| ArrayList<Worm> hits = new ArrayList<Worm>();
	 *			|
	 *			| while(this.getWorld().liesWithinBoundaries(calculatedPosition, this.getRadius()) &&
	 *			|	!this.getWorld().isImpassable(calculatedPosition, this.getRadius())
	 *			|		&& !(hits.size()>1) && !(hits.size()==1 && !hits.contains(this.getUsedWeapon().getOwner())))
	 *			|			calculatedPosition = this.jumpStep(loopTime)
	 *			|			loopTime += timeStep
	 *			|			hits = this.getWorld().hitsWorm(calculatedPosition, this.getRadius())
	 *			| result == loopTime
	 */
	public double jumpTime(double timeStep) {
		double loopTime = timeStep;
		Position calculatedPosition = this.getPosition();
		ArrayList<Worm> hits = new ArrayList<Worm>();
		
		double calcRadius = this.getRadius(); //So we don't recalculate this too many times.
		
		while(this.getWorld().liesWithinBoundaries(calculatedPosition, calcRadius) &&
				!this.getWorld().isImpassable(calculatedPosition, calcRadius)
				&& !(hits.size()>1) && !(hits.size()==1 && !hits.contains(this.getUsedWeapon().getOwner()))) {
			calculatedPosition = this.jumpStep(loopTime);
			loopTime += timeStep;
			hits = this.getWorld().hitsWorm(calculatedPosition, calcRadius);
		}
		return loopTime;
	}
	
	/**
	 * Lets this weaponProjectile jump to a certain location and inflict damage on any worm hit.
	 * 
	 * @param timeStep The time step with which we calculate the time needed to reach the final position.
	 * 
	 * @effect	Initiate jump of our superclass.
	 * 			| super.jump(timeStep)
	 * @effect All worms within the radius of the new projectile's position will be hit.
	 * 			| for each Worm shotWorm in new.getWorld().hitsWorm(new.getPosition(), new.getRadius());
	 * 			|	shotWorm.inflictHitDamage(new.getUsedWeapon().getDamage())
	 */
	public void jump(double timeStep) {
		super.jump(timeStep);
		ArrayList<Worm> hitList = this.getWorld().hitsWorm(this.getPosition(), this.getRadius());
		for(Worm shotWorm : hitList) {
			shotWorm.inflictHitDamage(this.getUsedWeapon().getDamage());
		}
	}

}
