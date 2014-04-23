package worms.model.equipment.weapons;

import be.kuleuven.cs.som.annotate.Raw;
import worms.model.world.entity.Worm;

/**
 * Brent's weapon of joy.
 * Boom - instant kill - headshot!
 * 
 * @author Coosemans Brent
 * @author Derkinderen Vincent
 *
 */
public class BrentsWeaponOfDoom extends Weapon {

	/**
	 * The mass of a Brent's Weapon of Doom projectile.
	 */
	public static final double DOOM_PROJECTILE_MASS = 0.5;
	
	/**
	 * The cost to shoot with a Brent's Weapon of Doom.
	 */
	public static final int DOOM_SHOOTCOST = 500;
	
	/**
	 * The base damage a Brent's Weapon of Doom inflicts.
	 */
	public static final int DOOM_BASEDAMAGE = 1000;
	
	/**
	 * The base force amount of a Brent's Weapon of Doom.
	 */
	public static final double DOOM_BASEFORCE = 50.0;

	/**
	 * The maximum force amount of a Brent's Weapon of Doom.
	 */
	public static final double DOOM_MAXFORCE = 100.0;	
	
	@Raw
	public BrentsWeaponOfDoom(Worm owner) {
		super(owner, DOOM_PROJECTILE_MASS, DOOM_BASEDAMAGE, DOOM_SHOOTCOST, DOOM_BASEFORCE, DOOM_MAXFORCE);
	}

	@Override
	public String getName() {
		return "Brent's Weapon of Doom";
	}

}
