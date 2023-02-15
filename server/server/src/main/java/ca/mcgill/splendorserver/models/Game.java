package ca.mcgill.splendorserver.models;

/**
 * Models one game.
 */
public class Game {

  private String id;
  private String[] players;

  /**
   * Constructor.
   *
   * @param id String representing the game id (same as session id from LobbyService)
   * @param players player usernames
   */
  public Game(String id, String[] players) {
    this.id = id;
    this.players = players;
  }

  /**
   * Getter for game id.
   *
   * @return String representing the game id (same as session id from LobbyService)
   */
  public String getId() {
    return id;
  }
}
