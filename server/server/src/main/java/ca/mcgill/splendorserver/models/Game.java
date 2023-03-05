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

  private Player[] players;

  private int currentPlayerIndex;

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
   * @param players player objects
   * @param creator the creator of the session
   */
  public Game(String id, String creator, Player[] players, String variant) {
    this.id = id;
    this.variant = variant;
    this.creator = creator;
    this.players = players;

    launched = false;
    setLaunched();

    currentPlayerIndex = 0;
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

  /**
   * Getter for the players who are part of this game.
   *
   * @return String of players in this game
   */
  public Player[] getPlayers() {
    return players;
  }

  /**
   * Setter for the players who are part of this game.
   *
   * @param players the players in this game
   */
  public void setPlayers(Player[] players) {
    this.players = players;
  }

  public Player getCurrentPlayer() {
    return players[currentPlayerIndex];
  }

  /**
   * Changes the current player to the next player.
   */
  public void nextPlayer() {
    currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
  }

  /**
   * Get game's vairant.
   */
  public String getVariant() {
    return variant;
  }

  /**
   * Sets current player to player with given username.

   * @param username of desired player.
   * 
   */
  public void setCurrentPlayer(String username) {
    for (int i = 0; i < players.length; i++) {
      if (players[i].getUsername().equals(username)) {
        currentPlayerIndex = i;
        return;
      }
    }
  }
}
