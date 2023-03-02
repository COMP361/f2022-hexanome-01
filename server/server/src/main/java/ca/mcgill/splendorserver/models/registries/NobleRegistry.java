package ca.mcgill.splendorserver.models.registries;

import ca.mcgill.splendorserver.apis.GameController;
import ca.mcgill.splendorserver.apis.JsonHandler;
import ca.mcgill.splendorserver.models.Noble;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Set;
import java.util.Stack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Registry of Splendor noble tiles.
 */
public class NobleRegistry implements Registry<Noble> {

  @SuppressWarnings("serial")
  private static final HashMap<Integer, Noble> data = new HashMap<Integer, Noble>();

  static {
    try {
      InputStream is = GameController.class.getClassLoader().getResourceAsStream("Nobles.txt");
      JSONArray nobles = (JSONArray) JsonHandler.decodeJsonRequest(
          new String(is.readAllBytes(), StandardCharsets.UTF_8));

      for (Object obj : nobles) {
        Noble noble = new Noble((JSONObject) (JsonHandler.decodeJsonRequest((String) obj)));
        data.put(noble.getId(), noble);
      }

    } catch (IOException e) {
      throw new RegistryException("nobles", e);
    }
  }

  /**
   * Static version of listIds.
   *
   * @return a set of unique noble ids.
   */
  public static Stack<Integer> getIds() {
    Stack<Integer> ids = new Stack<>();
    ids.addAll(data.keySet());
    return ids;
  }

  public Set<Integer> listIds() {
    return data.keySet();
  }

  public static Noble of(int id) {
    return data.get(id);
  }
}
