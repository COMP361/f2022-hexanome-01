package ca.mcgill.splendorserver.models.registries;

import ca.mcgill.splendorserver.apis.GameController;
import ca.mcgill.splendorserver.apis.JsonHandler;
import ca.mcgill.splendorserver.models.expansion.City;
import ca.mcgill.splendorserver.models.expansion.TradingPost;
import ca.mcgill.splendorserver.models.expansion.Unlockable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Set;
import java.util.Stack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Registry of unlockables, i.e. cities or trading posts from the Splendor expansion packs.
 */
public class UnlockableRegistry implements Registry<Unlockable> {

  @SuppressWarnings("serial")
  static HashMap<Integer, Unlockable> data = new HashMap<Integer, Unlockable>();

  static {
    try {
      InputStream is = GameController.class.getClassLoader().getResourceAsStream("Unlockables.txt");
      JSONArray unlockables = (JSONArray) JsonHandler.decodeJsonRequest(
          new String(is.readAllBytes(), StandardCharsets.UTF_8));

      for (Object obj : unlockables) {
        JSONObject json = (JSONObject) (JsonHandler.decodeJsonRequest((String) obj));
        Unlockable unlockable;
        if (json.get("effectType").equals("City")) {
          unlockable = new City(json);
        } else {
          unlockable = new TradingPost(json);
        }
        data.put(unlockable.getId(), unlockable);
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Getter for the ids of the city tiles.
   *
   * @return a Stack of the ids of the city tiles
   */
  public static Stack<Integer> getCityIds() {
    Stack<Integer> ids = new Stack<>();
    for (Integer id : data.keySet()) {
      if (data.get(id).getClass() == City.class) {
        ids.push(id);
      }
    }
    return ids;
  }

  public Set<Integer> listIds() {
    return data.keySet();
  }

  public Unlockable of(int id) {
    return data.get(id);
  }

}
