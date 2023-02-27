package ca.mcgill.splendorserver.controllers;

import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.SessionData;
import ca.mcgill.splendorserver.models.registries.CardRegistry;
import ca.mcgill.splendorserver.models.registries.NobleRegistry;
import ca.mcgill.splendorserver.models.registries.UnlockableRegistry;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * This is the controller for all game managing functionality.
 * This classed is used to separate the REST endpoints with the
 * different controller functionality.
 */
@Component
public class GameManager {
  private HashMap<String, Game> gameRegistry =
      new HashMap<String, Game>(Map.of("test",
          new Game("testId", "testPlayer1", new String[] {"testPlayer1"}, "splendor")));

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
   * @return a game object
   */
  public Game launchGame(String gameId, SessionData session) {
    String saveId = session.getSavegame();
    String[] playerList = session.getPlayers();
    String variant = session.getVariant();
    String creator = session.getCreator();
    //We get save if we have a saveId if not we create new game object
    Game save = saves.get(saveId);

    if (save != null) {
      gameRegistry.put(saveId, save);
      save.setLaunched();
      return save;
    } else {
      return new Game(saveId, variant, playerList, creator);
    }
  }

  /**
   * Returns the game saved in the active game list
   *
   * @param gameId the id of the game we want to find.
   * @return the game object saved
   */
  public Game getGame(String gameId) {
    return gameRegistry.get(gameId);
  }
}
