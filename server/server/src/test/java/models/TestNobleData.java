package models;

import static org.junit.Assert.assertEquals;

import org.junit.*;

import ca.mcgill.splendorserver.models.*;

public class TestNobleData {
	@Test
	public void testMethods() {
		NobleData test = new NobleData();
		test.setBlue(1);
		test.setBrown(1);
		test.setGreen(1);
		test.setId(1);
		test.setPoints(5);
		test.setRed(1);
		test.setWhite(1);
		
		assertEquals(test.getBlue(), 1);
		assertEquals(test.getBrown(), 1);
		assertEquals(test.getGreen(), 1);
		assertEquals(test.getId(), 1);
		assertEquals(test.getPoints(), 5);
		assertEquals(test.getRed(), 1);
		assertEquals(test.getWhite(), 1);
	}
}
