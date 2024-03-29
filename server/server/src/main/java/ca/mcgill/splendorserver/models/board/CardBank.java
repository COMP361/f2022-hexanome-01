package ca.mcgill.splendorserver.models.board;

import ca.mcgill.splendorserver.models.cards.Card;
import ca.mcgill.splendorserver.models.cards.CardLevel;
import ca.mcgill.splendorserver.models.registries.CardRegistry;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Stack;
import org.json.simple.JSONArray;

/**
 * Model class holding all Splendor development card decks.
 */
public class CardBank implements Serializable {

  private static final long serialVersionUID = 4250728141496188403L;
  
  private HashMap<CardLevel, int[]> rows = new HashMap<CardLevel, int[]>();
  private HashMap<CardLevel, Stack<Integer>> decks = new HashMap<CardLevel, Stack<Integer>>();

  private static final CardLevel[] regularLevels = {
      CardLevel.LEVEL1, CardLevel.LEVEL2, CardLevel.LEVEL3
  };
  private static final CardLevel[] orientLevels = {
      CardLevel.ORIENTLEVEL1, CardLevel.ORIENTLEVEL2, CardLevel.ORIENTLEVEL3
  };

  private static final int REGULAR_NUM = 4;
  private static final int ORIENT_NUM = 2;

  /**
   * Constructor.
   */
  public CardBank() {

    for (CardLevel level : CardLevel.values()) {
      Stack<Integer> deck = new Stack<Integer>();
      for (Card card : CardRegistry.getCards()) {
        if (card.getLevel().equals(level)) {
          deck.push(card.getId());
        }
      }
      Collections.shuffle(deck);
      decks.put(level, deck);
    }

    for (CardLevel level : regularLevels) {
      int[] cards = new int[REGULAR_NUM];
      for (int i = 0; i < REGULAR_NUM; i++) {
        cards[i] = decks.get(level).pop();
      }
      rows.put(level, cards);
    }
    for (CardLevel level : orientLevels) {
      int[] cards = new int[ORIENT_NUM];
      for (int i = 0; i < ORIENT_NUM; i++) {
        cards[i] = decks.get(level).pop();
      }
      rows.put(level, cards);
    }
  }

  /**
   * Getter for the hashmap of displayed cards.
   *
   * @return the rows of displayed cards.
   */
  public HashMap<CardLevel, int[]> getRows() {
    return rows;
  }

  /**
   * Draw a card from the deck.
   *
   * @param index the index of the card to draw
   * @param level the level of the card to draw
   * @return the id of the card that used to be in the new cards spot
   */
  public int draw(CardLevel level, int index) {
    Stack<Integer> deck = decks.get(level);
    if (deck.isEmpty()) {
      int[] row = rows.get(level);
      int old = row[index];
      row[index] = -1;
      rows.put(level, row);
      return old;
    }
    int[] row = rows.get(level);
    int old = row[index];
    row[index] = deck.pop();
    rows.put(level, row);
    return old;
  }

  /**
   * Draws a card.
   *
   * @param card the card being replaced
   * @return the id of the newly drawn card
   */
  public int draw(Card card) {
    Stack<Integer> deck = decks.get(card.getLevel());
    int[] row = rows.get(card.getLevel());
    for (int i = 0; i < row.length; i++) {
      if (row[i] == card.getId()) {
        if (deck.isEmpty()) {
          int old = row[i];
          row[i] = -1;
          rows.put(card.getLevel(), row);
          return old;
        }
        int old = row[i];
        row[i] = deck.pop();
        rows.put(card.getLevel(), row);
        return old;
      }
    }
    return -1;
  }

  /**
   * Draws a card.
   *
   * @param level level of the deck being drawn from
   * @return the id of the newly drawn card
   */
  public int drawCardFromDeck(CardLevel level) {
    Stack<Integer> deck = decks.get(level);
    if (deck.isEmpty()) { 
      return -1; 
    }
    return deck.pop();
  }

  /**
   * Getter for an array of JSONArrays of the cards and decks on the board.
   *
   * @return the cards and decks on the board as an array of JSONArrays
   */
  @SuppressWarnings("unchecked")
public JSONArray[] toJson() {
    JSONArray cardsJson = new JSONArray();
    //rows
    for (CardLevel level : CardLevel.values()) {
      int[] row = rows.get(level);
      JSONArray list = new JSONArray();
      for (int cardId : row) {
        list.add(cardId);
      }
      cardsJson.add(list);
    }
    //decks (whether or not they're empty)
    JSONArray decksJson = new JSONArray();
    for (CardLevel level : CardLevel.values()) {
      if (!decks.get(level).isEmpty()) {
        decksJson.add(level.toString().toLowerCase());
      }
    }
    return new JSONArray[] {cardsJson, decksJson};
  }

}
