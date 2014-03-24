/**
 * 
 */
package worms.util;

import static org.junit.Assert.*;

import org.junit.Test;

import worms.util.Util;

/**
 * @author Derkinderen Vincent
 * @author Coosemans Brent
 *
 */
public class UtilTest {

	/**
	 * Test method for {@link worms.util.Util#modulo(double, double)}.
	 * For exceptional "not allowed" cases.
	 */
	@Test
	public void testModulo_IllegalCases() {
		assertTrue(Double.isNaN(Util.modulo(Double.NaN, 1)));
		assertTrue(Double.isNaN(Util.modulo(1, Double.NaN)));
		assertTrue(Double.isNaN(Util.modulo(Double.POSITIVE_INFINITY, 1)));
		
		assertTrue(Util.modulo(1, Double.POSITIVE_INFINITY) == 0);
		assertTrue(Util.modulo(5, 0) == 5);
		assertTrue(Util.modulo(5, 1) == 5);
	}
	
	/**
	 * Test method for {@link worms.util.Util#modulo(double, double)}.
	 * For a few "legal cases".
	 */
	@Test
	public void testModulo_LegalCase() {
		assertEquals(Util.modulo(2*Math.PI, 2*Math.PI), 0, 1E-9);
		assertEquals(Util.modulo(Math.PI, 2*Math.PI), Math.PI, 1E-9);
		assertEquals(Util.modulo(2*Math.PI + 3.0/4 * Math.PI, 2*Math.PI), 3.0/4 * Math.PI, 1E-9);
	}

}
