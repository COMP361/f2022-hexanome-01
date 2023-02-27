package ca.mcgill.splendorserver.models;

/**
 * Stores and manages LobbyService session data.
 * Mainly used upon launching a session or otherwise communicating with LS.
 */
public class SessionData {

  private String creator;
  private String gameServer;
  private LobbyServicePlayerData[] playersLobbyService;
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
  public String[] getPlayers() {
    String[] players = new String[playersLobbyService.length];
    for (int i = 0; i < playersLobbyService.length; i++) {
      players[i] = playersLobbyService[i].name;
    }
    return players;
  }

  /**
   * Getter for the savegame.
   *
   * @return the savegame id string
   */
  public String getSavegame() {
    return savegame;
  }

  /**
   * Getter for the game variant.
   *
   * @return the game variant
   */
  public String getVariant() {
    return gameServer;
  }

  /**
   * Getter for the session creator.
   *
   * @return the session creator
   */
  public String getCreator() {
    return creator;
  }
}
