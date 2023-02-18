package ca.mcgill.splendorserver.models.board;

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
  private NobleBank nobles;

  /**
   * Constructor.
   *
   * @param variant the game variant
   * @param playerList String array of player usernames
   */
  public Board(int player_num, String variant) {
    boardTokens = new TokenBank(player_num + (player_num == 4 ? 3 : 2));
    cards = new CardBank();
    nobles = new NobleBank(player_num + 1);
  }
}
