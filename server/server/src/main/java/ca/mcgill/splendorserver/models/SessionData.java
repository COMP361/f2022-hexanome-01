package ca.mcgill.splendorserver.models;

/**
 * Stores and manages session data.
 */
public class SessionData {
  private String sessionName;
  private int maxPlayers;
  private LobbyPlayer[] playerList;
  
  /**
   * Gets session name.
   *
   * @return Session name
   */
  public String getSessionName() {
    return sessionName;
  }
  
  /**
   * Sets session name.
   *
   * @param Session name
   */
  public void setSessionName(String sessionName) {
    this.sessionName = sessionName;
  }
  
  /**
   * Gets max players.
   *
   * @return max players
   */
  public int getMaxPlayers() {
    return maxPlayers;
  }
  
  /**
   * Sets max players.
   *
   * @param max players
   */
  public void setMaxPlayers(int maxPlayers) {
    this.maxPlayers = maxPlayers;
  }
  
  /**
   * Gets player list from lobby.
   *
   * @return list of players
   */
  public LobbyPlayer[] getPlayerList() {
    return playerList;
  }
  
  /**
   * Sets player list from lobby.
   *
   * @param list of players
   */
  public void setPlayerList(LobbyPlayer[] playerList) {
    this.playerList = playerList;
  }
}
