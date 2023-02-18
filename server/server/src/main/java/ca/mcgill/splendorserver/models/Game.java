package ca.mcgill.splendorserver.models;

import ca.mcgill.splendorserver.models.board.Board;

/**
 * Models one game.
 */
public class Game {

  private String id;
  private String[] players;
  private String variant;
  
  private Board board;
  
  private boolean launched;
  
  /**
   * Constructor.
   *
   * @param id String representing the game id (same as session id from LobbyService)
   * @param players player usernames
   */
  public Game(String id, String[] players, String variant) {
    this.id = id;
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
