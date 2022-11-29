package database;

import static org.junit.Assert.assertEquals;

import ca.mcgill.splendorserver.database.DataManager;
import ca.mcgill.splendorserver.models.SessionData;
import org.junit.Test;


public class TestDataManager {
	@Test
	public void TestDataManager() {
		DataManager test = new DataManager();
		SessionData temp = new SessionData();
		temp.setSessionName("test");
		test.addSession(temp);
		
		assertEquals(test.getSession("test"), temp);
	}
}
