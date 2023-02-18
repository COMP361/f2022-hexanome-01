package ca.mcgill.splendorserver.models;

/**
 * Models one game.
 */
public class Game {

  private String id;
  private String variant;
  private String creator;
  private String[] players;
  private boolean launched;
  private String currentPlayer;
  private Board board;

  /**
   * Constructor.
   *
   * @param id String representing the game id (same as session id from LobbyService)
   * @param variant the variant of the game
   * @param players player usernames
   * @param creator the creator of the session
   */
  public Game(String id, String variant, String[] players, String creator) {
    this.id = id;
    this.variant = variant;
    this.players = players;
    this.creator = creator;
    launched = false;
    currentPlayer = creator;
    board = new Board(variant, players);
    //TO DO: create player inventories?
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
   * Marks the game as launched.
   */
  public void setLaunched() {
    launched = true;
  }
}
