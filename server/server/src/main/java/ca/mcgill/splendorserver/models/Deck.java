package ca.mcgill.splendorserver.models;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Holds the data for each deck as stored on client as well.
 */
public class Deck {
  
  private ArrayList<CardData> cardsInDeck;
  private int onBoard;

  /**
   * Constructs deck.
   *
   * @param onBoard card number on board
   */
  public Deck(int onBoard) {
    cardsInDeck = new ArrayList<CardData>();
    this.onBoard = onBoard;
  }

  /**
   * Constructs deck from array of cards.
   *
   * @param onBoard card number on board
   * @param cards cards to init with
   */
  public Deck(int onBoard, CardData[] cards) {
    cardsInDeck = new ArrayList<CardData>();
    Collections.addAll(cardsInDeck, cards);
    this.onBoard = onBoard;
  }

  /**
   * Populates deck.
   *
   * @param cards for population
   */
  public void populate(CardData[] cards) {
    for (CardData card : cards) {
      cardsInDeck.add(card);
    }
  }

  /**
   * Shuffles deck.
   * 
   */
  public void shuffle() {
    Collections.shuffle(cardsInDeck);
  }

  /**
   * Draws from deck.
   *
   * @return card to be drawn
   */
  public CardData draw() {
    return cardsInDeck.remove(0);
  }

  /**
   * Initial draw.
   *
   * @return row to be presented
   */
  public CardData[] initialDraw() {
    CardData[] row = new CardData[onBoard];
    for (int i = 0; i < onBoard; i++) {
      row[i] = draw();
    }
    return row;
  }
  
}
