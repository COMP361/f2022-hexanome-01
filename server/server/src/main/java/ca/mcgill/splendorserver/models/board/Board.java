package ca.mcgill.splendorserver.models.board;

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
   * @param playerList String array of player usernames
   */
  public Board(int player_num, String variant) {
    boardTokens = new TokenBank(player_num + (player_num == 4 ? 3 : 2));
    cards = new CardBank();
    nobles = new NobleBank(player_num + 1);
  }
}
