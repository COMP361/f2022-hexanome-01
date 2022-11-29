package apis;

import static org.junit.Assert.assertEquals;
import org.junit.*;
import ca.mcgill.splendorserver.apis.*;

public class TestPing {
	@Test
	public void Test() {
		PingController test = new PingController();
		assertEquals(test.ping(), "Ping");
		assertEquals(test.pingPolling(), false);
		test.elapsedTime = 1;
		assertEquals(test.pingPolling(), false);
		test.startTime = -2*60*1000 - 1;
		assertEquals(test.pingPolling(), true);
	}
}
