package ca.mcgill.splendorserver.models.board;

import ca.mcgill.splendorserver.models.Inventory;
import ca.mcgill.splendorserver.models.JsonStringafiable;
import ca.mcgill.splendorserver.models.Player;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Model class for the Splendor board.
 */
public class Board implements JsonStringafiable {

  private String currentPlayer;

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

  @SuppressWarnings("unchecked")
  @Override
  public String toJsonString() {
    JSONObject data = new JSONObject();
    data.put("currentPlayer", currentPlayer);

    data.put("cards", cards.toJsonString());
    data.put("nobles", nobles.toJsonString());
    data.put("tokens", tokens.toJsonString());

    JSONObject inventoryJson = new JSONObject();
    for (Player player : players) {
      inventoryJson.put(player.getUsername(), player.getInventory().toJsonString());
    }

    data.put("inventories", inventoryJson.toJSONString());

    return data.toJSONString();
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

    JSONObject inventoriesJson = new JSONObject();
    for (Player player : players) {
      inventoriesJson.put(player.getUsername(), player.getInventory().toJsonString());
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

}
