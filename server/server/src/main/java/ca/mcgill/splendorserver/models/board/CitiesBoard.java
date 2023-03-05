package ca.mcgill.splendorserver.models.board;

import ca.mcgill.splendorserver.models.Player;
import ca.mcgill.splendorserver.models.registries.UnlockableRegistry;
import java.util.Collections;
import java.util.Stack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Model class for a Splendor board with cities expansion.
 */
public class CitiesBoard extends Board {

  private int[] cities;

  /**
   * Constructor.
   *
   * @param creator the creator of the session to pass to Board's constructor
   * @param players the players in the session to pass to Board's constructor
   */
  public CitiesBoard(String creator, Player[] players) {
    super(creator, players);
    cities = new int[3];
    Stack<Integer> cityIds = UnlockableRegistry.getCityIds();
    Collections.shuffle(cityIds);
    for (int i = 0; i < cities.length; i++) {
      cities[i] = cityIds.pop();
    }
  }

  /**
   * getter for list of cities.
   *
   */
  public int[] getCities() {
    return cities;
  }
  /**
   * Removes a city from the board.
   *
   * @param index the index of the city to remove
   * @return whether the removal was successful
   */
  public boolean remove(int index) {
    if (index >= cities.length || cities[index] == -1) {
      return false;
    }
    cities[index] = -1;
    return true;
  }

  @Override
  public JSONObject toJson() {
    JSONObject json = super.toJson();

    JSONArray citiesJson = new JSONArray();
    for (int id : cities) {
      citiesJson.add(id);
    }

    json.put("cities", citiesJson);
    return json;
  }
}
