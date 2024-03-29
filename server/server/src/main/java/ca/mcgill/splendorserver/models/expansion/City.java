package ca.mcgill.splendorserver.models.expansion;

import ca.mcgill.splendorserver.apis.JsonHandler;
import ca.mcgill.splendorserver.models.Player;
import java.io.Serializable;
import org.json.simple.JSONObject;

/**
 * Model class for cities from Splendor cities expansion.
 */
public class City implements Unlockable, Serializable {

  private static final long serialVersionUID = -6421194523179986103L;
  private int id;
  private Condition condition; //condition to unlock this city

  /**
   * Constructor.
   *
   * @param jsonObject a JSONObject of the city
   */
  public City(JSONObject jsonObject) {
    id = Integer.parseInt(jsonObject.get("id").toString());
    condition = 
        new Condition((JSONObject) 
          JsonHandler.decodeJsonRequest(jsonObject.get("condition").toString()));
  }

  @Override
  public void observe(Player player) {
    if (!player.getInventory().getUnlockables().contains(this) 
        && condition.checkCondition(player.getInventory())) {
      //owners.add(player);
      player.getInventory().getUnlockables().add(this);
      //adds this city to players inventory, does not check if multiple unlocks
    }
  }

  @Override
  public void use(Player player) {
    // TODO Auto-generated method stub

  }

  @Override
  public int getId() {
    return id;
  }

  /**
   * Getter for this city's point requirement.
   *
   * @return point threshold
   */
  public int getPoints() {
    return condition.getPoints();
  }

}
