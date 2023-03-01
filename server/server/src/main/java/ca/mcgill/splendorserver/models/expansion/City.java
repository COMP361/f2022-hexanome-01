package ca.mcgill.splendorserver.models.expansion;

import org.json.simple.JSONObject;

import ca.mcgill.splendorserver.apis.JsonHandler;
import ca.mcgill.splendorserver.models.Player;

/**
 * Model class for cities from Splendor cities expansion.
 */
public class City implements Unlockable {

  private int id;
  private Condition condition; //condition to unlock this city

  public City(JSONObject jsonObject) {
    id = Integer.parseInt(jsonObject.get("id").toString());
    condition = 
        new Condition((JSONObject) JsonHandler.decodeJsonRequest(jsonObject.get("condition").toString()));
  }

  @Override
  public void observe(Player player) {
    // TODO Auto-generated method stub

  }

  @Override
  public void use(Player player) {
    // TODO Auto-generated method stub

  }

  @Override
  public int getId() {
    // TODO Auto-generated method stub
    return id;
  }

}
