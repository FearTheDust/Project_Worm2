package worms.model.equipment.weapons;

/**
 * A Rifle is a Weapon with a projectile mass of RIFLE_MASS kg, a cost of RIFLE_SHOOTCOST, a base damage of RIFLE_BASEDAMAGE
 * and a base/max force of RIFLE_BASEFORCE/RIFLE_MAXFORCE N.
 * 
 * @author Derkinderen Vincent
 * @author Coosemans Brent
 * 
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
	 * Initialize a Rifle with a projectile mass of RIFLE_PROJECTILE_MASS, damage infliction of RIFLE_BASEDAMAGE, a cost to shoot of RIFLE_SHOOTCOST and a base force of RIFLE_BASEFORCE and a max force of RIFLE_MAXFORCE.
	 */
	public Rifle() {
		super(RIFLE_PROJECTILE_MASS, RIFLE_BASEDAMAGE, RIFLE_SHOOTCOST, RIFLE_BASEFORCE, RIFLE_MAXFORCE);
	}

}