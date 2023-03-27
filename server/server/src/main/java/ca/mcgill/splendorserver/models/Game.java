package ca.mcgill.splendorserver.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import ca.mcgill.splendorserver.models.board.Board;
import ca.mcgill.splendorserver.models.board.CitiesBoard;

/**
 * Models one game.
 */
public class Game implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;
  private String variant;
  private String creator;

  private String winner;

  private Player[] players;

  private int currentPlayerIndex;

  public void setBoard(Board board) {
    this.board = board;
  }

  private Board board;

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

    switch (variant) {
    case "cities":
    	board = new CitiesBoard(creator, players);
    	break;
    default:
    	board = new Board(creator, players);
    	break;
    }

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
   * Checker for if player won.
   *
   * @return playerId of winner, empty string if none
   */
  public String getWinner() {
    return winner;
  }

  /**
   * Flags the game as launched.
   */
  public void setLaunched() {
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
    this.board.setCurrentPlayer(getCurrentPlayer().getUsername());
  }

  /**
   * Get game's variant.

   * @return the games variant
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

  /**
   * Sets the variant.

   * @param variant new variant for this game.
   *
   */
  public void setVariant(String variant) {
    this.variant = variant;
  }

  public void setWinner(String winner) {this.winner = winner;}

public String getCreatorId() {
	return creator;
}

public Set<String> playerIdSet() {
	Set<String> set = new HashSet<String>();
	for (Player player : getPlayers()) {
		set.add(player.getUsername());
	}
	return set;
}

  public Player checkWinState() {
    int winPoints = 0;
    int winCards = 0;
    int winReserve = 0;
    int numPlayerTiePoints = 0;
    int numPlayerTieCards = 0;
    int numPlayerTieRes = 0;
    Player potentialWinner = null;
    for (Player player: players) {
      // 2 is the hard coded win rule
      if (player.getInventory().getPoints() >= 2) {
        if (player.getInventory().getPoints() == winPoints){ numPlayerTiePoints += 1; potentialWinner = null; }
        else if (player.getInventory().getPoints() > winPoints) {
          winPoints = player.getInventory().getPoints();
          if(numPlayerTiePoints != 0) { numPlayerTiePoints -= 1; }
          else { potentialWinner = player; }
        }
      }

    }
      //when some players have tie points, check least number of cards
    // if ( winPoints > 0 & numPlayerTiePoints > 0) {}
    return potentialWinner;
  }

}