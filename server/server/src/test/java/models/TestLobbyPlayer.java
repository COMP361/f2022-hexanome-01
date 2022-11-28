package models;

import static org.junit.Assert.assertEquals;

import org.junit.*;

import ca.mcgill.splendorserver.models.*;

public class TestLobbyPlayer {
	@Test
	public void testMethods() {
		LobbyPlayer test = new LobbyPlayer(); //other methods need to be added as necessary
		test.setToken("12345");
		test.setUsername("Steven");
		
		assertEquals(test.getToken(), "12345");
		assertEquals(test.getUsername(), "Steven");
	}
}
