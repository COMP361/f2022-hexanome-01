package ca.mcgill.splendorserver.controllers;

import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.SessionData;
import ca.mcgill.splendorserver.models.board.Board;
import ca.mcgill.splendorserver.models.registries.CardRegistry;
import ca.mcgill.splendorserver.models.registries.NobleRegistry;
import ca.mcgill.splendorserver.models.registries.UnlockableRegistry;
import java.util.HashMap;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * This is the controller for all game managing functionality.
 * This classed is used to separate the REST endpoints with the
 * different controller functionality.
 */
@Component
public class GameManager {

  private HashMap<String, Game> gameRegistry = new HashMap<String, Game>();

  private HashMap<String, Game> saves = new HashMap<>();

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
  public void launchGame(String gameId, SessionData session) {
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
      gameRegistry.put(gameId, new Game(gameId, variant, players, creator));
    }
  }

  /**
   * Returns the game saved in the active game list.
   *
   * @param gameId the id of the game we want to find.
   * @return the game object saved
   */
  public Game getGame(String gameId) {
    if (gameRegistry.containsKey(gameId)) {
      return gameRegistry.get(gameId);
    } else {
      return null;
    }
  }

  /**
   * Gets the gameboard of the game with the provided ID.
   *
   * @param gameId id of game
   * @return Optional Optional its used in this case as we might not have found the game
   */
  public Optional<Board> getGameBoard(String gameId) {
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
  public void deleteGame(String gameId) {
    gameRegistry.remove(gameId);
  }

  /**
   * Returns the game registry.
   *
   * @return gameRegistry
   */
  public HashMap<String, Game> getGameRegistry() {
    return gameRegistry;
  }
}
