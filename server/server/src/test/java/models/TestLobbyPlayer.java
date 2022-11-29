package models;

import static org.junit.Assert.assertEquals;

import org.junit.*;

import ca.mcgill.splendorserver.models.*;

public class TestLobbyPlayer {
	@Test
	public void testMethods() {
		LobbyPlayer test = new LobbyPlayer(); //other methods need to be added as necessary
		test.setAccessToken("access");
		test.setExpiresIn("expires");
		test.setRefreshToken("refresh");
		test.setUsername("Steven");
		
		assertEquals(test.getAccessToken(), "access");
		assertEquals(test.getExpiresIn(), "expires");
		assertEquals(test.getRefreshToken(), "refresh");
		assertEquals(test.getUsername(), "Steven");
	}
}
