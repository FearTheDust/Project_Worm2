package worms.model;

import static org.junit.Assert.*;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import worms.model.world.World;
import worms.model.world.entity.Worm;
import worms.util.Util;

//TODO: Tests old version of this Test class located in project part 1, copy & paste here??
public class PartialFacadeTest {

	private static final double EPS = Util.DEFAULT_EPSILON;

	private IFacade facade;

	private Random random;

	private World world;

	// X X X X
	// . . . .
	// . . . .
	// X X X X
	private boolean[][] passableMap = new boolean[][] {
			{ false, false, false, false }, 
			{ true, true, true, true },
			{ true, true, true, true }, 
			{ false, false, false, false } };

	@Before
	public void setup() {
		facade = new Facade();
		random = new Random(7357);
		world = new World(4.0, 4.0, passableMap, random);
	}

	@Test
	public void testMaximumActionPoints() {
		Worm worm = facade.createWorm(world, 1, 2, 0, 1, "Test");
		assertEquals(4448, facade.getMaxActionPoints(worm));
	}

	@Test
	public void testMoveHorizontal() {
		Worm worm = facade.createWorm(world, 1, 2, 0, 1, "Test");
		facade.move(worm);
		assertEquals(2, facade.getX(worm), EPS);
		assertEquals(2, facade.getY(worm), EPS);
	}

	@Test
	public void testMoveVertical() {
		// X X X X
		// . . . .
		// . w . .
		// X X X X
		Worm worm = facade.createWorm(world, 1, 1.5, Math.PI / 2, 0.5, "Test");
		facade.move(worm);
		assertEquals(1, facade.getX(worm), EPS);
		assertEquals(2.0, facade.getY(worm), EPS);
	}

	@Test
	public void testMoveVerticalAlongTerrain() {
		// . . X
		// . w X
		World world = facade.createWorld(3.0, 2.0, new boolean[][] {
				{ true, true, false }, 
				{ true, true, false } 
				}, random);
		Worm worm = facade.createWorm(world, 1.5, 0.5,
				Math.PI / 2 - 10 * 0.0175, 0.5, "Test");
		facade.move(worm); 
//TODO: de fout zit in isImpassable, hij beweegt volgens zijn initiele richting terwijl hij dat niet zou mogen doen
		double a=worm.getRadius() * Math.cos(worm.getAngle()) + worm.getPosition().getX();
		double b=worm.getRadius() * Math.sin(worm.getAngle()) + worm.getPosition().getY();
		System.out.println(a + ", " + b);
		System.out.println(facade.getX(worm)+ ", " + facade.getY(worm));
		assertEquals(1.5, facade.getX(worm), EPS);
		assertEquals(1.0, facade.getY(worm), EPS);
	}

	@Test
	public void testFall() {
		// . X .
		// . w .
		// . . .
		// X X X
		World world = facade.createWorld(3.0, 4.0, new boolean[][] {
				{ true, false, true },
				{ true, true, true },
				{ true, true, true },
				{ false, false, false }
				
				/*{ false, false, false },
				{ true, true, true },
				{ true, true, true },
				{ true, false, true }*/
				
				}, random);
		Worm worm = facade.createWorm(world, 1.5, 2.5, 3*Math.PI / 2, 0.5,
				"Test"); //changed angle - Math.PI / 2 -> 3*Math.PI / 2
		assertFalse(facade.canFall(worm));
		facade.move(worm);
		assertTrue(facade.canFall(worm));
		facade.fall(worm);
		assertEquals(1.5, facade.getX(worm), EPS);
		assertEquals(1.5, facade.getY(worm), EPS);
	}
}
