package models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.*;

import ca.mcgill.splendorserver.models.*;

public class TestCardData {
	@Test
	public void testMethods() {
		CardData test = new CardData();
		test.setBlue(1);
		test.setBonus('B');
		test.setBonusAmount(2);
		test.setBrown(1);
		test.setGreen(1);
		test.setId(1);
		test.setPoints(5);
		test.setRed(1);
		test.setWhite(1);
		
		assertEquals(test.getBlue(), 1);
		assertEquals(test.getBonus(), 'B');
		assertEquals(test.getBonusAmount(), 2);
		assertEquals(test.getBrown(), 1);
		assertEquals(test.getGreen(), 1);
		assertEquals(test.getId(), 1);
		assertEquals(test.getPoints(), 5);
		assertEquals(test.getRed(), 1);
		assertEquals(test.getWhite(), 1);
	}
}
