package ca.mcgill.splendorserver.models;

/**
 * Holds the data for each player as stored on client as well.
 */
public class PlayerData {
  private String id;
  private int points;

  /**
   * Constructs player.
   *
   * @param id player id
   */
  public PlayerData(String id) {
    this.id = id;
    this.points = 0;
  }
  
  /**
   * Gets ID.
   *
   * @return id gotten
   */
  public String getId() {
    return this.id;
  }
  
  /**
   * Gets points.
   *
   * @return points gotten
   */
  public int getPoints() {
    return this.points;
  }
  
  /**
   * Sets points.
   *
   * @param points to be set
   */
  public void setPoints(int points) {
    this.points = points;
  }
}
