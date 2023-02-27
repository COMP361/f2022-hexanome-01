package ca.mcgill.splendorserver.models.board;

import ca.mcgill.splendorserver.models.JsonStringafiable;
import ca.mcgill.splendorserver.models.cards.Card;
import ca.mcgill.splendorserver.models.cards.CardLevel;
import ca.mcgill.splendorserver.models.registries.CardRegistry;
import java.util.Collections;
import java.util.HashMap;
import java.util.Stack;
import org.json.simple.JSONArray;

/**
 * Model class holding all Splendor development card decks.
 */
public class CardBank implements JsonStringafiable {

  private HashMap<CardLevel, int[]> rows = new HashMap<CardLevel, int[]>();
  private HashMap<CardLevel, Stack<Integer>> decks = new HashMap<CardLevel, Stack<Integer>>();

  private static final CardLevel[] regularLevels = {
      CardLevel.LEVEL1, CardLevel.LEVEL2, CardLevel.LEVEL3
  };
  private static final CardLevel[] orientLevels = {
      CardLevel.ORIENT_LEVEL1, CardLevel.ORIENT_LEVEL2, CardLevel.ORIENT_LEVEL3
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
   * Draw a card from the deck.
   *
   * @param index the index of the card to draw
   * @param level the level of the card to draw
   * @return the id of the card that used to be in the new cards spot
   */
  public int draw(CardLevel level, int index) {
    Stack<Integer> deck = decks.get(level);
    if (deck.isEmpty()) {
      return -1;
    }
    int[] row = rows.get(level);
    int old = row[index];
    row[index] = deck.pop();
    rows.put(level, row);
    return old;
  }

  @Override
  public String toJsonString() {
    JSONArray data = new JSONArray();
    for (CardLevel level : CardLevel.values()) {
      int[] row = rows.get(level);
      JSONArray list = new JSONArray();
      for (int cardId : row) {
        list.add(cardId);
      }
      data.add(list.toJSONString());
    }
    return data.toJSONString();
  }

  /**
   * Getter for a JSONArray of the cards on the board.
   *
   * @return the cards on the board as a JSONArray
   */
  public JSONArray toJson() {
    JSONArray json = new JSONArray();
    for (CardLevel level : CardLevel.values()) {
      int[] row = rows.get(level);
      JSONArray list = new JSONArray();
      for (int cardId : row) {
        list.add(cardId);
      }
      json.add(list);
    }
    return json;
  }
}
