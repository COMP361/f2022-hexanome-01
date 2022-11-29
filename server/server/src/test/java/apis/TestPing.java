package apis;

import static org.junit.Assert.assertEquals;

import ca.mcgill.splendorserver.apis.PingController;
import org.junit.Test;

/**
 * ping test.
 */

public class TestPing {
  @Test
  public void test() {
    PingController test = new PingController();
    assertEquals(test.ping(), "Ping");
    
    assertEquals(test.pingPolling(), false);
    test.elapsedTime = 1;
    assertEquals(test.pingPolling(), false);
    test.startTime = -2 * 60 * 1000 + 1;
    assertEquals(test.pingPolling(), true);
  }
}
