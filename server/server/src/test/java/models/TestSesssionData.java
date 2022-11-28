package models;

import static org.junit.Assert.assertEquals;

import org.junit.*;

import ca.mcgill.splendorserver.models.*;

public class TestSesssionData {
	@Test
	public void testMethods() {
		SessionData test = new SessionData();
		test.setMaxPlayers(4);
		test.setSessionName("Splendor Fun");
		LobbyPlayer[] temp = {new LobbyPlayer()};
		test.setPlayerList(temp);
		
		assertEquals(test.getMaxPlayers(), 4);
		assertEquals(test.getSessionName(), "Splendor Fun");
		for(int i = 0;i < temp.length;i++)
			assertEquals(test.getPlayerList()[i], temp[i]);
	}
}
