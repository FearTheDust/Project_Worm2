package worms.model;


import static org.junit.Assert.*;
import org.junit.Test;
import worms.util.Position;

/**
 * 
 * @author Coosemans Brent
 * @author Derkinderen Vincent
 */
public class WormTest {
	
	/*
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}
	*/	
	
	/**
	 * Test Worm with a given amount of action points
	 */
	@Test
	public void testWorm_PositionAngleRadiusNameActionpoints() {
		Worm worm = new Worm(new Position(-1,2), 3, 4, "Testworm", 5);
		assertEquals(worm.getPosition(),new Position(-1,2));
		assertEquals(worm.getAngle(),3,0);
		assertEquals(worm.getRadius(),4,0);
		assertEquals(worm.getName(),"Testworm");
		assertEquals(worm.getCurrentActionPoints(),5);	
	}

	/**
	 * Test Worm with default action points
	 */
	@Test
	public void testWormPositionAngleRadiusName() {
		Worm worm = new Worm(new Position(-1,2), 3, 4, "Testworm");
		assertEquals(worm.getPosition(),new Position(-1,2));
		assertEquals(worm.getAngle(),3,0);
		assertEquals(worm.getRadius(),4,0);
		assertEquals(worm.getName(),"Testworm");
		assertEquals(worm.getCurrentActionPoints(), worm.getMaximumActionPoints());
	}

	/**
	 * Test jump with a legal angle
	 */
	@Test
	public void testJump_Legal() {
		Worm worm = new Worm(new Position(0,0), Math.PI/4, 1, "Test Jump Legal", 1);
		Position oldJump=worm.jumpStep(worm.jumpTime());
		worm.jump();
		assertEquals(worm.getPosition(),oldJump); //jumpStep and jumpTime already tested.
		assertEquals(worm.getCurrentActionPoints(),0);
	}
	
	/**
	 * Test jump with a legal angle to the right
	 */
	@Test
	public void testJump_LegalRight() {
		Worm worm = new Worm(new Position(0,0), 0, 1, "Test Jump Right", 1);
		Position afterJump=worm.jumpStep(worm.jumpTime());
		worm.jump();
		assertEquals(worm.getPosition(),afterJump);
		assertEquals(worm.getCurrentActionPoints(),0);
	}
	
	/**
	 * Test jump with a legal angle to the left
	 */
	@Test
	public void testJump_LegalLeft() {
		Worm worm = new Worm(new Position(0,0), Math.PI, 1, "Test Jump Left", 1);
		Position afterJump=worm.jumpStep(worm.jumpTime());
		worm.jump();
		assertEquals(worm.getPosition(),afterJump);
		assertEquals(worm.getCurrentActionPoints(),0);
	}
	
	/**
	 * Test jump with an illegal angle
	 */
	@Test
	public void testJump_Illegal() {
		Worm worm = new Worm(new Position(0,0), 3*Math.PI/2, 1, "Test Jump Illegal", 1);
		Position oldPosition=worm.getPosition();
		worm.jump();
		assertEquals(worm.getPosition(),oldPosition);
		assertEquals(worm.getCurrentActionPoints(),1);
	}

	/**
	 * Test jumpStep with a legal time
	 */
	@Test
	public void testJumpStep_Legal() {
		Worm worm = new Worm(new Position(0,0), Math.PI/4, 1, "Test JumpStep Legal", 1);
		Position position = worm.jumpStep(worm.jumpTime());
		assertEquals(position.getX(), 2.45222451, 1E-3);
		assertEquals(position.getY(), 0, 1E-4);
	}
	
	/**
	 * Test jumpStep with the time too high
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testJumpStep_TooHigh() {
		Worm worm = new Worm(new Position(0,0), Math.PI/4, 1, "Test JumpStep Too High", 1);				
		worm.jumpStep(worm.jumpTime()+10);
	}
	
	/**
	 * Test jumpStep with the time too low
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testJumpStep_TooLow() {
		Worm worm = new Worm(new Position(0,0), Math.PI/4, 1, "Test JumpStep Too Low", 1);	
		worm.jumpStep(-1);
	}
	
	/**
	 * Test jumpTime with a legal angle
	 */
	@Test
	public void testJumpTime_Legal() {
		Worm worm = new Worm(new Position(0,0), Math.PI/2, 1, "Test JumpTime", 1);
		assertEquals(worm.jumpTime(), 1.00011, 1E-5);
	}

	/**
	 * Test jumpTime with an angle too high to jump
	 */
	@Test
	public void testJumpTime_Zero() {
		Worm worm = new Worm(new Position(0,0), 3*Math.PI/2, 1, "Test JumpTime Zero", 1);
		assertEquals(worm.jumpTime(), 0, 0);
	}
	
	/**
	 * Test Move(horizontally) with a legal amounts of steps and sufficient amount of action points
	 */
	@Test
	public void testMove_Horizontal() {
		Worm worm = new Worm(new Position(0,0), 0, 1, "Test Move Horizontal");
		int oldAP=worm.getCurrentActionPoints();
		worm.move(5);
		assertEquals(worm.getPosition(), new Position(5,0));
		assertEquals(worm.getCurrentActionPoints(),oldAP-5);
	}

