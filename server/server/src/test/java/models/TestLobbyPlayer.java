package models;

import static org.junit.Assert.assertEquals;

import org.junit.*;

import ca.mcgill.splendorserver.models.*;

public class TestLobbyPlayer {
	@Test
	public void testMethods() {
		LobbyPlayer test = new LobbyPlayer(); //other methods need to be added as necessary
		test.setAccess_token("access");
		test.setExpires_in("expires");
		test.setRefresh_token("refresh");
		test.setUsername("Steven");
		
		assertEquals(test.getAccess_token(), "access");
		assertEquals(test.getExpires_in(), "expires");
		assertEquals(test.getRefresh_token(), "refresh");
		assertEquals(test.getUsername(), "Steven");
	}
}
