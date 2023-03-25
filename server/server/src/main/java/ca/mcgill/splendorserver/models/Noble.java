package ca.mcgill.splendorserver.models;

import ca.mcgill.splendorserver.models.board.TokenBank;

import java.io.Serializable;
import java.util.HashMap;
import org.json.simple.JSONObject;

/**
 * Model class for Splendor noble tiles.
 */
public class Noble implements Serializable {

  private static final long serialVersionUID = 2326481723951152262L;
  private int id;
  private int pts;
  private HashMap<String, Integer> cost = new HashMap<String, Integer>();

  /**
   * Constructor for noble from JSONObject.
   *
   * @param obj JSONObject containing noble data
   */
  public Noble(JSONObject obj) {
    id = Integer.parseInt((String) obj.get("id"));
    pts = Integer.parseInt((String) obj.get("points"));
    cost.put(Token.BLUE.toString(), Integer.parseInt((String) obj.get("blue")));
    cost.put(Token.GREEN.toString(), Integer.parseInt((String) obj.get("green")));
    cost.put(Token.RED.toString(), Integer.parseInt((String) obj.get("red")));
    cost.put(Token.WHITE.toString(), Integer.parseInt((String) obj.get("white")));
    cost.put(Token.BLACK.toString(), Integer.parseInt((String) obj.get("black")));
  }

  /**
   * Checks whether the noble will be impressed given an amount of bonuses.
   *
   * @param bonus the bonuses that could earn a noble visit
   * @return whether the noble is impressed by the amount of bonuses
   */
  public boolean impressed(TokenBank bonus) {
    for (Token token : Token.values()) {
      if (token.equals(Token.GOLD)) {
        continue;
      }
      if (bonus.checkAmount(token) < cost.get(token.toString())) {
        return false;
      }
    }
    return true;
  }

  /**
   * Getter for the noble's id.
   *
   * @return the unique identifier for the noble
   */
  public int getId() {
    return id;
  }

  /**
   * Getter for the points associated with acquiring the noble.
   *
   * @return the points associated with acquiring the noble.
   */
  public int getPts() {
    return pts;
  }

  /**
   * Getter for the cost of the noble.
   *
   * @return the counts of the required bonuses
   */
  public HashMap<String, Integer> getCost() {
    return cost;
  }

  /**
   * Setter for the noble id.
   *
   * @param id a unique identifier for the noble tile
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Setter for the points associated with acquiring the noble.
   *
   * @param pts the points associated with acquiring the noble
   */
  public void setPts(int pts) {
    this.pts = pts;
  }

  /**
   * Setter for the cost of the noble.
   *
   * @param cost the counts of the required bonuses
   */
  public void setCost(HashMap<String, Integer> cost) {
    this.cost = cost;
  }
}
