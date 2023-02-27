package ca.mcgill.splendorserver.models.board;

import ca.mcgill.splendorserver.models.Inventory;
import ca.mcgill.splendorserver.models.JsonStringafiable;
import java.util.HashMap;
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
   * @param variant the game variant
   * @param players String array of player usernames
   */
  public Board(String creator, String[] players, String variant) {
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
}
