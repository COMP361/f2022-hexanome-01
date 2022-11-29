package ca.mcgill.splendorserver.models;

/**
 * Stores and manages Splendor game data.
 */
public class GameData {

  private String gameId;
  private PlayerData currentPlayer;
  private CardData[] row1;
  private CardData[] row2;
  private CardData[] row3;
  private CardData[] exRow1;
  private CardData[] exRow2;
  private CardData[] exRow3;
  private NobleData[] nobles;
  private PlayerData[] players;
  
  /**
   * Constructor.
   *
   * @param currentGame game to construct off of
   */
  public GameData(Game currentGame) {
    gameId = currentGame.getId();
    currentPlayer = currentGame.getCurrentPlayer();
    CardData[][] cardsOnBoard = currentGame.getCardsOnBoard();
    row1 = cardsOnBoard[0];
    row2 = cardsOnBoard[1];
    row3 = cardsOnBoard[2];
    exRow1 = cardsOnBoard[3];
    exRow2 = cardsOnBoard[4];
    exRow3 = cardsOnBoard[5];
    
    nobles = currentGame.getNobles();
    
    players = currentGame.getPlayers();
  }

  /**
   * Setter for nobles.
   *
   * @param nobles array of noble data
   */
  public void setNobles(NobleData[] nobles) {
    this.nobles = nobles;
  }

  /**
   * Setter for players.
   *
   * @param players array of player data
   */
  public void setPlayers(PlayerData[] players) {
    this.players = players;
  }

  /**
   * Getter for nobles.
   *
   * @return array of noble data
   */
  public NobleData[] getNobles() {
    return nobles;
  }

  /**
   * Getter for players.
   *
   * @return array of player data
   */
  public PlayerData[] getPlayers() {
    return players;
  }

  /**
   * Getter for game id.
   *
   * @return game id as string
   */
  public String getGameId() {
    return gameId;
  }

  /**
   * Setter for game id.
   *
   * @param gameId game id as string
   */
  public void setGameId(String gameId) {
    this.gameId = gameId;
  }

  /**
   * Getter for row 1 (of green cards).
   *
   * @return data for the green cards
   */
  public CardData[] getRow1() {
    return row1;
  }

  /**
   * Setter for row 1 (of green cards).
   *
   * @param row1 data for the green cards
   */
  public void setRow1(CardData[] row1) {
    this.row1 = row1;
  }

  /**
   * Getter for row 2 (of yellow cards).
   *
   * @return data for yellow cards
   */
  public CardData[] getRow2() {
    return row2;
  }

  /**
   * Setter for row 2 (of yellow cards).
   *
   * @param row2 data for yellow cards
   */
  public void setRow2(CardData[] row2) {
    this.row2 = row2;
  }

  /**
   * Getter for row 3 (of blue cards).
   *
   * @return data for blue cards
   */
  public CardData[] getRow3() {
    return row3;
  }

  /**
   * Setter for row 3 (of blue cards).
   *
   * @param row3 data for blue cards
   */
  public void setRow3(CardData[] row3) {
    this.row3 = row3;
  }

  /**
   * Getter for row 1 of red cards.
   *
   * @return data for red cards in row 1
   */
  public CardData[] getExRow1() {
    return exRow1;
  }

  /**
   * Setter for row 1 of red cards.
   *
   * @param exRow1 data for red cards in row 1
   */
  public void setExRow1(CardData[] exRow1) {
    this.exRow1 = exRow1;
  }

  /**
   * Getter for row 2 of red cards.
   *
   * @return data for red cards in row 2
   */
  public CardData[] getExRow2() {
    return exRow2;
  }

  /**
   * Setter for row 2 of red cards.
   *
   * @param exRow2 data for red cards in row 2
   */
  public void setExRow2(CardData[] exRow2) {
    this.exRow2 = exRow2;
  }

  /**
   * Getter for row 3 of red cards.
   *
   * @return data for red cards in row 3.
   */
  public CardData[] getExRow3() {
    return exRow3;
  }

  /**
   * Setter for row 3 of red cards.
   *
   * @param exRow3 data for red cards in row 3.
   */
  public void setExRow3(CardData[] exRow3) {
    this.exRow3 = exRow3;
  }

  /**
   * Getter for current player.
   *
   * @return current player
   */
  public PlayerData getCurrentPlayer() {
    return currentPlayer;
  }

  /**
   * Setter for current player.
   *
   * @param currentPlayer current player
   */
  public void setCurrentPlayer(PlayerData currentPlayer) {
    this.currentPlayer = currentPlayer;
  }
}