	/**
	 * Test Move(vertically) with a legal amounts of steps and sufficient amount of action points
	 */
	@Test
	public void testMove_Vertical() {
		Worm worm = new Worm(new Position(0,0), Math.PI / 2,  1, "Test Move Vertical");
		int oldAP = worm.getCurrentActionPoints();
		worm.move(5);
		assertEquals(worm.getPosition(), new Position(0,5));
		assertEquals(worm.getCurrentActionPoints(), oldAP-20);
	}
	
	/**
	 * Test Move with an illegal amount of steps
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testMove_IllegalSteps() {
		Worm worm = new Worm(new Position(0,0), 0, 1, "Test Move Illegal Steps");
		worm.move(-2);
	}
	
	/**
	 * Test move with insufficient amount of action points
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testMove_IllegalActionPoints() {
		Worm worm = new Worm(new Position(0,0), 0, 1, "Test Move Illegal AP", 1);
		worm.move(10);
	}

	/**
	 * Test getMoveCost
	 */
	@Test
	public void testGetMoveCost() {
		assertEquals(Worm.getMoveCost(2, Math.PI/4), 8);
	}

	/**
	 * Test turn with a negative angle
	 */
	@Test
	public void testTurn_Negative() {
		Worm worm = new Worm(new Position(0,0), 3.0/2.0 * Math.PI, 1, "Test Turn Negative", 16);
		worm.turn(-Math.PI/2);
		assertEquals(worm.getAngle(),Math.PI,1E-9);
		assertEquals(worm.getCurrentActionPoints(),1);
	}
	/**
	 * Test turn with the angle = 0
	 */
	@Test
	public void testTurn_Zero() {
		Worm worm = new Worm(new Position(0,0), 3.0/2.0 * Math.PI, 1, "Test Turn Zero", 16);
		worm.turn(0);
		assertEquals(worm.getAngle(),3.0/2.0 * Math.PI,1E-9);
		assertEquals(worm.getCurrentActionPoints(),16);
	}
	
	/**
	 * Test turn with a positive angle
	 */
	@Test
	public void testTurn_Positive() {
		Worm worm = new Worm(new Position(0,0), 3 * Math.PI / 2, 1, "Test Turn Positive", 16);
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
		Worm worm = new Worm(new Position(0,0), 0, 2, "Test Radius Legal");
		worm.setRadius(1);
		assertEquals(worm.getRadius(), 1, 0);
	}
	
	/**
	 * Test setRadius with a radius lower than the minimum radius
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testSetRadius_Illegal() {
		Worm worm = new Worm(new Position(0,0), 0, 2, "Test Radius Illegal");
		worm.setRadius(worm.getMinimumRadius()-0.1);
	}
	
	/**
	 * Test setRadius with radius = NaN
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testSetRadius_NaN() {
		Worm worm = new Worm(new Position(0,0), 0, 2, "Test Radius NaN");
		worm.setRadius(Double.NaN);
	}

	/**
	 * Test getMass
	 */
	@Test
	public void testGetMass() {
		Worm worm = new Worm(new Position(0,0), 0, 1, "Test Mass");
		assertEquals(worm.getMass(), 4448.495, 1E-3);
	}

	/**
	 * Test setName with a legal name
	 */
	@Test
	public void testSetName_Legal() {
		Worm worm = new Worm(new Position(0,0), 0, 1, "Test Name Legal");
		
		worm.setName("Eric");
		assertEquals(worm.getName(), "Eric");
	}
	
	/**
	 * Test setName with an illegal name
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testSetName_Illegal() {
		Worm worm = new Worm(new Position(0,0), 0, 1, "Test Name Illegal");
		worm.setName("eric");
	}

	/**
	 * Test isValidName with legal names
	 */
	@Test
	public void testIsValidName_Legal() {
		assert(Worm.isValidName("James o'Hara"));
		assert(Worm.isValidName("James o\"Hara"));
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
		assertFalse(Worm.isValidName("James 0 Hara"));
		assertFalse(Worm.isValidName("James \n Hara"));
		assertFalse(Worm.isValidName("N"));
		assertFalse(Worm.isValidName(null));
	}
	
	/**
	 * Test getMaximumActionPoints with the radius too high resulting in the MaxActionPoints being bigger than Integer.MAX_VALUE
	 */
	@Test
	public void testGetMaximumActionPoints_HighestMaximum() {
		Worm worm = new Worm(new Position(0,0), 0, Double.MAX_VALUE, "Test Max AP Max Value");
		assertEquals(worm.getMaximumActionPoints(), Integer.MAX_VALUE);
	}
	
	/**
	 * Test getMaximumActionPoints with legal action points
	 */
	@Test
	public void testGetMaximumActionPoints_Legal() {
		Worm worm = new Worm(new Position(0,0), 0, 1, "Test Max AP Legal");
		assertEquals(worm.getMaximumActionPoints(), 4448);
	}

}
