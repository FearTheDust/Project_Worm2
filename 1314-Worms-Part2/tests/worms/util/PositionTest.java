package worms.util;

import static org.junit.Assert.*;
import org.junit.Test;
import worms.util.Position;

/**
 * @author Coosemans Brent
 * @author Derkinderen Vincent
 */
public class PositionTest {
	
	/**
	 * Test the constructor with an illegal X (NaN) coordinate.
	 * {@link worms.util.Position#Position(double, double)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testPosition_IllegalX() {
		@SuppressWarnings("unused")
		Position position = new Position(Double.NaN, 1);
	}
	
	/**
	 * Test the constructor with an illegal Y (NaN) coordinate.
	 * {@link worms.util.Position#Position(double, double)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testPosition_IllegalY() {
		@SuppressWarnings("unused")
		Position position = new Position(1, Double.NaN);
	}
	
	/**
	 * Test the constructor with a legal coordinate (1,1)
	 *  {@link worms.util.Position#Position(double, double)}
	 */
	@Test
	public void testPosition_Legal() {
		Position position = new Position(1,1);
		assertEquals(position.getX(), 1, 0);
		assertEquals(position.getY(), 1, 0);
	}

	/**
	 * Test the method equals with an equaling and not equaling object.
	 * {@link worms.util.Position#equals(java.lang.Object)}
	 */
	@Test
	public void testEqualsObject() {
		//Same values
		Position firstPosition00 = new Position(0.1568984989189189156,0);
		Position secondPosition00 = new Position(0.1568984989189189158,0); //value, < epsilon difference
		//Different value
		Position firstPosition10 = new Position(1,0);
		
		/* Tests */
		assertTrue(firstPosition00.equals(secondPosition00)); //Same instance, same values
		assertFalse(firstPosition00.equals(firstPosition10)); // same instance, different values
		assertFalse(firstPosition00.equals(null)); // null
		assertFalse(firstPosition00.equals(this)); //other instance
	}
}
