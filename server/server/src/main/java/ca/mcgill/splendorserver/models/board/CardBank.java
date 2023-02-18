package ca.mcgill.splendorserver.models.board;

import java.util.Collections;
import java.util.HashMap;
import java.util.Stack;

import org.json.simple.JSONArray;

import ca.mcgill.splendorserver.models.JSONStringafiable;
import ca.mcgill.splendorserver.models.cards.CardLevel;

/**
 * Model class holding all Splendor development card decks.
 */
public class CardBank implements JSONStringafiable {
	
	private HashMap<CardLevel, int[]> rows = new HashMap<CardLevel, int[]>();
	private HashMap<CardLevel, Stack<Integer>> decks = new HashMap<CardLevel, Stack<Integer>>();

	private static final CardLevel[] regularLevels = { CardLevel.LEVEL1, CardLevel.LEVEL2, CardLevel.LEVEL3 };
	private static final CardLevel[] orientLevels = { CardLevel.ORIENT_LEVEL1, CardLevel.ORIENT_LEVEL2, CardLevel.ORIENT_LEVEL3 };
	
	private static final int REGULAR_NUM = 4;
	private static final int ORIENT_NUM = 2;

  /**
   * Constructor.
   */
  public CardBank() {
	  
	  for (CardLevel level : CardLevel.values()) {
		  decks.put(level, new Stack<Integer>());
		  Collections.shuffle(decks.get(level));
	  }
	  
	  for (CardLevel level : regularLevels) {
		  int[] cards = new int[REGULAR_NUM];
		  for (int i=0; i<REGULAR_NUM; i++) cards[i] = decks.get(level).pop();
		  rows.put(level, cards);
	  }
	  for (CardLevel level : orientLevels) {
		  int[] cards = new int[ORIENT_NUM];
		  for (int i=0; i<ORIENT_NUM; i++) cards[i] = decks.get(level).pop();
		  rows.put(level, cards);
	  }
  }

  /**
   * Add a card to a deck.
   *
   * @param card the card to add to the deck
   */
  public int draw(CardLevel level, int index) {
	  Stack<Integer> deck = decks.get(level);
	  if (deck.isEmpty()) return -1;
	  int[] row = rows.get(level);
	  int old = row[index];
	  row[index] = deck.pop();
	  rows.put(level, row);
	  return old;
  }

@Override
public String toJSONString() {
	JSONArray data = new JSONArray();
	for (CardLevel level : CardLevel.values()) {
		int [] row = rows.get(level);
		JSONArray list = new JSONArray();
		for (int cardId : row) {
			list.add(cardId);
		}
		data.add(list.toJSONString());
	}
	return data.toJSONString();
}
}
