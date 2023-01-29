package ca.mcgill.splendorserver.models;

/**
 * Stores and manages LobbyService session data.
 * Mainly used upon launching a session or otherwise communicating with LS.
 */
public class SessionData {

  private String creator;
  private String gameServer;
  private LobbyServicePlayerData[] players;
  private String savegame;

  private class LobbyServicePlayerData {

    private String name;
    private String preferredColour;
  }

  /**
   * Essentially unused constructor for SessionData.
   */
  public SessionData() {

  }

  /**
   * Getter for the players in the session.
   *
   * @return an array of four PlayerData
   */
  public PlayerData[] getPlayers() {
    PlayerData[] playerData = new PlayerData[4];
    for (int i = 0; i < players.length; i++) {
      playerData[i] = new PlayerData(players[i].name);
    }
    return playerData;
  }

  /**
   * Getter for the savegame.
   *
   * @return the savegame id string
   */
  public String getSavegame() {
    return savegame;
  }

}
