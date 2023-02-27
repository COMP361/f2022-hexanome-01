package ca.mcgill.splendorserver.models;

import java.util.Optional;

/**
 * Stores and manages LobbyService session data.
 * Mainly used upon launching a session or otherwise communicating with LS.
 */
public class SessionData {

  private String creator;
  private String gameServer;
  private LobbyServicePlayerData[] playersLobbyService;
  private String savegame;


  /**
   * Used to match lobby service model of providing
   * info about players in this session.
   */
  public static class LobbyServicePlayerData {

    private String name;
    private String preferredColour;

    /**
     * Constructor for creating a lobbyServicePlayerData object.
     *
     * @param name            name of player
     * @param preferredColour prefered colour
     */
    public LobbyServicePlayerData(String name, String preferredColour) {
      this.name = name;
      this.preferredColour = preferredColour;
    }
  }


  /**
   * Constructor used to create a new session data.
   * Used to create session data provided by Lobby Service
   *
   * @param creator             creator of the session
   * @param gameServer          variant of the game
   * @param playersLobbyService players in this session
   * @param savegame            this is the optional savegame if the game launches from save
   */
  public SessionData(String creator, String gameServer,
                     LobbyServicePlayerData[] playersLobbyService, String savegame) {
    this.creator = creator;
    this.gameServer = gameServer;
    this.playersLobbyService = playersLobbyService;
    this.savegame = savegame;
  }

  /**
   * Getter for the players in the session.
   *
   * @return an array of four PlayerData
   */
  public String[] getPlayers() {
    String[] players = new String[4];
    for (int i = 0; i < playersLobbyService.length; i++) {
      players[i] = playersLobbyService[i].name;
    }
    return players;
  }

  /**
   * Getter for the savegame. (Optional)
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
