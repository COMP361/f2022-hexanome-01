package ca.mcgill.splendorserver.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Models one game.
 */
public class Game {

  private Deck[] decks = new Deck[6];

  private CardData[][] cardsOnBoard = new CardData[6][];
  private NobleData[] nobles = new NobleData[5];

  private String id;
  private String name;

  private PlayerData[] players = new PlayerData[4];
  private int numOfPlayers;

  private int turnIndex = 0;

  /**
   * Constructs game for debugging.
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
   * @param id     id of game
   * @param config game config data
   */
  public Game(String id, GameConfigData config) {
    this.id = id;
    name = config.getGameName();
    numOfPlayers = Math.min(4, config.getPlayerIds().length);
    for (int i = 0; i < numOfPlayers; i++) {
      players[i] = new PlayerData(config.getPlayerIds()[i]);
      if (players[i].getId().equals(config.getHostId())) {
        turnIndex = i;
      }
    }
    decks[0] = new Deck(4, config.getDeck1());
    decks[1] = new Deck(4, config.getDeck2());
    decks[2] = new Deck(4, config.getDeck3());
    decks[3] = new Deck(2, config.getExDeck1());
    decks[4] = new Deck(2, config.getExDeck2());
    decks[5] = new Deck(2, config.getExDeck3());

    ArrayList<NobleData> tmp = new ArrayList<>(Arrays.asList(config.getAllNobles()));
    Collections.shuffle(tmp);
    for (int i = 0; i < numOfPlayers + 1; i++) {
      nobles[i] = tmp.get(i);
    }

    for (Deck deck : decks) {
      if (deck != null) {
        deck.shuffle();
      }
    }

    for (int i = 0; i < 3; i++) {
      cardsOnBoard[i] = new CardData[4];
    }

    for (int i = 3; i < 6; i++) {
      cardsOnBoard[i] = new CardData[2];
    }

    for (int i = 0; i < 6; i++) {
      cardsOnBoard[i] = decks[i].initialDraw();
    }
  }

  /**
   * Constructor to construct a game from the information provided by the lobby service
   * to start a game (See startGame in GameController).
   *
   * @param id            the id of the game
   * @param startGameData the data provided by the lobby service
   */
  public Game(String id, StartGameData startGameData) {
    this.id = id;
    name = startGameData.getGameServer();
    numOfPlayers = Math.min(4, startGameData.getPlayers().length);
    for (int i = 0; i < numOfPlayers; i++) {
      players[i] = new PlayerData(startGameData.getPlayers()[i].getName());
      if (players[i].getId().equals(startGameData.getCreator())) {
        turnIndex = i;
      }
    }
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
   * Updates game.
   *
   * @param turn turn data
   */
  public void updateGame(TurnData turn) {
    if (turn.getCardTaken() != null) {
      for (int i = 0; i < 6; i++) {
        for (int j = 0; j < cardsOnBoard[i].length; j++) {
          if (Arrays.asList(turn.getCardTaken()).contains(cardsOnBoard[i][j])) {
            cardsOnBoard[i][j] = decks[i].draw();
          }
        }
      }
    }
    if (turn.getNobleTaken() != null) {
      for (int i = 0; i < 5; i++) {
        if (Arrays.asList(turn.getNobleTaken()).contains(nobles[i])) {
          nobles[i] = null;
        }
      }
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

  /**
   * Gets cards on board.
   *
   * @return cards on board
   */
  public CardData[][] getCardsOnBoard() {
    return cardsOnBoard;
  }

  /**
   * Gets nobles.
   *
   * @return nobles
   */
  public NobleData[] getNobles() {
    return nobles;
  }

}
