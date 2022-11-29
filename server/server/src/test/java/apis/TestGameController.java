package apis;

import static org.junit.Assert.assertEquals;

import ca.mcgill.splendorserver.apis.GameController;
import ca.mcgill.splendorserver.models.GameData;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

/**
 * game controller test.
 */

public class TestGameController {
  @Test
  public void test() throws JsonProcessingException {
    GameController test = new GameController();
    GameData temp = new GameData();
    temp.setGameId("test");

    test.createGame(temp);

    assertEquals(test.getGame("test"), temp);
    assertEquals(test.lauchGame(temp.getGameId(), temp), temp.getGameId());
    assertEquals(test.getGame((new GameData()).getGameId()), null);
  }
}
