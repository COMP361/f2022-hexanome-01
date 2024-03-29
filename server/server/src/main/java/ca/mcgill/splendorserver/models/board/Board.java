package ca.mcgill.splendorserver.models.board;


import ca.mcgill.splendorserver.models.Inventory;
import ca.mcgill.splendorserver.models.Player;
import java.io.Serializable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Model class for the Splendor board.
 */
public class Board implements Serializable {

  private static final long serialVersionUID = 1977163128205559350L;

  private String currentPlayer;

  private String host;

  private String winner;

  private Player[] players;
  private TokenBank tokens;


  private CardBank cards;
  private NobleBank nobles;

  /**
   * Constructor.
   *
   * @param creator the creator of the game.
   * @param players String array of player usernames.
   */
  public Board(String creator, Player[] players) {
    int playerNum = players.length;
    tokens = new TokenBank(playerNum + (playerNum == 4 ? 3 : 2));
    cards = new CardBank();
    nobles = new NobleBank(playerNum + 1);
    this.players = players;
    this.currentPlayer = creator;
    this.host = creator;
  }

  public void setCurrentPlayer(String player) {
    currentPlayer = player;
  }

  /**
   * Gets the inventory of the player with the provided id.
   *
   * @param playerId the id of player.
   * @return Inventory object.
   */
  public Inventory getInventory(String playerId) {
    for (Player player : players) {
      if (player.getUsername().equals(playerId)) {
        return player.getInventory();
      }
    }
    return null;
  }

  public NobleBank getNobles() {
    return nobles;
  }

  public TokenBank getTokens() {
    return tokens;
  }

  /**
   * Getter for the board as a JSONObject.
   *
   * @return the board as a JSONObject
   */
  @SuppressWarnings("unchecked")
  public JSONObject toJson() {
    JSONObject json = new JSONObject();
    json.put("currentPlayer", currentPlayer);
    JSONArray[] cardsAndDecks = cards.toJson();
    json.put("cards", cardsAndDecks[0]);
    json.put("decks", cardsAndDecks[1]);
    json.put("nobles", nobles.toJson());
    json.put("tokens", tokens.toJson());
    json.put("host", host);
    json.put("winner", winner);

    JSONObject inventoriesJson = new JSONObject();
    for (Player player : players) {
      inventoriesJson.put(player.getUsername(), player.getInventory().toJson());
    }
    json.put("inventories", inventoriesJson);

    return json;
  }

  /**
   * returns the cards in this game.
   *
   * @return cards in the board
   */
  public CardBank getCards() {
    return cards;
  }

  /**
   * Sets the bank of card for this board.
   *
   * @param cards bank of cards
   */
  public void setCards(CardBank cards) {
    this.cards = cards;
  }

  public String getHost() {
    return host;
  }

  public void setWinner(String winner) {
    this.winner = winner;
  }


}
