package ca.mcgill.splendorserver.models;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
  
  private ArrayList<CardData> cardsInDeck;
  private int onBoard;
  
  public Deck(int onBoard) {
    cardsInDeck = new ArrayList<CardData>();
    this.onBoard = onBoard;
  }
  
  public void populate(CardData[] cards) {
    for (CardData card : cards) {
      cardsInDeck.add(card);
    }
  }

  public void shuffle() {
    Collections.shuffle(cardsInDeck);
  }
  
  public CardData draw() {
	  return cardsInDeck.remove(0);
  }
  
  public CardData[] initialDraw() {
	  CardData[] row = new CardData[onBoard];
	  for (int i=0; i<onBoard; i++) {
		  row[i] = draw();
	  }
	  return row;
  }
  
}
