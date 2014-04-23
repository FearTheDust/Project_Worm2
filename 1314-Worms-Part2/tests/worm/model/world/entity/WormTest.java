/**
 * 
 */
package worm.model.world.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import worms.gui.GUIConstants;
import worms.model.Facade;
import worms.model.IFacade;
import worms.model.Team;
import worms.model.equipment.weapons.BrentsWeaponOfDoom;
import worms.model.equipment.weapons.Weapon;
import worms.model.world.World;
import worms.model.world.entity.Worm;
import worms.util.Position;
import worms.util.Util;

/**
 * @author Coosemans Brent
 * @author Derkinderen Vincent
 *
 */

/**
 * Test method for {@link worms.model.world.entity.Worm#setName(java.lang.String)}.
 */
public class WormTest {
	
	private static final double EPS = Util.DEFAULT_EPSILON;
	
	private IFacade facade;

	private Random random;

	private World world;

	// X X X X
	// . . . .
	// . . . .
	// . . . .
	// X X X X
	private boolean[][] passableMap = new boolean[][] {
			{ false, false, false, false },
			{ true, true, true, true },			
			{ true, true, true, true },
			{ true, true, true, true }, 
			{ false, false, false, false } };

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		facade = new Facade();
		random = new Random(7357);
		world = new World(4.0, 4.0, passableMap, random);
	}

	/**
	 * Test method for {@link worms.model.world.entity.Worm#isAlive()}.
	 * Tests whether a worm is alive when created with 5 HP.
	 * Tests whether a worm isn't alive when less than 1 HP.
	 * Tests whether a worm isn't alive after moving out of game boundaries.
	 */
	@Test
	public void testIsAlive() {
		Worm worm = new Worm(world, new Position(1,2), 0, 1, "Myname", 20, 5);
		assertTrue(worm.isAlive());
		worm.inflictHitDamage(6);
		assertFalse(worm.isAlive());
		
		Worm worm2 = new Worm(world, new Position(1,2), Math.PI, 1, "Myname", 20, 5);
		worm2.move();
		assertFalse(worm2.isAlive());
	}

	/**
	 * Test method for {@link worms.model.world.entity.Worm#canFall()}.
	 * Tested in PartialFacadeTest
	 */
	@Test
	public void testCanFall() {
	}

	/**
	 * Test method for {@link worms.model.world.entity.Worm#fall()}.
	 * Tested in PartialFacadeTest
	 */
	@Test
	public void testFall() {
		// . X .
		// . . .
		// . w .
		// . . .
		// . . .
		// X X X
		World world = facade.createWorld(3.0, 6.0, new boolean[][] {
				{ true, false, true },
				{ true, true, true },
				{ true, true, true },
				{ true, true, true },
				{ true, true, true },
				{ false, false, false }
				
				}, random);
		Worm worm = facade.createWorm(world, 1.5, 4, 3*Math.PI / 2, 0.5,
				"Test");
		int previousHitPoints = worm.getCurrentHitPoints();
		facade.fall(worm);
		assertEquals(1.5, facade.getX(worm), EPS);
		assertTrue(Util.fuzzyLessThanOrEqualTo(1.5, facade.getY(worm), EPS) && Util.fuzzyGreaterThanOrEqualTo(1.55, facade.getY(worm), EPS));
		assertEquals(worm.getCurrentHitPoints(),previousHitPoints-6);
	}
	
	/**
	 * Test method for {@link worms.model.world.entity.Worm#softFall()}.
	 * Test a soft fall on a worm. The worm falls more than 1 meter and loses no HP.
	 */
	@Test
	public void testSoftFall() {
		// . X .
		// . . .
		// . w .
		// . . .
		// . . .
		// X X X
		World world = facade.createWorld(3.0, 6.0, new boolean[][] {
				{ true, false, true },
				{ true, true, true },
				{ true, true, true },
				{ true, true, true },
				{ true, true, true },
				{ false, false, false }
				
				}, random);
		Worm worm = facade.createWorm(world, 1.5, 4, 3*Math.PI / 2, 0.5,
				"Test");
		int previousHitPoints = worm.getCurrentHitPoints();
		worm.softFall();
		assertEquals(1.5, facade.getX(worm), EPS);
		assertTrue(Util.fuzzyLessThanOrEqualTo(1.5, facade.getY(worm), EPS) && Util.fuzzyGreaterThanOrEqualTo(1.55, facade.getY(worm), EPS));
		assertEquals(worm.getCurrentHitPoints(),previousHitPoints);
	}
	
	/**
	 * Test method for {@link worms.model.world.entity.Worm#jump(double)}.
	 * Test if when jumped with 0 AP our position remains.
	 */
	@Test
	public void testJump_Illegal() {
		Worm worm = new Worm(world, new Position(1,2), Math.PI/4, 1, "Test Jump Illegal", 0, 10);
		Position position = worm.getPosition();
		worm.jump(2);
		assertEquals(worm.getPosition().getX(), position.getX(), 0);
		assertEquals(worm.getPosition().getY(), position.getY(), 0);
	}

	/**
	 * Test method for {@link worms.model.world.entity.Worm#jumpStep(double)}.
	 * Test jumpStep with a legal time
	 */
	@Test
	public void testJumpStep_Legal() {
		Worm worm = new Worm(world, new Position(1,2), Math.PI/4, 1, "Test JumpStep Legal", 1, 10);
		Position position = worm.jumpStep(2);
		assertEquals(7.9351, position.getX(), 1E-4);
		assertEquals(-10.6781, position.getY(), 1E-4);
	}
	
	/**
	 * Test method for {@link worms.model.world.entity.Worm#jumpStep(double)}.
	 * Test whether the jumpStep with time = 0 returns the same position.
	 */
	@Test
	public void testJumpStep_Zero() {
		Worm worm = new Worm(world, new Position(1,2), Math.PI/4, 1, "Test JumpStep Zero", 1, 10);
		Position position = worm.jumpStep(0);
		assertEquals(position, worm.getPosition());
	}
	
	/**
	 * Test method for {@link worms.model.world.entity.Worm#jumpStep(double)}.
	 * Test jumpStep with the time negative.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testJumpStep_NegativeTime() {
		Worm worm = new Worm(world, new Position(1,2), Math.PI/4, 1, "Test JumpStep Too Low", 1, 10);	
		worm.jumpStep(-1);
	}

	/**
	 * Test method for {@link worms.model.world.entity.Worm#jumpTime(double)}.
	 * Test whether the jumpTime for a worm facing the wall is equal to 0.
	 */
	@Test
	public void testJumpTime() {
		// . . x
		// . . x
		// x x x
		World world = facade.createWorld(3.0, 3.0, new boolean[][] {
				{ true, true, false }, 
				{ true, true, false },
				{ false, false, false}
				}, random);
		
		Worm worm = new Worm(world, new Position(2.45,2), 0, 0.5, "TestWorm");
		assertEquals(worm.jumpTime(GUIConstants.JUMP_TIME_STEP), 0, 0);
	}

	/**
	 * Test method for {@link worms.model.world.entity.Worm#getMoveCost(worms.util.Position)}.
	 * Test if it does cost 1 to move 0.5 over a straight line.
	 */
	@Test
	public void testGetMoveCost() {
		Worm worm = new Worm(world, new Position(1,2), 0, 0.5, "TestWorm");
		assertEquals(worm.getMoveCost(new Position(1.5, 2.0)), 1);
	}
	
	/**
	 * Test setName with a legal name
	 */
	@Test
	public void testSetName_Legal() {
		Worm worm = new Worm(world, new Position(1,2), 0, 1, "Test Name Legal");
		
		worm.setName("Eric");
		assertEquals(worm.getName(), "Eric");
	}
	
	/**
	 * Test setName with an illegal name
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testSetName_Illegal() {
		Worm worm = new Worm(world, new Position(1,2), 0, 1, "Test Name Illegal");
		worm.setName("eric");
	}

	/**
	 * Test isValidName with legal names
	 */
	@Test
	public void testIsValidName_Legal() {
		assertTrue(Worm.isValidName("James o'Hara"));
		assertTrue(Worm.isValidName("James o\"Hara"));
		assertTrue(Worm.isValidName("James 007"));
		assertTrue(Worm.isValidName("James 0 Hara"));
	}

	/**
	 * Test isValidName with illegal names
	 * First letter lower
	 * Non alphabetic letter (excluding space, ' and ")
	 * Illegal character \n 
	 * length < 2
	 * null
	 */
	@Test
	public void testIsValidName_Illegal() {
		assertFalse(Worm.isValidName("james o'Hara"));
		assertFalse(Worm.isValidName("James \n Hara"));
		assertFalse(Worm.isValidName("N"));
		assertFalse(Worm.isValidName(null));
	}


	/**
	 * Test method for {@link worms.model.world.entity.Worm#inflictHitDamage(int)}.
	 * Test whether a worm created with 10 HP has 10 HP.
	 * Test whether the worm inflicted 3 HP has 7 HP left.
	 * Test whether the worm inflicted 8 more HP has 0 HP left.
	 * Test whether the worm inflicted 10 more HP on top of that 0 HP still has 0 HP.
	 */
	@Test
	public void testInflictHitDamage() {
		Worm worm = new Worm(world, new Position(1,2), 0, 1, "Test", 20, 10);
		assertEquals(worm.getCurrentHitPoints(), 10);
		worm.inflictHitDamage(3);
		assertEquals(worm.getCurrentHitPoints(), 7);
		worm.inflictHitDamage(8);
		assertEquals(worm.getCurrentHitPoints(), 0);
		worm.inflictHitDamage(10);
		assertEquals(worm.getCurrentHitPoints(), 0);
	}

	/**
	 * Test getMaximumActionPoints with the radius too high resulting in the MaxActionPoints being bigger than Integer.MAX_VALUE
	 * Test cancelled due radius impossible within a world.
	 */
	/*@Test
	public void testGetMaximumActionPoints_HighestMaximum() {
		Worm worm = new Worm(world, new Position(1,2), 0, Double.MAX_VALUE, "Test Max AP Max Value");
		assertEquals(worm.getMaximumActionPoints(), Integer.MAX_VALUE);
	}*/
	
	/**
	 * Test getMaximumActionPoints with legal action points
	 */
	@Test
	public void testGetMaximumActionPoints_Legal() {
		Worm worm = new Worm(world, new Position(1,2), 0, 1, "Test Max AP Legal");
		assertEquals(worm.getMaximumActionPoints(), 4448);
	}

	/**
	 * Test method for {@link worms.model.world.entity.Worm#setTeam(worms.model.Team)}.
	 * Tests whether trying to set the team of a worm to a team while the worm isn't in that team yet creates an error.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testSetTeam_Illegal() {
		Worm worm = new Worm(world, new Position(1,2), 0, 1, "Test", 20, 10);
		Team team = new Team("TeamAwesomeness");
		
		worm.setTeam(team);
	}
	
	/**
	 * Test method for {@link worms.model.world.entity.Worm#setTeam(worms.model.Team)}.
	 * Test trying to add this worm to a team.
	 */
	@Test
	public void testSetTeam_Legal() {
		Worm worm = new Worm(world, new Position(1,2), 0, 1, "Test", 20, 10);
		Team team = new Team("TeamAwesomeness");
		
		team.add(worm);
		assertTrue(team.isMember(worm));	
	}

	/**
	 * Test method for {@link worms.model.world.entity.Worm#setCurrentWeapon(worms.model.equipment.weapons.Weapon)}.
	 * Test if after setting the worm's weapon to the next the getter returns that weapon.
	 */
	@Test
	public void testSetCurrentWeapon() {
		Worm worm = new Worm(world, new Position(1,2), 0, 1, "Test", 20, 10);
		Weapon weapon = worm.getNextWeapon();
		worm.setCurrentWeapon(weapon);
		
		assertEquals(worm.getCurrentWeapon(), weapon);
	}

	/**
	 * Test method for {@link worms.model.world.entity.Worm#add(worms.model.equipment.weapons.Weapon)}.
	 * Tests whether adding an already equipped weapon isn't added again.
	 */
	@Test
	public void testAdd() {
		Worm worm = new Worm(world, new Position(1,2), 0, 1, "Test", 20, 10);
		Weapon weapon = worm.getNextWeapon();
		int sizeBefore = worm.getWeaponList().size();
		worm.add(weapon);
		assertEquals(worm.getWeaponList().size(), sizeBefore);
	}

	/**
	 * Test method for {@link worms.model.world.entity.Worm#hasGot(worms.model.equipment.weapons.Weapon)}.
	 * Tests whether hasGot returns true for a type of weapon(class) the worm already has.
	 */
	@Test
	public void testHasGot() {
		Worm worm = new Worm(world, new Position(1,2), 0, 1, "Test", 20, 10);
		Weapon weapon = worm.getNextWeapon();
		assertTrue(worm.hasGot(weapon));
		
		Weapon brent = new BrentsWeaponOfDoom(worm);
		assertFalse(worm.hasGot(brent));
	}

	/**
	 * Test method for {@link worms.model.world.entity.Worm#giveTurnPoints()}.
	 * Tests whether a worm his AP changes to Max AP and his HP are increased by 10.
	 */
	@Test
	public void testGiveTurnPoints() {
		Worm worm = new Worm(world, new Position(1,2), 0, 1, "Test", 20, 10);
		worm.giveTurnPoints();
		assertEquals(worm.getCurrentActionPoints(),worm.getMaximumActionPoints());
		assertEquals(worm.getCurrentHitPoints(),20);
	}

	/**
	 * Test method for {@link worms.model.world.entity.Worm#getMovePosition()}.
	 * Used in move so tested in testMove.
	 */
	@Test
	public void testGetMovePosition() {
	}

	/**
	 * Test method for {@link worms.model.world.entity.Worm#move()}.
	 * Tested in PartialFacadeTest.
	 */
	@Test
	public void testMove() {
	}
	
	/**
	 * Test turn with a negative angle
	 */
	@Test
	public void testTurn_Negative() {
		Worm worm = new Worm(world, new Position(1,2), 3.0/2.0 * Math.PI, 1, "Test Turn Negative", 16, 10);
		worm.turn(-Math.PI/2);
		assertEquals(worm.getAngle(),Math.PI,1E-9);
		assertEquals(worm.getCurrentActionPoints(),1);
	}
	
	/**
	 * Test turn with the angle = 0
	 */
	@Test
	public void testTurn_Zero() {
		Worm worm = new Worm(world, new Position(1,2), 3.0/2.0 * Math.PI, 1, "Test Turn Zero", 16, 10);
		worm.turn(0);
		assertEquals(worm.getAngle(),3.0/2.0 * Math.PI,1E-9);
		assertEquals(worm.getCurrentActionPoints(),16);
	}
	
	/**
	 * Test turn with a positive angle
	 */
	@Test
	public void testTurn_Positive() {
		Worm worm = new Worm(world, new Position(1,2), 3 * Math.PI / 2, 1, "Test Turn Positive", 16, 10);
		worm.turn(Math.PI/2);
		assertEquals(worm.getAngle(),0,1E-9);
		assertEquals(worm.getCurrentActionPoints(),1);
	}
	
	/**
	 * Test getTurnCost with a negative angle
	 */
	@Test
	public void testGetTurnCost_Negative() {
		assertEquals(Worm.getTurnCost(-Math.PI), 30);
	}
	
	/**
	 * Test getTurnCost with the angle = 0
	 */
	@Test
	public void testGetTurnCost_Zero() {
		assertEquals(Worm.getTurnCost(0), 0);
	}
		
	/**
	 * Test getTurnCost with a positive angle
	 */
	@Test
	public void testGetTurnCost_Positive() {
		assertEquals(Worm.getTurnCost(Math.PI), 30);
	}

	/**
	 * Test isValidAngle with legal angles
	 */
	@Test
	public void TestIsValidAngle_Legal(){
		assert(Worm.isValidAngle(Math.PI));
		assert(Worm.isValidAngle(0));
	}
	
	/**
	 * Test isValidAngle with illegal angles
	 */
	@Test
	public void TestIsValidAngle_Illegal(){
		assertFalse(Worm.isValidAngle(2*Math.PI));
		assertFalse(Worm.isValidAngle(-1));
	}
	
	/**
	 * Test setRadius with a legal radius
	 */
	@Test
	public void testSetRadius_Legal() {
		Worm worm = new Worm(world, new Position(2,2), 0, 2, "Test Radius Legal");
		worm.setRadius(1);
		assertEquals(worm.getRadius(), 1, 0);
	}
	
	/**
	 * Test setRadius with a radius lower than the minimum radius
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testSetRadius_Illegal() {
		Worm worm = new Worm(world, new Position(1,2), 0, 1, "Test Radius Illegal");
		worm.setRadius(worm.getMinimumRadius()-0.1);
	}
	
	/**
	 * Test setRadius with radius = NaN
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testSetRadius_NaN() {
		Worm worm = new Worm(world, new Position(1,2), 0, 1, "Test Radius NaN");
		worm.setRadius(Double.NaN);
	}

	/**
	 * Test getMass
	 */
	@Test
	public void testGetMass() {
		Worm worm = new Worm(world, new Position(1,2), 0, 1, "Test Mass");
		assertEquals(worm.getMass(), 4448.495, 1E-3);
	}
	
	/**
	 * Test Worm with a given amount of action points and hit points.
	 */
	@Test
	public void testWorm_PositionAngleRadiusNameActionpointsHitpoints() {
		Worm worm = new Worm(world, new Position(1.5,2), 3, 1.5, "Testworm", 5, 10);
		assertEquals(worm.getPosition(),new Position(1.5,2));
		assertEquals(worm.getAngle(),3,0);
		assertEquals(worm.getRadius(),1.5,0);
		assertEquals(worm.getName(),"Testworm");
		assertEquals(worm.getCurrentActionPoints(),5);
		assertEquals(worm.getCurrentHitPoints(), 10);
	}

	/**
	 * Test Worm with default action points & hit points.
	 */
	@Test
	public void testWormPositionAngleRadiusName() {
		Worm worm = new Worm(world, new Position(1.56,2), 3, 1.56, "Testworm2");
		assertEquals(worm.getPosition(),new Position(1.56,2));
		assertEquals(worm.getAngle(),3,0);
		assertEquals(worm.getRadius(),1.56, 0);
		assertEquals(worm.getName(),"Testworm2");
		assertEquals(worm.getCurrentActionPoints(), worm.getMaximumActionPoints());
		assertEquals(worm.getCurrentHitPoints(), worm.getMaximumHitPoints());
	}

}
