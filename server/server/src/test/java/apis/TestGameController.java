package apis;

import static org.junit.Assert.assertEquals;

import ca.mcgill.splendorserver.apis.GameController;
import ca.mcgill.splendorserver.models.CardData;
import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.GameConfigData;
import ca.mcgill.splendorserver.models.GameData;
import ca.mcgill.splendorserver.models.NobleData;
import ca.mcgill.splendorserver.models.TurnData;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

/**
 * game controller test.
 */

public class TestGameController {
  @Test
  public void test() throws JsonProcessingException {
    GameController test = new GameController();
    
    GameConfigData tempData = new GameConfigData();
    tempData.setGameName("test");
    tempData.setHostId("player1Id");
    String[] temp = {"player1Id", "player2Id"};
    CardData[] temp1 = {new CardData(), new CardData(), new CardData(), new CardData(), new CardData()};
    tempData.setPlayerIds(temp);
    tempData.setDeck1(temp1);
    tempData.setDeck2(temp1);
    tempData.setDeck3(temp1);
    tempData.setExDeck1(temp1);
    tempData.setExDeck2(temp1);
    tempData.setExDeck3(temp1);
    NobleData[] temp2 = {new NobleData(), new NobleData(), new NobleData()};
    tempData.setAllNobles(temp2);
    
    assertEquals(test.registerGame(null), false);
    assertEquals(test.registerGame(tempData), true);
    assertEquals(test.getGame("test").getGameId(), "test");
    assertEquals(test.getGame(null), null);
    assertEquals(test.getGame("bad"), null);
    assertEquals(test.updateGame("player1Id-test", null), false);
    assertEquals(test.updateGame("player1Id-test", new TurnData(null, null)), true);
  }
}
