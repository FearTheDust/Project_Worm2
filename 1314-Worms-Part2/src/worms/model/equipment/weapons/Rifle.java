package worms.model.equipment.weapons;

/**
 * A Rifle is a Weapon with a mass of 0.01 kg, a cost of 10 and a base damage of 20.
 * 
 * @author Derkinderen Vincent
 * @author Coosemans Brent
 * 
 */
public class Rifle extends Weapon {
	
	/**
	 * The mass of a rifle.
	 */
	public final double RIFLE_MASS = 0.01;
	
	/**
	 * The cost to shoot with a rifle.
	 */
	public final int RIFLE_SHOOTCOST = 10;
	
	/**
	 * The base damage a rifle inflicts.
	 */
	public final int RIFLE_BASEDAMAGE = 20;
	
	/**
	 * The base force amount of a rifle.
	 */
	public final double RIFLE_BASEFORCE = 1.5;

	/**
	 * The maximum force amount of a rifle.
	 */
	public final double RIFLE_MAXFORCE = 1.5;
	
	/**
	 * Initialize a Rifle with a mass of 0.01 kg (10g), damage infliction of 20 and a cost to shoot of 10.
	 */
	public Rifle() {
		super(0.01, 20, 10, 1.5, 1.5);
	}

}
