package apis;

import static org.junit.Assert.assertEquals;

import ca.mcgill.splendorserver.apis.SessionController;
import ca.mcgill.splendorserver.models.SessionData;
import org.junit.Test;

/**
 * session controller test.
 */

public class TestSessionController {
  @Test
  public void test() {
    SessionController test = new SessionController();
    SessionData temp = new SessionData();
    temp.setMaxPlayers(4);
    temp.setSessionName("test");

    int max = test.receiveSession(temp);

    assertEquals(max, 4);
    assertEquals(test.getSession("test"), temp);
    assertEquals(test.getSession(null), null);
    Object[] temp1 = {temp};
    assertEquals(test.getSessions(), temp1);
  }
}
