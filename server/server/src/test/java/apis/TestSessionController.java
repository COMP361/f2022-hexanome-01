package apis;
import static org.junit.Assert.*;
import org.junit.*;
import ca.mcgill.splendorserver.apis.*;
import ca.mcgill.splendorserver.models.*;

public class TestSessionController {
	@Test
	public void Test() {
		SessionController test = new SessionController();
		SessionData temp = new SessionData();
		temp.setMaxPlayers(4);
		temp.setSessionName("test");
		
		int max = test.receiveSession(temp);
		
		assertEquals(max, 4);
		assertEquals(test.getSession("test"), temp);
		assertEquals(test.getSession(null), null);
	}
}
