package models;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ca.mcgill.splendorserver.models.CardData;
import ca.mcgill.splendorserver.models.Deck;

public class TestDeck {
  @Test
  public void test() {
	  Deck test = new Deck(1);
	  CardData card1 = new CardData();
	  CardData card2 = new CardData();
	  CardData[] temp = {card1, card2, new CardData()};
	  
	  test.populate(temp);
	  
	  assertEquals(test.draw(), card1);
	  assertEquals(test.initialDraw()[0], card2);
  }
}
