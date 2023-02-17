package ca.mcgill.splendorserver.models;

import java.util.HashMap;

/**
 * Model class for Splendor noble tiles.
 */
public class Noble {

  private int id;
  private int pts;
  private HashMap<String, Integer> cost;

  /**
   * Sole constructor (for invocation by subclass constructors, typically implicit.)
   */
  public Noble() {

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
