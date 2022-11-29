package models;

import ca.mcgill.splendorserver.models.CardData;
import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.GameConfigData;
import ca.mcgill.splendorserver.models.NobleData;
import ca.mcgill.splendorserver.models.PlayerData;
import ca.mcgill.splendorserver.models.TurnData;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * test game data.
 * 
 */
public class TestGame {
  @Test
  public void test() {
    Game test1 = new Game();
    GameConfigData tempData = new GameConfigData();
    tempData.setGameName("test");
    tempData.setHostId("player1Id");
    String[] temp = {"player1Id", "player2Id"};
    CardData[] temp1 = {new CardData()};
    tempData.setPlayerIds(temp);
    tempData.setDeck1(temp1);
    tempData.setDeck2(temp1);
    tempData.setDeck3(temp1);
    tempData.setExDeck1(temp1);
    tempData.setExDeck2(temp1);
    tempData.setExDeck3(temp1);
    NobleData[] temp2 = {new NobleData()};
    tempData.setAllNobles(temp2);
    Game test2 = new Game("test2", tempData);
    
    PlayerData[] temp3 = {new PlayerData("Jeremy"), new PlayerData("Josh")};
    
    assertEquals(test1.getId(), "test");
    assertEquals(test1.getCurrentPlayer().getId(), temp3[0].getId());
    assertEquals(test1.getPlayers()[0].getId(), temp3[0].getId());
    
    test1.updateGame(new TurnData(-1, -1, -1));
    assertEquals(test1.getCurrentPlayer().getId(), temp3[1].getId());
  }
}
