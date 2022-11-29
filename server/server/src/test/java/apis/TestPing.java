package apis;

import static org.junit.Assert.assertEquals;
import org.junit.*;
import ca.mcgill.splendorserver.apis.*;

public class TestPing {
	@Test
	public void Test() {
		PingController test = new PingController();
		assertEquals(test.ping(), "Ping");
	}
}
