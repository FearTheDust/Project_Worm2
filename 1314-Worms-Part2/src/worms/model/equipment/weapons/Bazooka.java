package worms.model.equipment.weapons;

import be.kuleuven.cs.som.annotate.*;
import worms.model.world.entity.Worm;

/**
 * A Rifle is a Weapon with a projectile mass of BAZOOKA_PROJECTILE_MASS kg, a cost of BAZOOKA_SHOOTCOST, a base damage of BAZOOKA_BASEDAMAGE 
 * and a base/max force of BAZOOKA_BASEFORCE/BAZOOKA_MAXFORCE N and an owner.
 * 
 * @author Derkinderen Vincent
 * @author Coosemans Brent
 */
public class Bazooka extends Weapon {
	
	/**
	 * The mass of a bazooka projectile.
	 */
	public static final double BAZOOKA_PROJECTILE_MASS = 0.3;
	
	/**
	 * The cost to shoot with a bazooka.
	 */
	public static final int BAZOOKA_SHOOTCOST = 50;
	
	/**
	 * The base damage a bazooka inflicts.
	 */
	public static final int BAZOOKA_BASEDAMAGE = 80;
	
	/**
	 * The base force amount of a bazooka.
	 */
	public static final double BAZOOKA_BASEFORCE = 2.5;

	/**
	 * The maximum force amount of a bazooka.
	 */
	public static final double BAZOOKA_MAXFORCE = 9.5;

	
	/**
	 * Initialize a Bazooka with an owner, a projectile mass of BAZOOKA_PROJECTILE_MASS, damage infliction of BAZOOKA_BASEDAMAGE, 
	 * a cost to shoot of BAZOOKA_SHOOTCOST and a base force of BAZOOKA_BASEFORCE and a max force of BAZOOKA_MAXFORCE.
	 * 
	 * @param owner The owner of the Bazooka
	 * 
	 * @effect super(owner, BAZOOKA_PROJECTILE_MASS, BAZOOKA_BASEDAMAGE, BAZOOKA_SHOOTCOST, BAZOOKA_BASEFORCE, BAZOOKA_MAXFORCE);
	 */
	@Raw
	public Bazooka(Worm owner) {
		super(owner, BAZOOKA_PROJECTILE_MASS, BAZOOKA_BASEDAMAGE, BAZOOKA_SHOOTCOST, BAZOOKA_BASEFORCE, BAZOOKA_MAXFORCE);
	}
	
	@Override @Basic @Immutable
	public String getName() {
		return "Bazooka";
	}

}
