package worms.model.world;

/**
 * An enum to set the state of a world:
 * - INITIALISATION while adding worms, food and teams.
 * - PLAYING while there are still worms from different teams or multiple worms with no team
 * - ENDED when there is one team, one worm with no team or no worm left alive in this world.
 * 
 * @author Coosemans Brent
 * @author Derkinderen Vincent
 */
public enum WorldState {
	INITIALISATION,
	PLAYING,
	ENDED
}
