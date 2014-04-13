package worms.model.world;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import worms.model.Facade;
import worms.model.IFacade;
import worms.model.Team;
import worms.model.equipment.weapons.Bazooka;
import worms.model.world.entity.*;
import worms.util.Position;

/**
 * @author Derkinderen Vincent
 * @author Coosemans Brent
 *
 */
public class WorldTest {

	private IFacade facade;

	private Random random;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		facade = new Facade();
		random = new Random(7357);
	}

	/**
	 * Test method for {@link worms.model.world.World#World(double, double, boolean[][], java.util.Random)}.
	 * Test trying to create a world with a null reference as Random.
	 */
	@SuppressWarnings("unused")
	@Test(expected=IllegalArgumentException.class)
	public void testWorld_IllegalRandom() {
		World world = facade.createWorld(3.0, 4.0, new boolean[][] {
				{ true, false, true },
				{ true, true, true },
				{ true, true, true },
				{ false, false, false }
		}, null);
	}

	/**
	 * Test method for {@link worms.model.world.World#World(double, double, boolean[][], java.util.Random)}.
	 * Test trying to create a world with null as passableMap.
	 */
	@SuppressWarnings("unused")
	@Test(expected=IllegalArgumentException.class)
	public void testWorld_IllegalPassableMap() {
		World world = facade.createWorld(3.0, 4.0, null, random);
	}

	/**
	 * Test method for {@link worms.model.world.World#isValidDimension(double, double)}.
	 * Test if (1,1) and (0,0) are valid Dimensions.
	 * Test if (-1,2) and (2, Double.POSITIVE_INFINITY) and (Double.NaN, 2) are invalid dimensions.
	 */
	@Test
	public void testIsValidDimension() {
		assertTrue(World.isValidDimension(1, 1));
		assertTrue(World.isValidDimension(0, 0));
		assertFalse(World.isValidDimension(-1, 2));
		assertFalse(World.isValidDimension(2, Double.POSITIVE_INFINITY));
		assertFalse(World.isValidDimension(Double.NaN, 2));
	}

	/**
	 * Test method for {@link worms.model.world.World#add(worms.model.Team)}.
	 * Tests whether an added team is in the team list and a not added team isn't.
	 */
	@Test
	public void testAddTeam() {
		// . X .
		// . . .
		// . . .
		// X X X
		World world = facade.createWorld(3.0, 4.0, new boolean[][] {
				{ true, false, true },
				{ true, true, true },
				{ true, true, true },
				{ false, false, false }
		}, random);

		Team team1 = new Team("TeamNumberOne");
		Team team2 = new Team("TeamNumberTwo");
		Team team17 = new Team("TeamSeventeen");
		world.add(team1);
		world.add(team2);
		assertTrue(world.getTeams().contains(team1));
		assertTrue(world.getTeams().contains(team2));
		assertFalse(world.getTeams().contains(team17));
	}

	/**
	 * Test method for {@link worms.model.world.World#add(worms.model.Team)}.
	 * Tests that there can't be more teams in the team list than the maximum allowed amount.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAddMaxTeam() {
		// . X .
		// . . .
		// . . .
		// X X X
		World world = facade.createWorld(3.0, 4.0, new boolean[][] {
				{ true, false, true },
				{ true, true, true },
				{ true, true, true },
				{ false, false, false }
		}, random);

		Team team1 = new Team("TeamNumberOne");
		Team team2 = new Team("TeamNumberTwo");
		Team team3 = new Team("TeamNumberThree");
		Team team4 = new Team("TeamNumberFour");
		Team team5 = new Team("TeamNumberFive");
		Team team6 = new Team("TeamNumberSix");
		Team team7 = new Team("TeamNumberSeven");
		Team team8 = new Team("TeamNumberEight");
		Team team9 = new Team("TeamNumberNine");
		Team team10 = new Team("TeamNumberTen");
		Team team17 = new Team("TeamSeventeen");
		world.add(team1);
		world.add(team2);
		world.add(team3);
		world.add(team4);
		world.add(team5);
		world.add(team6);
		world.add(team7);
		world.add(team8);
		world.add(team9);
		world.add(team10);
		world.add(team17);
	}

	/**
	 * Test method for {@link worms.model.world.World#add(worms.model.world.entity.GameObject)}.
	 * Test adding null as game object.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAddGameObject_Illegal_Null() {
		// . 
		World world = facade.createWorld(1.0, 1.0, new boolean[][] {
				{ true }
		}, random);
		GameObject gameobject = null;
		world.add(gameobject);
	}
	
	/**
	 * Test method for {@link worms.model.world.World#add(worms.model.world.entity.GameObject)}.
	 * Test adding a game object outside of the boundaries of the world.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAddGameObject_Illegal_Boundary() {
		// . 
		World world = facade.createWorld(1.0, 1.0, new boolean[][] {
				{ true }
		}, random);
		Worm worm = new Worm(world, new Position(0.5,0.5), 0, 0.5, "Test");
		worm.move();
		world.add(worm);
	}
	
	/**
	 * Test method for {@link worms.model.world.World#add(worms.model.world.entity.GameObject)}.
	 * Tests adding a dead game object.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAddGameObject_Illegal_Dead() {
		// . 
		World world = facade.createWorld(1.0, 1.0, new boolean[][] {
				{ true }
		}, random);
		Worm worm = new Worm(world, new Position(0.5,0.5), 0, 0.5, "Test");
		worm.inflictHitDamage(Integer.MAX_VALUE);
		world.add(worm);
	}
	
	/**
	 * Test method for {@link worms.model.world.World#add(worms.model.world.entity.GameObject)}.
	 * Tests adding a projectile during initialization.
	 */
	@Test(expected = IllegalStateException.class)
	public void testAddGameObject_Illegal_ProjectileState() {
		// . 
		World world = facade.createWorld(1.0, 1.0, new boolean[][] {
				{ true }
		}, random);
		Worm worm = new Worm(world, new Position(0.5,0.5), 0, 0.5, "Test");
		Bazooka bazooka = new Bazooka(worm);
		WeaponProjectile weaponProjectile = new WeaponProjectile(world, new Position(0.5,0.5), 0, 0.5, 100, bazooka);
		world.add(weaponProjectile);
	}
	
	/**
	 * Test method for {@link worms.model.world.World#add(worms.model.world.entity.GameObject)}.
	 * Tests adding a game object already added.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAddGameObject_Illegal_Already_Added() {
		// . 
		World world = facade.createWorld(1.0, 1.0, new boolean[][] {
				{ true }
		}, random);
		Worm worm = new Worm(world, new Position(0.5,0.5), 0, 0.5, "Test");
		world.add(worm);
	}
	
	/**
	 * Test method for {@link worms.model.world.World#add(worms.model.world.entity.GameObject)}.
	 * Tests adding a game object while in the playing state.
	 */
	@Test(expected = IllegalStateException.class)
	public void testAddGameObject_Illegal_PlayingState() {
		// . 
		World world = facade.createWorld(1.0, 1.0, new boolean[][] {
				{ true }
		}, random);
		Worm worm = new Worm(world, new Position(0.5,0.5), 0, 0.5, "Test");
		Worm worm2 = new Worm(world, new Position(0.5,0.5), 0, 0.5, "Test");
		Worm worm3 = new Worm(world, new Position(0.5,0.5), 0, 0.5, "Test");
		world.startGame();
		world.add(worm3);
	}
	
	

	/**
	 * Test method for {@link worms.model.world.World#liesWithinBoundaries(worms.model.world.entity.GameObject)}.
	 * Test a worm being inside the world and a worm who moves out.
	 */
	@Test
	public void testLiesWithinBoundaries() {
		// . X .
		// . . .
		// . . .
		// X X X
		World world = facade.createWorld(3.0, 4.0, new boolean[][] {
				{ true, false, true },
				{ true, true, true },
				{ true, true, true },
				{ false, false, false }
		}, random);

		Worm worm = facade.createWorm(world, 1.5, 1.5, 0, 0.5, "Test");
		Worm worm2 = facade.createWorm(world, 0.5, 1.5, Math.PI, 0.5, "Test2");
		worm2.move();
		assertTrue(world.liesWithinBoundaries(worm));
		assertFalse(world.liesWithinBoundaries(worm2));
	}

	/**
	 * Test method for {@link worms.model.world.World#nextTurn()}.
	 * Test with 3 created worms.
	 * When called nextTurn and is in Initialization, getActiveWorm() is still null because we did nothing.
	 * When called nextTurn after startGame, the current ActiveWorm() is the second worm.
	 * Also the hitpoints of the second worm increased with 10 and maximum AP is set.
	 * When called nextTurn after 2 worms died, the gameState is equal to ENDED.
	 */
	@Test
	public void testNextTurn() {
		// . X .
		// . . .
		// . . .
		// X X X
		World world = facade.createWorld(3.0, 4.0, new boolean[][] {
				{ true, false, true },
				{ true, true, true },
				{ true, true, true },
				{ false, false, false }
		}, random);

		Worm worm = facade.createWorm(world, 1.5, 1.5, 0, 0.5, "Test");
		Worm worm2 = new Worm(world, new Position(1.5, 1.5), 0, 0.5, "Test2", 10, 10);
		Worm worm3 = facade.createWorm(world, 1.5, 1.5, 0, 0.5, "Test3");
		
		world.nextTurn();
		assertEquals(world.getActiveWorm(), null);
		world.startGame();
		world.nextTurn();
		assertEquals(world.getActiveWorm(), worm2);
		assertEquals(worm2.getCurrentHitPoints(), 20);
		assertEquals(worm2.getMaximumActionPoints(), worm2.getCurrentActionPoints());
		
		worm.inflictHitDamage(worm.getCurrentHitPoints());
		worm3.inflictHitDamage(worm3.getCurrentHitPoints());
		world.nextTurn();
		assertEquals(world.getState(), WorldState.ENDED);
	}

	/**
	 * Test method for {@link worms.model.world.World#startGame()}.
	 * Already tested nextTurn.
	 */
	@Test
	public void testStartGame() {
	}

	/**
	 * Test method for {@link worms.model.world.World#gameEnded()}.
	 * Tests whether the game ended when there are no worms.
	 */
	@Test
	public void testGameEnded() {
		// . X .
		// . . .
		// . . .
		// X X X
		World world = facade.createWorld(3.0, 4.0, new boolean[][] {
				{ true, false, true },
				{ true, true, true },
				{ true, true, true },
				{ false, false, false }
		}, random);
		world.startGame();
		assertTrue(world.gameEnded());
	}

	/**
	 * Test method for {@link worms.model.world.World#gameEnded()}.
	 * Tests whether the game ended when there is only one worm.
	 */
	@Test
	public void testGameEnded_OneWorm() {
		// . X .
		// . . .
		// . . .
		// X X X
		World world = facade.createWorld(3.0, 4.0, new boolean[][] {
				{ true, false, true },
				{ true, true, true },
				{ true, true, true },
				{ false, false, false }
		}, random);
		Worm worm = facade.createWorm(world, 1.5, 1.5, 0, 1, "Test");
		world.startGame();
		assertTrue(world.gameEnded());
	}

	/**
	 * Test method for {@link worms.model.world.World#gameEnded()}.
	 * Tests whether the game ended when there is only one team left.
	 */
	@Test
	public void testGameEnded_SameTeam() {
		// . X .
		// . . .
		// . . .
		// X X X
		World world = facade.createWorld(3.0, 4.0, new boolean[][] {
				{ true, false, true },
				{ true, true, true },
				{ true, true, true },
				{ false, false, false }
		}, random);
		Team team17 = new Team("TeamSeventeen");
		Worm worm1 = facade.createWorm(world, 1.5, 1.5, 0, 1, "Test1");
		Worm worm2 = facade.createWorm(world, 1.5, 1.5, 0, 1, "Test2");
		team17.add(worm1);
		team17.add(worm2);
		world.add(team17);
		world.startGame();
		assertTrue(world.gameEnded());
	}

	/**
	 * Test method for {@link worms.model.world.World#getNextWorm()}.
	 * Tests whether the second worm added will be the next worm to get a turn.
	 */
	@Test
	public void testGetNextWorm() {
		// . X .
		// . . .
		// . . .
		// X X X
		World world = facade.createWorld(3.0, 4.0, new boolean[][] {
				{ true, false, true },
				{ true, true, true },
				{ true, true, true },
				{ false, false, false }
		}, random);

		Worm worm1 = facade.createWorm(world, 1.5, 1.5, 0, 1, "Test1");
		Worm worm2 = facade.createWorm(world, 1.5, 1.5, 0, 1, "Test2");
		world.startGame();
		assertEquals(world.getNextWorm(),worm2);
	}

	/**
	 * Test method for {@link worms.model.world.World#isImpassable(worms.util.Position, double)}.
	 * Test if a certain "tile" is impassable in the world.
	 */
	@Test
	public void testIsImpassable() {
		// . X .
		// . . .
		// . . .
		// X X X
		World world = facade.createWorld(3.0, 4.0, new boolean[][] {
				{ true, false, true },
				{ true, true, true },
				{ true, true, true },
				{ false, false, false }
		}, random);

		Worm worm = facade.createWorm(world, 1.5, 1.5, 0, 1, "Test");
		assertTrue(world.isImpassable(worm.getPosition(), worm.getRadius()));
	}

	/**
	 * Test method for {@link worms.model.world.World#isAdjacent(worms.util.Position, double)}.
	 * Test if a worm is adjacent near an impassable "tile" as described in the assignment.
	 */
	@Test
	public void testIsAdjacent() {
		// . X .
		// . . .
		// . . .
		// X X X
		World world = facade.createWorld(3.0, 4.0, new boolean[][] {
				{ true, false, true },
				{ true, true, true },
				{ true, true, true },
				{ false, false, false }
		}, random);

		Worm worm = facade.createWorm(world, 1.5, 1.5, 0, 0.5, "Graag 20op20");
		assertTrue(world.isAdjacent(worm.getPosition(), worm.getRadius()));
	}

	/**
	 * Test method for {@link worms.model.world.World#isPassableTile(worms.util.Position)}.
	 * Test if a tile is passable when it is.
	 * Test if a tile isn't passable when it isn't.
	 */
	@Test
	public void testIsPassableTile() {
		// . X .
		// . . .
		// . . ."
		// X X X
		World world = facade.createWorld(3.0, 4.0, new boolean[][] {
				{ true, false, true },
				{ true, true, true },
				{ true, true, true },
				{ false, false, false }
		}, random);

		assertTrue(world.isPassableTile(new Position(2,2)));
		assertFalse(world.isPassableTile(new Position(1,0.5)));
	}

	/**
	 * Test method for {@link worms.model.world.World#hitsWorm(worms.util.Position, double)}.
	 * Test if 2 worms are detected as a "hit" when they are within the radius of a position.
	 */
	@Test
	public void testHitsWorm() {
		// . X .
		// . . .
		// . . .
		// X X X
		World world = facade.createWorld(3.0, 4.0, new boolean[][] {
				{ true, false, true },
				{ true, true, true },
				{ true, true, true },
				{ false, false, false }
		}, random);

		facade.createWorm(world, 1.5, 1.5, 0, 1, "Vincent");
		facade.createWorm(world, 1.6, 1.6, 0, 1, "Brent");

		assertEquals(world.hitsWorm(new Position(1.4, 1.4), 0.5).size(), 2);
	}

	/**
	 * Test method for {@link worms.model.world.World#eatableFood(worms.util.Position, double)}.
	 * Test if 2 Food are detected as eatable when they are within the radius of a position.
	 */
	@Test
	public void testEatableFood() {
		// . X .
		// . . .
		// . . .
		// X X X
		World world = facade.createWorld(3.0, 4.0, new boolean[][] {
				{ true, false, true },
				{ true, true, true },
				{ true, true, true },
				{ false, false, false }
		}, random);

		facade.createFood(world, 1.5, 1.5);
		facade.createFood(world, 1.6, 1.6);

		assertEquals(world.eatableFood(new Position(1.4, 1.4), 0.3).size(), 2);
	}

	/**
	 * Test method for {@link worms.model.world.World#getRandomPassablePos(double)}.
	 * Test whether a position returned from getRandomPassablePos is passable when it isn't null.
	 */
	@Test
	public void testGetRandomPassablePos() {
		double radius = 0.5;
		// . . .
		// . . .
		// . . .
		// X X X
		World world = facade.createWorld(3.0, 4.0, new boolean[][] {
				{ true, true, true },
				{ true, true, true },
				{ true, true, true },
				{ false, false, false }
		}, random);

		Position position = world.getRandomPassablePos(radius);

		if(position != null)
			assertTrue(!world.isImpassable(position, radius));
	}

	/**
	 * Test method for {@link worms.model.world.World#getWinner()}.
	 * Test if getWinner() returns the team's name of the worm alive when he has a team.
	 */
	@Test
	public void testGetWinner_Team() {
		// . X .
		// . . .
		// . . .
		// X X X
		World world = facade.createWorld(3.0, 4.0, new boolean[][] {
				{ true, false, true },
				{ true, true, true },
				{ true, true, true },
				{ false, false, false }
		}, random);

		assertTrue(world.getWinner() == null);
		Team team = new Team("Awesomeness");
		world.add(team);

		Worm vincent = facade.createWorm(world, 1.5, 1.5, 0, 1, "Vincent");
		Worm brent =  facade.createWorm(world, 1.5, 1.5, 0, 1, "Brent");
		team.add(vincent);
		team.add(brent);

		assertTrue(world.getWinner().equals("Awesomeness"));
	}

	/**
	 * Test method for {@link worms.model.world.World#getWinner()}.
	 * Test if getWinner() returns null when no worms are alive in the world.
	 * Test if getWinner() returns the worm's name if there is a worm alive without a team.
	 */
	@Test
	public void testGetWinner() {
		// . X .
		// . . .
		// . . .
		// X X X
		World world = facade.createWorld(3.0, 4.0, new boolean[][] {
				{ true, false, true },
				{ true, true, true },
				{ true, true, true },
				{ false, false, false }
		}, random);

		assertTrue(world.getWinner() == null);
		facade.createWorm(world, 1.5, 1.5, 0, 1, "Vincent");
		assertTrue(world.getWinner().equals("Vincent"));
	}

	/**
	 * Test method for {@link worms.model.world.World#getObjectsOfType(java.lang.Class)}.
	 * Adds a worm and a Food object to a world confirm that for both types there is only 1 in the list received.
	 */
	@Test
	public void testGetObjectsOfType() {
		// . X .
		// . . .
		// . . .
		// X X X
		World world = facade.createWorld(3.0, 4.0, new boolean[][] {
				{ true, false, true },
				{ true, true, true },
				{ true, true, true },
				{ false, false, false }
		}, random);

		facade.createWorm(world, 1.5, 1.5, 0, 1, "Test");
		facade.createFood(world, 2.0, 2.0);
		assertEquals(world.getObjectsOfType(Food.class).size(), 1);
		assertEquals(world.getObjectsOfType(Worm.class).size(), 1);
	}


	/**
	 * Test method for {@link worms.model.world.World#getWorms()}.
	 * Add 2 Worm objects and confirm that getWorms() returns 2.
	 */
	@Test
	public void testGetWorms() {
		// . X .
		// . . .
		// . . .
		// X X X
		World world = facade.createWorld(3.0, 4.0, new boolean[][] {
				{ true, false, true },
				{ true, true, true },
				{ true, true, true },
				{ false, false, false }
		}, random);

		facade.createWorm(world, 1.5, 1.5, 0, 1, "Test");
		facade.createWorm(world, 2.0, 2.0, 0, 1, "Test");
		assertEquals(world.getWorms().size(), 2);
	}

	/**
	 * Test method for {@link worms.model.world.World#getFood()}.
	 * Create 2 Food Objects and see if getFood() returns the amount added.
	 */
	@Test
	public void testGetFood() {
		// . X .
		// . . .
		// . . .
		// X X X
		World world = facade.createWorld(3.0, 4.0, new boolean[][] {
				{ true, false, true },
				{ true, true, true },
				{ true, true, true },
				{ false, false, false }
		}, random);

		facade.createFood(world, 1.5, 1.5);
		facade.createFood(world, 2.0, 2.0);
		assertEquals(world.getFood().size(), 2);
	}

}
