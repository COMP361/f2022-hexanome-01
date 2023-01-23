package ca.mcgill.splendorserver.models;

/**
 * Model of the data received from the lobby service to start a game.
 *
 * @author Cadin Londono
 * @date January 2023
 */
public class StartGameData {
  private String creator;
  private String gameServer;
  private LobbyServicePlayerData[] players;
  private String saveGame;

  /**
   * Constructor for the data received from the lobby service to start a game.
   *
   * @param creator    the host of the session
   * @param gameServer which server it is on
   * @param players    a list of the players in the game
   * @param saveGame   (Optional) information about the saved game if applicable
   */
  public StartGameData(String creator, String gameServer,
                       LobbyServicePlayerData[] players, String saveGame) {
    this.creator = creator;
    this.gameServer = gameServer;
    this.players = players;
    this.saveGame = saveGame;
  }

  /**
   * Getter for the creator of the game/session.
   *
   * @return name of the player
   */
  public String getCreator() {
    return creator;
  }

  /**
   * Setter for the creator of the game.
   *
   * @param creator name of the creator/host
   */
  public void setCreator(String creator) {
    this.creator = creator;
  }

  /**
   * Getter for the game server of the game.
   *
   * @return the game server of the game
   */
  public String getGameServer() {
    return gameServer;
  }

  /**
   * Setter for the game server of the game.
   *
   * @param gameServer the game server of the game
   */
  public void setGameServer(String gameServer) {
    this.gameServer = gameServer;
  }

  /**
   * Getter of all the players in the game/session.
   *
   * @return list of players of the game
   */
  public LobbyServicePlayerData[] getPlayers() {
    return players;
  }

  /**
   * Setter for the list of player of the game/session.
   *
   * @param players list of player in the lobby service format
   */
  public void setPlayers(LobbyServicePlayerData[] players) {
    this.players = players;
  }

  /**
   * Getter of the save game of the session.
   *
   * @return the save game of the session
   */
  public String getSaveGame() {
    return saveGame;
  }

  /**
   * Sets the save game of the game/session.
   *
   * @param saveGame the save game
   */
  public void setSaveGame(String saveGame) {
    this.saveGame = saveGame;
  }


}
