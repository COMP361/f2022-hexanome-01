package ca.mcgill.splendorserver.models;

import java.util.LinkedList;

/**
 * Stores and manages LobbyService session data.
 * Mainly used upon launching a session or otherwise communicating with LS.
 */
public class SessionData {

  private String creator;
  private String gameServer;
  private LinkedList<LobbyServicePlayerData> players;
  private String savegame;


  /**
   * Constructor used to create a new session data.
   * Used to create session data provided by Lobby Service
   *
   * @param creator    creator of the session
   * @param gameServer variant of the game
   * @param players    players in this session
   * @param savegame   this is the optional savegame if the game launches from save
   */
  public SessionData(String creator, String gameServer,
                     LinkedList<LobbyServicePlayerData> players, String savegame) {
    this.creator = creator;
    this.gameServer = gameServer;
    this.players = players;
    this.savegame = savegame;
  }

  /**
   * Getter for the players in the session.
   *
   * @return an array of four PlayerData
   */
  public String[] getPlayers() {
    String[] playersNames = new String[this.players.size()];
    int count = 0;
    for (LobbyServicePlayerData player : players) {
      playersNames[count] = player.getName();
      count++;
    }
    return playersNames;
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

  /**
   * Setter for creator of the session.
   *
   * @param creator name of user who created the session
   */
  public void setCreator(String creator) {
    this.creator = creator;
  }

  /**
   * Setter for the variant used in the session.
   *
   * @param gameServer variant of session
   */
  public void setGameServer(String gameServer) {
    this.gameServer = gameServer;
  }

  /**
   * Setter for the players in the session.
   *
   * @param players list of players in the session
   */
  public void setPlayers(
      LinkedList<LobbyServicePlayerData> players) {
    this.players = players;
  }

  /**
   * Setters for the save game functionality.
   *
   * @param savegame save game to launch session from
   */
  public void setSavegame(String savegame) {
    this.savegame = savegame;
  }
}
