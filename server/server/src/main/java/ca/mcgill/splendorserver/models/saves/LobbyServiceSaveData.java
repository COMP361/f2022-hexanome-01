package ca.mcgill.splendorserver.models.saves;

import ca.mcgill.splendorserver.models.Game;

/**
 * Information required by the LobbyService for saving a game.
 */
public class LobbyServiceSaveData {

  private String gamename;
  private String[] players;
  private String savegameid;

  /**
   * Parametric constructor.
   *
   * @param gamename the game variant of the game being saved
   * @param players the players in the game being saved
   * @param savegameid the id of the save
   */
  public LobbyServiceSaveData(String gamename, String[] players, String savegameid) {
    this.setGamename(gamename);
    this.setPlayers(players);
    this.setSavegameid(savegameid);
  }

  /**
   * Parametric constructor.
   *
   * @param game the game to save from which the required information will be extracted
   */
  public LobbyServiceSaveData(Game game) {
    this.setGamename(game.getVariant());
    this.setPlayers((String[]) game.playerIdSet().toArray());
    this.setSavegameid(game.getId());
  }

  public String getGamename() {
    return gamename;
  }

  public void setGamename(String gamename) {
    this.gamename = gamename;
  }

  public String[] getPlayers() {
    return players;
  }

  public void setPlayers(String[] players) {
    this.players = players;
  }

  public String getSavegameid() {
    return savegameid;
  }

  public void setSavegameid(String savegameid) {
    this.savegameid = savegameid;
  }

}
