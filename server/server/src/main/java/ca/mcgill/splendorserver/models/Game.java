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
  private String variant;
  
  private Board board;
  
  private boolean launched;
  
  /**
   * Constructor.
   *
   * @param id String representing the game id (same as session id from LobbyService)
   * @param variant the variant of the game
   * @param players player usernames
   * @param creator the creator of the session
   */
  public Game(String id, String[] players, String variant) {
    this.id = id;
    this.variant = variant;
    this.players = players;
    this.variant = variant;
    
    launched = false;
  }

  /**
   * Getter for game id.
   *
   * @return String representing the game id (same as session id from LobbyService)
   */
  public String getId() {
    return id;
  }
  
  public void setLaunched() {
	  board = new Board(players.length, variant);
	  
	  launched = true;
  }
}
