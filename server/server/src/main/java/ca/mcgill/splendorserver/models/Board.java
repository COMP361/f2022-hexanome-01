package ca.mcgill.splendorserver.models;

/**
 * Model class for the Splendor board.
 */
public class Board {

  private TokenBank boardTokens;
  private CardBank cards;

  /**
   * Constructor.
   *
   * @param playerList String array of player usernames
   */
  public Board(String[] playerList) {
    boardTokens = new TokenBank(playerList.length + (playerList.length == 4 ? 3 : 2));
    cards = new CardBank();
  }
}
