package ca.mcgill.splendorserver.models;

import ca.mcgill.splendorserver.apis.GameController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Model class for the Splendor board.
 */
public class Board {

  private TokenBank boardTokens;
  private CardBank cards;
  private String variant;
  private Noble[] nobles;

  /**
   * Constructor.
   *
   * @param variant the game variant
   * @param playerList String array of player usernames
   */
  public Board(String variant, String[] playerList) {
    this.variant = variant;
    boardTokens = new TokenBank(playerList.length + (playerList.length == 4 ? 3 : 2));
    cards = new CardBank(); //and shuffles decks and places cards on board

    //initialize nobles
    nobles = new Noble[playerList.length + 1];
    HashMap<String, Noble> nobleRegistry = GameController.getAllNobles();
    ArrayList<Noble> nobleList = new ArrayList<>(nobleRegistry.values());
    Collections.shuffle(nobleList);
    Iterator<Noble> nobleIterator = nobleList.iterator();
    for (int i = 0; i < nobles.length; i++) {
      nobles[i] = nobleIterator.next();
    }
    //TO DO: select cities
  }
}
