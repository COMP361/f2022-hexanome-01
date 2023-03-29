package ca.mcgill.splendorserver.models.expansion;

import ca.mcgill.splendorserver.models.Inventory;
import ca.mcgill.splendorserver.models.Token;
import java.util.HashMap;
import org.json.simple.JSONObject;

/**
 * Condition for unlocking an unlockable.
 */
public class Condition {
  private HashMap<String, Integer> gemConditions = new HashMap<>();
  private int genericGemCost;
  private boolean nobleRequired;
  private int points;

  /**
   * Constructor.
   *
   * @param obj a JSONObject of the city
   */
  public Condition(JSONObject obj) {
    points = Integer.parseInt(obj.get("points").toString());
    genericGemCost = Integer.parseInt(obj.get("generic").toString());
    nobleRequired = Boolean.parseBoolean(obj.get("nobleRequired").toString());
    
    gemConditions.put("red", Integer.parseInt(obj.get("red").toString()));
    gemConditions.put("blue", Integer.parseInt(obj.get("blue").toString()));
    gemConditions.put("green", Integer.parseInt(obj.get("green").toString()));
    gemConditions.put("white", Integer.parseInt(obj.get("white").toString()));
    gemConditions.put("black", Integer.parseInt(obj.get("brown").toString()));
  }
  
  /**
   * Check if a player's inventory meets the conditions defined by this condition.
   *
   * @param inventory inventory of the player who may or may not meet this condition.
   * @return whether the inventory meets the conditions stipulated.
   */
  public boolean checkCondition(Inventory inventory) {
    if (nobleRequired && inventory.getNobles().isEmpty()) {
      return false;
    }

    if (points > inventory.getPoints()) {
      return false;
    }

    if (genericGemCost == 0) {
      return (gemConditions.get("red") <= inventory.getBonuses().checkAmount(Token.RED)
        && gemConditions.get("blue") <= inventory.getBonuses().checkAmount(Token.BLUE)
        && gemConditions.get("white") <= inventory.getBonuses().checkAmount(Token.WHITE)
        && gemConditions.get("green") <= inventory.getBonuses().checkAmount(Token.GREEN)
        && gemConditions.get("black") <= inventory.getBonuses().checkAmount(Token.BLACK));
    } else {
      return (genericGemCost <= inventory.getBonuses().checkAmount(Token.RED)
        || genericGemCost <= inventory.getBonuses().checkAmount(Token.BLUE)
        || genericGemCost <= inventory.getBonuses().checkAmount(Token.WHITE)
        || genericGemCost <= inventory.getBonuses().checkAmount(Token.GREEN)
        || genericGemCost <= inventory.getBonuses().checkAmount(Token.BLACK));
    }
  }

  /**
   * Getter for this condition's point requirement.
   *
   * @return point threshold
   */
  public int getPoints() {
    return points;
  }
}
