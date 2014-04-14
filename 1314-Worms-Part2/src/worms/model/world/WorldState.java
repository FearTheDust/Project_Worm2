package worms.model.world;

/**
 * An enum to set the state of a world:
 * - initialisation while adding worms and food
 * - playing while there are still worms from different teams or multiple worms with no team
 * - ended when there is maximum one team or one worm with no team left
 * 
 * @author Coosemans Brent
 * @author Derkinderen Vincent
 */
public enum WorldState {
	INITIALISATION,
	PLAYING,
	ENDED
}
