package ca.mcgill.splendorserver.controllers;

import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.Inventory;
import ca.mcgill.splendorserver.models.Noble;
import ca.mcgill.splendorserver.models.Player;
import ca.mcgill.splendorserver.models.board.Board;
import ca.mcgill.splendorserver.models.board.CardBank;
import ca.mcgill.splendorserver.models.cards.Card;
import ca.mcgill.splendorserver.models.cards.CardLevel;
import ca.mcgill.splendorserver.models.cards.CardType;
import ca.mcgill.splendorserver.models.communicationbeans.ReserveCardData;
import ca.mcgill.splendorserver.models.communicationbeans.SessionData;
import ca.mcgill.splendorserver.models.registries.CardRegistry;
import ca.mcgill.splendorserver.models.registries.NobleRegistry;
import ca.mcgill.splendorserver.models.registries.UnlockableRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

/**
 * This is the controller for all game managing functionality.
 * This classed is used to separate the REST endpoints with the
 * different controller functionality.
 */
@Component
public class GameManager {

  private static HashMap<String, Game> gameRegistry = new HashMap<String, Game>();

  private static HashMap<String, Game> saves = new HashMap<>();

  private CardRegistry cardRegistry = new CardRegistry();
  private NobleRegistry nobleRegistry = new NobleRegistry();
  private UnlockableRegistry unlockRegistry = new UnlockableRegistry();

  /**
   * Provides functionality of launching a game.
   * This function is called by the launchGame endpoint(PUT /api/splendor/{gameId})
   *
   * @param gameId  the unique id of the game
   * @param session the data about the session
   */
  public static void launchGame(String gameId, SessionData session) {
    String saveId = session.getSavegame();

    if (!saveId.equals("")) {
      Game save = saves.get(saveId);
      gameRegistry.put(gameId, save);
      save.setLaunched();
    } else {
      //players
      String[] playersUsernames = session.getPlayers();
      int numberOfPlayers = playersUsernames.length;
      Player[] players = new Player[numberOfPlayers];
      for (int i = 0; i < numberOfPlayers; i++) {
        players[i] = new Player(playersUsernames[i]);
      }
      //variant
      String variant = session.getVariant();
      //creator
      String creator = session.getCreator();
      //create new game
      gameRegistry.put(gameId, new Game(gameId, creator, players, variant));
    }
  }

  /**
   * Returns the game saved in the active game list.
   *
   * @param gameId the id of the game we want to find.
   * @return the game object saved
   */
  public static Game getGame(String gameId) {
    if (gameRegistry.containsKey(gameId)) {
      return gameRegistry.get(gameId);
    } else {
      return null;
    }
  }

  /**
   * Adds a card to a players inventory. does pay for the card.
   *
   * @param game     the game where the player and card reside.
   * @param playerId the player in question.
   * @param cardId   the card the player wishes to acquire.
   * @return the JSONObject response containing the action being done, and choices for user.
   */
  @SuppressWarnings("unchecked")
  public static JSONObject purchaseCard(Game game, String playerId, int cardId) {
    Board board = game.getBoard();
    Card card = CardRegistry.of(cardId);
    Inventory inventory = board.getInventory(playerId);
    JSONObject purchaseResults = new JSONObject();

    if (!inventory.isCostAffordable(card.getCost()) || !acquireCard(card, board, inventory)) {
      return null;
    }
    inventory.payForCard(card);

    purchaseResults.put("action", "none");
    purchaseResults.put("choices", JSONArray.toJSONString(new ArrayList<Noble>()));
    if (card.getType() != CardType.NONE) {
      JSONObject result = OrientManager.handleCard(card, board, inventory);
      String furtherAction = (String) result.get("type");
      String actionOptions = (String) result.get("choices");
      purchaseResults.replace("action", furtherAction);
      purchaseResults.replace("choices", actionOptions);
      purchaseResults.put("noblesVisiting", JSONArray.toJSONString(new ArrayList<Noble>()));
    } else {
      ArrayList<Noble> noblesVisiting = board.getNobles().attemptImpress(inventory);
      purchaseResults.put("noblesVisiting", JSONArray.toJSONString(noblesVisiting));
    }

    return purchaseResults;
  }

  /**
   * Adds a card to a players inventory. does not pay for the card.
   *
   * @param card      the card the player wishes to acquire.
   * @param board     the board on which the card resides.
   * @param inventory the inventory we wish to add the card to.
   * @return whether or not the acquisition was successful.
   */
  public static boolean acquireCard(Card card, Board board, Inventory inventory) {
    if (card == null) {
      return false;
    }
    CardBank cards = board.getCards();
    int pickedUp = cards.draw(card.getId());
    if (pickedUp != card.getId()) {
      return false;
    }
    inventory.addCard(card);
    return true;
  }

  /**
   * Gets the gameboard of the game with the provided ID.
   *
   * @param gameId id of game
   * @return Optional Optional its used in this case as we might not have found the game
   */
  public static Optional<Board> getGameBoard(String gameId) {
    if (gameRegistry.containsKey(gameId)) {
      return Optional.of(gameRegistry.get(gameId).getBoard());
    } else {
      return Optional.empty();
    }
  }

  /**
   * Deletes the given game from the registry.
   *
   * @param gameId the id of the games
   */
  public static void deleteGame(String gameId) {
    gameRegistry.remove(gameId);
  }

  /**
   * Functionality for a player to reserve a card.
   * It makes sure that the player is part of the game
   * they requested from.
   *
   * @param gameId          the game that its being played
   * @param reserveCardData the data receive from the request
   * @return true or false depending if the player can or cannot reserve card
   */
  public boolean reserveCard(String gameId, ReserveCardData reserveCardData) {
    if (gameRegistry.containsKey(gameId)) {
      Game game = gameRegistry.get(gameId);
      Player[] players = game.getPlayers();
      for (Player player : players) {
        if (player.getUsername().equals(reserveCardData.getPlayer())) {
          if (reserveCardData.getDeck().equals("")) {
            int cardToReserve =
                game.getBoard().getCards().draw(reserveCardData.getCard());
            if (cardToReserve == -1) {
              return false;
            }
            player.getInventory().reserve(cardRegistry.of(cardToReserve));
            return true;
          } else {
            CardLevel cardLevel = CardBank.getCardLevelFromString(reserveCardData.getDeck());
            int card = game.getBoard().getCards().drawCardFromDeck(cardLevel);
            player.getInventory().reserve(cardRegistry.of(card));
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * Returns the game registry.
   *
   * @return gameRegistry
   */
  public static HashMap<String, Game> getGameRegistry() {
    return gameRegistry;
  }
}
