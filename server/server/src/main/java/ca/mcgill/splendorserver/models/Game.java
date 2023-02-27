package ca.mcgill.splendorserver.models;

import ca.mcgill.splendorserver.models.board.Board;

/**
 * Models one game.
 */
public class Game {

  private String id;
  private String variant;
  private String creator;
  private String[] players;

  private Board board;

  private boolean launched;

  /**
   * Constructor.
   *
   * @param id      String representing the game id (same as session id from LobbyService)
   * @param variant the variant of the game
   * @param players player usernames
   * @param creator the creator of the session
   */
  public Game(String id, String creator, String[] players, String variant) {
    this.id = id;
    this.variant = variant;
    this.creator = creator;
    this.players = players;

    launched = false;
    setLaunched();
  }

  /**
   * Getter for game id.
   *
   * @return String representing the game id (same as session id from LobbyService)
   */
  public String getId() {
    return id;
  }

  /**
   * Flags the game as launched.
   */
  public void setLaunched() {
    board = new Board(creator, players, variant);

    launched = true;
  }

  /**
   * Getter for the board as a JSON string.
   *
   * @return the game board as a JSON string
   */
  public String getBoardJson() {
    return board.toJson().toJSONString();
  }
}
