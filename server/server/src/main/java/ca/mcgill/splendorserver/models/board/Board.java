package ca.mcgill.splendorserver.models.board;

import ca.mcgill.splendorserver.models.Inventory;
import ca.mcgill.splendorserver.models.JsonStringafiable;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Model class for the Splendor board.
 */
public class Board implements JsonStringafiable {

  private String currentPlayer;

  private HashMap<String, Inventory> inventories;
  private TokenBank tokens;
  private CardBank cards;
  private NobleBank nobles;

  /**
   * Constructor.
   *
   * @param creator the creator of the game
   * @param players String array of player usernames
   */
  public Board(String creator, String[] players) {
    int playerNum = players.length;
    tokens = new TokenBank(playerNum + (playerNum == 4 ? 3 : 2));
    cards = new CardBank();
    nobles = new NobleBank(playerNum + 1);
    inventories = new HashMap<String, Inventory>();
    for (String playerId : players) {
      inventories.put(playerId, new Inventory());
    }
    this.currentPlayer = creator;
  }

  public Inventory getInventory(String playerId) {
    return inventories.get(playerId);
  }

  public CardBank getCards() {
    return cards;
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
    for (String playerId : inventories.keySet()) {
      inventoryJson.put(playerId, inventories.get(playerId).toJsonString());
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
    for (String playerId : inventories.keySet()) {
      inventoriesJson.put(playerId, inventories.get(playerId).toJson());
    }
    json.put("inventories", inventoriesJson);

    return json;
  }
}
