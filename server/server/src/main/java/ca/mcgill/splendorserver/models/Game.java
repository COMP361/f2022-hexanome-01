package ca.mcgill.splendorserver.models;

import ca.mcgill.splendorserver.models.board.Board;
import ca.mcgill.splendorserver.models.board.CitiesBoard;

/**
 * Models one game.
 */
public class Game {

  private String id;
  private String variant;
  private String creator;
  private String[] players;


  public void setBoard(Board board) {
    this.board = board;
  }

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
    switch (variant) {
      case "cities":
        board = new CitiesBoard(creator, players);
        break;
      default:
        board = new Board(creator, players);
        break;
    }
    launched = true;
  }
  
  /**
   * Getter for the board.
   *
   * @return board object
   */
  public Board getBoard() {
    return board;
  }
}
