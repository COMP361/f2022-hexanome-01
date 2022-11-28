package models;


import static org.junit.Assert.assertEquals;

import org.junit.*;

import ca.mcgill.splendorserver.models.*;

public class TestPLayerData {
	@Test
	public void testMethods() {
		PlayerData test = new PlayerData(); //other methods need to be added as necessary
		test.setToken("12345");
		test.setUsername("Steven");
		int[] temp = {1, 2, 3, 4, 5};
		test.setDiscounts(temp);
		NobleData[] temp1 = {new NobleData()};
		CardData[] temp2 = {new CardData()};
		CardData[] temp3 = {new CardData()};
		test.setNobles(temp1);
		test.setInventory(temp2);
		test.setReserved(temp3);
		
		assertEquals(test.getToken(), "12345");
		assertEquals(test.getUsername(), "Steven");
		for(int i = 0; i < temp.length;i++)
			assertEquals(test.getDiscounts()[i], temp[i]);
		for(int i = 0; i < temp1.length;i++)
			assertEquals(test.getNobles()[i], temp1[i]);
		for(int i = 0; i < temp2.length;i++)
			assertEquals(test.getInventory()[i], temp2[i]);
		for(int i = 0; i < temp3.length;i++)
			assertEquals(test.getReserved()[i], temp3[i]);
	}
}
