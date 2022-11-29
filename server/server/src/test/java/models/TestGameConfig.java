package models;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ca.mcgill.splendorserver.models.CardData;
import ca.mcgill.splendorserver.models.GameConfigData;
import ca.mcgill.splendorserver.models.NobleData;

public class TestGameConfig {
  @Test
  public void test() {
    GameConfigData test = new GameConfigData();
    test.setGameName("test");
    test.setHostId("player1Id");
    String[] temp = {"player1Id", "player2Id"};
    CardData[] temp1 = {new CardData()};
    test.setPlayerIds(temp);
    test.setDeck1(temp1);
    test.setDeck2(temp1);
    test.setDeck3(temp1);
    test.setExDeck1(temp1);
    test.setExDeck2(temp1);
    test.setExDeck3(temp1);
    NobleData[] temp2 = {new NobleData()};
    test.setAllNobles(temp2);
    
    assertEquals(test.getHostId(), "player1Id");
    assertEquals(test.getGameName(), "test");
    assertEquals(test.getPlayerIds()[0], temp[0]);
    assertEquals(test.getDeck1()[0], temp1[0]);
    assertEquals(test.getDeck2()[0], temp1[0]);
    assertEquals(test.getDeck3()[0], temp1[0]);
    assertEquals(test.getExDeck1()[0], temp1[0]);
    assertEquals(test.getExDeck2()[0], temp1[0]);
    assertEquals(test.getExDeck3()[0], temp1[0]);
    assertEquals(test.getAllNobles()[0], temp2[0]);
  }
}
