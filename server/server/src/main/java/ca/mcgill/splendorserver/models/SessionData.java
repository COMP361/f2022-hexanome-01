package ca.mcgill.splendorserver.models;

public class SessionData {
  private String sessionName;
  private int maxPlayers;
  private LobbyPlayer[] playerList;

  public String getSessionName() {
    return sessionName;
  }

  public void setSessionName(String sessionName) {
    this.sessionName = sessionName;
  }

  public int getMaxPlayers() {
    return maxPlayers;
  }

  public void setMaxPlayers(int maxPlayers) {
    this.maxPlayers = maxPlayers;
  }

  public LobbyPlayer[] getPlayerList() {
    return playerList;
  }

  public void setPlayerList(LobbyPlayer[] playerList) {
    this.playerList = playerList;
  }
}
