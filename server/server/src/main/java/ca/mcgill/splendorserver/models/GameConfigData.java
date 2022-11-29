package ca.mcgill.splendorserver.models;


/**
 * Game config data.
 * 
 */
public class GameConfigData {
  
  private String gameName;
  private String hostId;
  private String[] playerIds;

  /**
   * Data constructor.
   *
   * @param gameName name of game
   * @param hostId id of host
   * @param playerIds ids of players
   */
  public GameConfigData(String gameName, String hostId, String[] playerIds) {
    this.gameName = gameName;
    this.hostId = hostId;
    this.playerIds = playerIds;
  }
  
  /**
   * Gets game name.
   * 
   * @return gameName
   */
  public String getGameName() {
    return gameName;
  }
  
  /**
   * Gets host ID.
   * 
   * @return hostId
   */
  public String getHostID() {
    return hostId;
  }
  
  /**
   * Gets player IDs.
   * 
   * @return hostId
   */
  public String[] getPlayerIDs() {
    return playerIds;
  }
}
