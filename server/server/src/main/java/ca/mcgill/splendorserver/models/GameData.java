package ca.mcgill.splendorserver.models;

/**
 * Stores and manages Splendor game data.
 */
public class GameData {

  private String gameId;
  private int currentPlayer;
  private CardData[] row1;
  private CardData[] row2;
  private CardData[] row3;
  private CardData[] exRow1;
  private CardData[] exRow2;
  private CardData[] exRow3;
  private NobleData[] nobles;
  private PlayerData[] players;
    
  public void setNobles(NobleData[] nobles) {
    this.nobles = nobles;
  }

  public void setPlayers(PlayerData[] players) {
    this.players = players;
  }

    
  public NobleData[] getNobles() {
    return nobles;
  }
    
  public PlayerData[] getPlayers() {
    return players;
  }
    
  public String getGameId() {
    return gameId;
  }

  public void setGameId(String gameId) {
    this.gameId = gameId;
  }

  public CardData[] getRow1() {
    return row1;
  }

  public void setRow1(CardData[] row1) {
    this.row1 = row1;
  }

  public CardData[] getRow2() {
    return row2;
  }

  public void setRow2(CardData[] row2) {
    this.row2 = row2;
  }

  public CardData[] getRow3() {
    return row3;
  }

  public void setRow3(CardData[] row3) {
    this.row3 = row3;
  }

  public CardData[] getExRow1() {
    return exRow1;
  }

  public void setExRow1(CardData[] exRow1) {
    this.exRow1 = exRow1;
  }

  public CardData[] getExRow2() {
    return exRow2;
  }

  public void setExRow2(CardData[] exRow2) {
    this.exRow2 = exRow2;
  }

  public CardData[] getExRow3() {
    return exRow3;
  }

  public void setExRow3(CardData[] exRow3) {
    this.exRow3 = exRow3;
  }
  
  public int getCurrentPlayer() {
    return currentPlayer;
  }

  public void setCurrentPlayer(int currentPlayer) {
    this.currentPlayer = currentPlayer;
  }
}
