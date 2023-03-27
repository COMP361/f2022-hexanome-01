package ca.mcgill.splendorserver.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

  public String checkWinState() {
    int winPoints = 0;
    //some large number of cards that mostly likely no one can have
    int winCards = 100;
    int winReserve = 100;
    Player potentialWinner = null;
    List<Player> tiePlayers = new ArrayList<Player>();
    List<Player> tiePlayers2 = new ArrayList<Player>();

    for (Player player: players) {
      // 5 is the hard coded win rule
      if (player.getInventory().getPoints() >= 5) {
        if (player.getInventory().getPoints() == winPoints){
          tiePlayers.add(player);
          if(potentialWinner != null) {tiePlayers.add(potentialWinner);}
          potentialWinner = null; }
        else if (player.getInventory().getPoints() > winPoints) {
          winPoints = player.getInventory().getPoints();
          potentialWinner = player;
          if (tiePlayers.size() != 0) {
            tiePlayers.clear();
            tiePlayers = new ArrayList<Player>(); }
        }
      }

    }
      //when some players have tie points, check least number of cards
    if ( winPoints > 0 & tiePlayers.size() != 0) {
      for (Player player : tiePlayers) {
        int numCards = player.getInventory().getCards().size();
        if (numCards == winCards) {
          if(potentialWinner != null) {tiePlayers2.add(potentialWinner);}
          potentialWinner = null;
          tiePlayers2.add(player);}
        else if (numCards < winCards) {
          winCards = numCards;
          potentialWinner = player;
          tiePlayers2.clear();
          tiePlayers2 = new ArrayList<Player>();}
      }
    }

    String multiWinners = "";
    //when some players have tied number of cards in hand, check least number of reserved cards
    if ( potentialWinner == null & tiePlayers2.size() != 0) {
      //test
      //potentialWinner = new Player("num of players with tied amount of purchased cards "+Integer.toString(tiePlayers2.size()));
      for (Player player : tiePlayers2) {
        int numRes = player.getInventory().getReservedCards().size();
        if (numRes == winReserve) {
          if (potentialWinner != null) {multiWinners = potentialWinner.getUsername()+", "+player.getUsername();}
          else {multiWinners = multiWinners+", "+player.getUsername();}
          potentialWinner = null;
        }
        else if (numRes < winReserve) {
          winReserve = numRes;
          potentialWinner = player;
          multiWinners = "";}
      }
    }

    if (multiWinners.equals("")) {
      if (potentialWinner == null) {return null;}
      else {return potentialWinner.getUsername();}
      }
    else {return multiWinners;}

  }

}