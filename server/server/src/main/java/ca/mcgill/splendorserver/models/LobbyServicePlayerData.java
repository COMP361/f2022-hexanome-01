package ca.mcgill.splendorserver.models;

/**
 * Holds the data for a player in the lobby service format.
 *
 * @author cadin
 */
public class LobbyServicePlayerData {
  private String name;
  private String color;

  /**
   * Constructs the LSPlayer model with the data provided by the lobby service.
   *
   * @param name  Name of the player
   * @param color the chosen color of the player
   */
  public LobbyServicePlayerData(String name, String color) {
    this.name = name;
    this.color = color;
  }

  /**
   * Getter for the name of the player.
   *
   * @return name of player
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the player.
   *
   * @param name name of the player
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Getter for the player color.
   *
   * @return the chosen color of the player
   */
  public String getColor() {
    return color;
  }

  /**
   * Sets the color of the player.
   *
   * @param color the chosen color of the player
   */
  public void setColor(String color) {
    this.color = color;
  }
}
