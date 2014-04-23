package worms.model.equipment.weapons;

import be.kuleuven.cs.som.annotate.*;
import worms.model.world.entity.Worm;

/**
 * A Rifle is a Weapon with a projectile mass of RIFLE_MASS kg, a cost of RIFLE_SHOOTCOST, a base damage of RIFLE_BASEDAMAGE
 * and a base/max force of RIFLE_BASEFORCE/RIFLE_MAXFORCE N and an owner.
 * 
 * @author Derkinderen Vincent
 * @author Coosemans Brent
 */
public class Rifle extends Weapon {
	
	/**
	 * The mass of a rifle projectile.
	 */
	public static final double RIFLE_PROJECTILE_MASS = 0.01;
	
	/**
	 * The cost to shoot with a rifle.
	 */
	public static final int RIFLE_SHOOTCOST = 10;
	
	/**
	 * The base damage a rifle inflicts.
	 */
	public static final int RIFLE_BASEDAMAGE = 20;
	
	/**
	 * The base force amount of a rifle.
	 */
	public static final double RIFLE_BASEFORCE = 1.5;

	/**
	 * The maximum force amount of a rifle.
	 */
	public static final double RIFLE_MAXFORCE = 1.5;
	
	/**
	 * Initialize a Rifle with a projectile mass of RIFLE_PROJECTILE_MASS, damage infliction of RIFLE_BASEDAMAGE, 
	 * a cost to shoot of RIFLE_SHOOTCOST and a base force of RIFLE_BASEFORCE and a max force of RIFLE_MAXFORCE.
	 * 
	 * @param owner The owner of this weapon.
	 * 
	 * @effect super(owner, RIFLE_PROJECTILE_MASS, RIFLE_BASEDAMAGE, RIFLE_SHOOTCOST, RIFLE_BASEFORCE, RIFLE_MAXFORCE);
	 */
	@Raw
	public Rifle(Worm owner) {
		super(owner, RIFLE_PROJECTILE_MASS, RIFLE_BASEDAMAGE, RIFLE_SHOOTCOST, RIFLE_BASEFORCE, RIFLE_MAXFORCE);
	}
	
	@Override @Basic @Immutable
	public String getName() {
		return "Rifle";
	}

}
