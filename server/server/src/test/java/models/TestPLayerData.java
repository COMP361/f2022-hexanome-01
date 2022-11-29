package models;


import static org.junit.Assert.assertEquals;

import org.junit.*;

import ca.mcgill.splendorserver.models.*;

public class TestPLayerData {
	@Test
	public void testMethods() {
		PlayerData test = new PlayerData("test"); //other methods need to be added as necessary
		test.setPoints(1);
		assertEquals(test.getId(), "test");
		assertEquals(test.getPoints(), 1);
		
	}
}
