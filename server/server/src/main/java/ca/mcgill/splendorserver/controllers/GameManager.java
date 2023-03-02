package ca.mcgill.splendorserver.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.stereotype.Component;

import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.Inventory;
import ca.mcgill.splendorserver.models.Noble;
import ca.mcgill.splendorserver.models.SessionData;
import ca.mcgill.splendorserver.models.board.Board;
import ca.mcgill.splendorserver.models.board.CardBank;
import ca.mcgill.splendorserver.models.cards.Card;
import ca.mcgill.splendorserver.models.registries.CardRegistry;
import ca.mcgill.splendorserver.models.registries.NobleRegistry;
import ca.mcgill.splendorserver.models.registries.UnlockableRegistry;

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
      String[] players = session.getPlayers();
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
  
  public static ArrayList<Noble> purchaseCard(Game game, String playerId, int cardId) {
	  Board board = game.getBoard();
	  Card card = CardRegistry.of(cardId);
	  Inventory inventory = board.getInventory(playerId);
	  
	  if(!acquireCard(card, board, inventory)) {
		  return null;
	  }
	  
	  
	  
	  ArrayList<Noble> noblesVisiting = board.getNobles().attemptImpress(inventory);
	  
	  return noblesVisiting;
  }
  
  /**
   * Adds a card to a players inventory. does not pay for the card.
   *
   * @param card the card the player wishes to acquire.
   * @param board the board on which the card resides.
   * @param inventory the inventory we wish to add the card to.
   * @return whether or not the acquisition was successful.
   */
  public static boolean acquireCard(Card card, Board board, Inventory inventory) {
	  if (card == null)
		  return false;
	  
	  CardBank cards = board.getCards();
	  int pickedUp = cards.draw(card.getId());
	  if (pickedUp != card.getId()) 
		  return false;
	  
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
   * Returns the game registry.
   *
   * @return gameRegistry
   */
  public static HashMap<String, Game> getGameRegistry() {
    return gameRegistry;
  }
}
