package models;

import static org.junit.Assert.assertEquals;

import ca.mcgill.splendorserver.models.CardData;
import ca.mcgill.splendorserver.models.GameData;
import ca.mcgill.splendorserver.models.NobleData;
import ca.mcgill.splendorserver.models.PlayerData;
import org.junit.Test;

/**
 * game data test.
 */

public class TestGameData {
  @Test
  public void testMethods() {
    GameData test = new GameData();
    PlayerData[] temp = {new PlayerData()};
    NobleData[] temp1 = {new NobleData()};
    CardData[] testRow = {new CardData()};
    
    test.setNobles(temp1);
    test.setPlayers(temp);
    test.setGameId("test game");
    test.setExRow1(testRow);
    test.setExRow2(testRow);
    test.setExRow3(testRow);
    test.setRow1(testRow);
    test.setRow2(testRow);
    test.setRow3(testRow);
    test.setCurrentPlayer(0);
    

    for (int i = 0; i < temp.length; i++) {
      assertEquals(test.getPlayers()[i], temp[i]);
    }
    for (int i = 0; i < temp1.length; i++) {
      assertEquals(test.getNobles()[i], temp1[i]);
    }
    assertEquals(test.getGameId(), "test game");
    assertEquals(test.getExRow1()[0], testRow[0]);
    assertEquals(test.getExRow2()[0], testRow[0]);
    assertEquals(test.getExRow3()[0], testRow[0]);
    assertEquals(test.getRow1()[0], testRow[0]);
    assertEquals(test.getRow2()[0], testRow[0]);
    assertEquals(test.getRow3()[0], testRow[0]);
    assertEquals(test.getCurrentPlayer(), 0);
  }
}
