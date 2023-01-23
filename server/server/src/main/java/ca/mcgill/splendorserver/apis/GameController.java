package ca.mcgill.splendorserver.apis;

import ca.mcgill.splendorserver.Registrator;
import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.GameConfigData;
import ca.mcgill.splendorserver.models.GameData;
import ca.mcgill.splendorserver.models.StartGameData;
import ca.mcgill.splendorserver.models.TurnData;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * Game controller class for the server.
 */
@RestController
public class GameController {
  private final Logger logger;

  private HashMap<String, Game> gameRegistry =
      new HashMap<String, Game>(Map.of("test", new Game()));

  /**
   * Sole constructor.  (For invocation by subclass constructors, typically
   * implicit.)
   */
  public GameController() {
    logger = LoggerFactory.getLogger(GameController.class);
  }

  /**
   * Getter for the game.
   *
   * @param gameId the id of the game
   * @return the game data
   */
  @GetMapping(path = {"/update/{gameId}"}, produces = "application/json; charset=UTF-8")
  @ResponseBody
  public GameData getGame(@PathVariable(required = true, name = "gameId") String gameId) {
    if (gameId == null || !gameRegistry.containsKey(gameId)) {
      System.out.println("Polled null game with ID: " + gameId);
      return null;
    }
    System.out.println("Polled game with ID: " + gameId);

    return new GameData(gameRegistry.get(gameId));
  }

  /**
   * Registers a new game.
   *
   * @param config the game data for the game to create
   * @return success flag
   * @throws JsonProcessingException when JSON processing error occurs
   */
  @PostMapping("/register")
  public boolean registerGame(@RequestBody GameConfigData config) throws JsonProcessingException {

    if (config == null) {
      return false;
    }

    String id = config.getHostId() + "-" + config.getGameName();

    gameRegistry.put(id, new Game(id, config));
    System.out.println("Registered game with ID: " + id);

    return true;
  }


  /**
   * Ends turn.
   *
   * @param gameId the id of the game
   * @param turn   the game data for the game to create
   * @return success flag
   * @throws JsonProcessingException when JSON processing error occurs
   */
  @PostMapping("/endturn/{gameId}")
  public boolean updateGame(@PathVariable String gameId,
                            @RequestBody TurnData turn) throws JsonProcessingException {

    if (turn == null) {
      return false;
    }
    System.out.println(gameRegistry.get(gameId).getCurrentPlayer().getId());
    gameRegistry.get(gameId).updateGame(turn);
    System.out.println(gameRegistry.get(gameId).getCurrentPlayer().getId());
    System.out.println("Ended turn on game with ID: " + gameId);

    return true;
  }

  /**
   * End point for the lobby service to tell the server that the game has started.
   *
   * @param gameId the game that has been started
   * @param startGameData data provided by lobby service about the game
   *                      (name, list of players and saved game(optional))
   */

  @PutMapping(path = "/api/games/{gameId}", consumes = "application/json; charset=UTF-8")
  public void startGame(@PathVariable(required = true, name = "gameId") long gameId,
                        @RequestBody StartGameData startGameData) {
    try {
      if (gameRegistry.containsKey("" + gameId)) {
        throw new IllegalArgumentException();
      }
      Game game = new Game("" + gameId, startGameData);
      gameRegistry.put("" + gameId, game);
      //#TODO add broadcast to all players in the list that game started
    } catch (IllegalArgumentException e) {
      logger.error("Game id provided by lobby service is invalid");
    }

  }


}
