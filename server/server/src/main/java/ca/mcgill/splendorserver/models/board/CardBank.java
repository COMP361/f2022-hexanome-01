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
      return -1;
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
   * @param cardId the cardId of the card being replaced
   * @return the id of the newly drawn card
   */
  public int draw(int cardId) {
    Card card = CardRegistry.of(cardId);
    Stack<Integer> deck = decks.get(card.getLevel());
    if (deck.isEmpty()) {
      return -1;
    }
    int[] row = rows.get(card.getLevel());
    for (int i = 0; i < row.length; i++) {
      if (row[i] == card.getId()) {
        int old = row[i];
        row[i] = deck.pop();
        rows.put(card.getLevel(), row);
        return old;
      }
    }
    return -1;
  }

  public int drawCardFromDeck(CardLevel level) {
    Stack<Integer> deck = decks.get(level);
    return deck.pop();
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
   * Returns if true if the board contains the card
   * with the id provided.
   *
   * @param id card identifier
   * @return true if its found in board else false.
   */
  public boolean containsCard(int id) {
    if (id >= 64 & id <= 103) {
      return cardIsPartOfRow(CardLevel.LEVEL1, id);
    } else if (id >= 20 & id <= 50) {
      return cardIsPartOfRow(CardLevel.LEVEL2, id);
    } else if (id >= 0 & id <= 19) {
      return cardIsPartOfRow(CardLevel.LEVEL3, id);
    } else {
      if (cardIsPartOfRow(CardLevel.ORIENT_LEVEL1, id)) {
        return true;
      } else if (cardIsPartOfRow(CardLevel.ORIENT_LEVEL2, id)) {
        return true;
      } else if (cardIsPartOfRow(CardLevel.ORIENT_LEVEL3, id)) {
        return true;
      }
      return false;
      //Couldn't find card;

    }
  }

  private boolean cardIsPartOfRow(CardLevel level, int id) {
    int[] cardOnRow = rows.get(level);
    for (int card : cardOnRow) {
      if (card == id) {
        return true;
      }
    }
    return false;
  }

  /**
   * Getter for an array of JSONArrays of the cards and decks on the board.
   *
   * @return the cards and decks on the board as an array of JSONArrays
   */
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

  /**
   * Converter converting a string representation of a cardlevel to the enum.
   *
   * @return level of the desired (enum).
   */
  public static CardLevel getCardLevelFromString(String cardLevel) {
    if (cardLevel.equals("Level1")) {
      return CardLevel.LEVEL1;
    } else if (cardLevel.equals("Level2")) {
      return CardLevel.LEVEL2;
    } else if (cardLevel.equals("Level3")) {
      return CardLevel.LEVEL3;
    } else if (cardLevel.equals("OrientLevel1")) {
      return CardLevel.ORIENT_LEVEL1;
    } else if (cardLevel.equals("OrientLevel2")) {
      return CardLevel.ORIENT_LEVEL2;
    } else if (cardLevel.equals("OrientLevel3")) {
      return CardLevel.ORIENT_LEVEL3;
    }
    return null;
  }
}
