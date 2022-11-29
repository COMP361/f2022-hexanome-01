package ca.mcgill.splendorserver.models;


/**
 * Models one game.
 * 
 */
public class Game {

  private Deck[] decks = new Deck[6];
  private boolean[] nobles = new boolean[5];
  
  private String id;
  private String name;
  
  private Player[] players = new Player[4];
  private int numOfPlayers;
  
  private int turnIndex = 0;
  
  /**
   * Constructs game.
   *
   * @param id id of game
   * @param config game config data
   */
  public Game(String id, GameConfigData config) {
    this.id = id;
    name = config.getGameName();
    numOfPlayers = config.getPlayerIDs().length;
    for (int i = 0; i < numOfPlayers; i++) {
      players[i] = new Player(config.getPlayerIDs()[i]);
      if (players[i].getId().equals(config.getHostID())) turnIndex = i;
    }
    for (Deck deck : decks) {
      deck.shuffle();
    }
    for (int i = 0; i < 3; i++) {
      decks[i] = new Deck(4);
    }
    for (int i = 3; i < 6; i++) {
      decks[i] = new Deck(2);
    }
  }
  
  public void updateGame(TurnData turn) {
	  if (turn.getRowCardTaken() != -1) {
		  decks[turn.getRowCardTaken()].draw();
	  }
	  if (turn.getNobleTaken() != -1) {
		  nobles[turn.getNobleTaken()] = false;
	  }
  }

}
