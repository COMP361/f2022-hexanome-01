package ca.mcgill.splendorserver.models.board;

import java.util.HashMap;

import org.json.simple.JSONObject;

import ca.mcgill.splendorserver.models.Inventory;
import ca.mcgill.splendorserver.models.JSONStringafiable;

/**
 * Model class for the Splendor board.
 */
public class Board implements JSONStringafiable {
	
  private String currentPlayer;

  private HashMap<String, Inventory> inventories;
  private TokenBank tokens;
  private CardBank cards;
  private NobleBank nobles;

  /**
   * Constructor.
   *
   * @param variant the game variant
   * @param playerList String array of player usernames
   */
  public Board(String creator, String[] players, String variant) {
	int player_num = players.length;
	tokens = new TokenBank(player_num + (player_num == 4 ? 3 : 2));
    cards = new CardBank();
    nobles = new NobleBank(player_num + 1);
    for (String playerId : players) {
    	inventories.put(playerId, new Inventory());
    }
    this.currentPlayer = creator;
  }

  @SuppressWarnings("unchecked")
  @Override
  public String toJSONString() {
	  JSONObject data = new JSONObject();
	  data.put("currentPlayer", currentPlayer);
	  
	  data.put("cards", cards.toJSONString());
	  data.put("nobles", nobles.toJSONString());
	  data.put("tokens", tokens.toJSONString());
	  
	  JSONObject inventoryJSON = new JSONObject();
	  for (String playerId : inventories.keySet())
		  inventoryJSON.put(playerId, inventories.get(playerId).toJSONString());
	  
	  data.put("inventories", inventoryJSON.toJSONString());
	  
	  return data.toJSONString();
  }
}
