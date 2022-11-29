package models;

import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.GameConfigData;
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
    String[] temp = {"player1Id", "player2Id"};
    Game test  = new Game("id", new GameConfigData("name", "player1Id", temp));
    PlayerData[] temp1 = {new PlayerData("player1Id"), new PlayerData("player2Id")};
    
    assertEquals(test.getId(), "id");
    assertEquals(test.getCurrentPlayer().getId(), temp1[0].getId());
    assertEquals(test.getPlayers()[0].getId(), temp1[0].getId());
    
    test.updateGame(new TurnData(-1, -1, -1));
    assertEquals(test.getCurrentPlayer().getId(), temp1[1].getId());
  }
}
