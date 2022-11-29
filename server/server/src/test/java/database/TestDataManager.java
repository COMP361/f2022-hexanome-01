package Database;
import static org.junit.Assert.assertEquals;
import org.junit.*;
import ca.mcgill.splendorserver.Database.*;
import ca.mcgill.splendorserver.models.SessionData;


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
