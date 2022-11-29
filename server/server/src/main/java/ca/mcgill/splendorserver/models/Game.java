package ca.mcgill.splendorserver.models;


/**
 * Models one game.
 * 
 */
public class Game {

  private Deck[] decks = new Deck[6];
  
  private CardData [][] cardsOnBoard = new CardData [6][];
  private NobleData[] nobles = new NobleData[5];
  
  private String id;
  private String name;
  
  private PlayerData[] players = new PlayerData[4];
  private int numOfPlayers;
  
  private int turnIndex = 0;
  
  /**
   * Constructs game for debugging.
   *
   */
  public Game() {
    this.id = "test";
    this.name = "Test Game";
    this.numOfPlayers = 2;
    this.players[0] = new PlayerData("Jeremy");
    this.players[1] = new PlayerData("Josh");
    for (int i = 0; i < 3; i++) {
      decks[i] = new Deck(4);
    }
    for (int i = 3; i < 6; i++) {
      decks[i] = new Deck(2);
    }
    for (Deck deck : decks) {
      deck.shuffle();
    }
  }
  
  /**
   * Constructs game.
   *
   * @param id id of game
   * @param config game config data
   */
  public Game(String id, GameConfigData config) {
    this.id = id;
    name = config.getGameName();
    numOfPlayers = config.getPlayerIds().length;
    for (int i = 0; i < numOfPlayers; i++) {
      players[i] = new PlayerData(config.getPlayerIds()[i]);
      if (players[i].getId().equals(config.getHostId())) {
        turnIndex = i;
      }
    }
    for (Deck deck : decks) {
      deck.shuffle();
    }
  }
  
  /**
   * Updates game.
   *
   * @param turn turn data
   */
  public void updateGame(TurnData turn) {
    if (turn.getRowCardTaken() != -1) {
      int i = turn.getRowCardTaken();
      cardsOnBoard[i][turn.getColCardTaken()] = decks[i].draw();
    }
    if (turn.getNobleTaken() != -1) {
      nobles[turn.getNobleTaken()] = null;
    }
    turnIndex = (turnIndex + 1) % numOfPlayers;
  }
  
  /**
   * Gets game id.
   *
   * @return game id
   */
  public String getId() {
    return this.id;
  }
  
  /**
   * Gets all players.
   *
   * @return all players
   */
  public PlayerData[] getPlayers() {
    return this.players;
  }
  
  /**
   * Gets current player.
   *
   * @return current players
   */
  public PlayerData getCurrentPlayer() {
    return this.players[this.turnIndex];
  }

}
