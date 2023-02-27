package ca.mcgill.splendorserver.apis;

import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.SessionData;
import ca.mcgill.splendorserver.models.registries.CardRegistry;
import ca.mcgill.splendorserver.models.registries.NobleRegistry;
import ca.mcgill.splendorserver.models.registries.UnlockableRegistry;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Game controller class for the server.
 */
@RestController
public class GameController {

  private final Logger logger;

  private HashMap<String, Game> gameRegistry =
      new HashMap<String, Game>(Map.of("test",
          new Game("testId", "testPlayer1", new String[] {"testPlayer1"}, "splendor")));

  private HashMap<String, Game> saves = new HashMap<>();
  //registries might be unnecessary
  private CardRegistry cardRegistry = new CardRegistry();
  private NobleRegistry nobleRegistry = new NobleRegistry();
  private UnlockableRegistry unlockRegistry = new UnlockableRegistry();

  /**
   * Sole constructor.
   * (For invocation by subclass constructors, typically implicit.)
   */
  public GameController() {
    logger = LoggerFactory.getLogger(GameController.class);
  }

  /**
   * Takes token.
   *
   * @param gameId the id of the game
   * @return success flag
   * @throws JsonProcessingException when JSON processing error occurs
   */
  @GetMapping("/api/games/{gameId}/board")
  public ResponseEntity<String> getBoard(@PathVariable String gameId)
      throws JsonProcessingException {
    if (gameRegistry.containsKey(gameId)) {
      return ResponseEntity.ok(gameRegistry.get(gameId).getBoardJson());
    } else {
      return ResponseEntity.ok("");
    }
  }

  /**
   * Takes token.
   *
   * @param gameId the id of the game
   * @param data   the game data of the take tokens action
   * @return success flag
   * @throws JsonProcessingException when JSON processing error occurs
   */
  @PostMapping("/api/action/takeTokens/{gameId}")
  public ResponseEntity<HttpStatus> takeTokensAction(@PathVariable String gameId,
                                                     @RequestBody JSONObject data)
      throws JsonProcessingException {
    String playerId = (String) data.get("playerId");

    return ResponseEntity.ok(HttpStatus.OK);
  }

  /**
   * Takes token.
   *
   * @param gameId the id of the game
   * @param data   the game data of the take tokens action
   * @return success flag
   * @throws JsonProcessingException when JSON processing error occurs
   */
  @PostMapping("/api/action/performPurchaseRegularCard/{gameId}")
  public ResponseEntity<HttpStatus> performPurchaseRegularCard(@PathVariable String gameId,
                                                               @RequestBody JSONObject data)
      throws JsonProcessingException {
    String playerId = (String) data.get("playerId");

    return ResponseEntity.ok(HttpStatus.OK);
  }

  /**
   * Takes token.
   *
   * @param gameId the id of the game
   * @param data   the game data of the take tokens action
   * @return success flag
   * @throws JsonProcessingException when JSON processing error occurs
   */
  @PostMapping("/api/action/performPurchaseRedLevelThreeDevelopmentCard/{gameId}")
  public ResponseEntity<HttpStatus> performPurchaseRedLevelThreeDevelopmentCard(
      @PathVariable String gameId, @RequestBody JSONObject data) throws JsonProcessingException {
    String playerId = (String) data.get("playerId");

    return ResponseEntity.ok(HttpStatus.OK);
  }

  /**
   * Takes token.
   *
   * @param gameId the id of the game
   * @param data   the game data of the take tokens action
   * @return success flag
   * @throws JsonProcessingException when JSON processing error occurs
   */
  @PostMapping("/api/action/reserveCard/{gameId}")
  public ResponseEntity<HttpStatus> reserveCardAction(@PathVariable String gameId,
                                                      @RequestBody JSONObject data)
      throws JsonProcessingException {
    String playerId = (String) data.get("playerId");

    return ResponseEntity.ok(HttpStatus.OK);
  }

  /**
   * Takes token.
   *
   * @param gameId the id of the game
   * @param data   the game data of the take tokens action
   * @return success flag
   * @throws JsonProcessingException when JSON processing error occurs
   */
  @PostMapping("/api/action/claimNoble/{gameId}")
  public ResponseEntity<HttpStatus> claimNobleAction(@PathVariable String gameId,
                                                     @RequestBody JSONObject data)
      throws JsonProcessingException {
    String playerId = (String) data.get("playerId");

    return ResponseEntity.ok(HttpStatus.OK);
  }


  /**
   * Removes a game from the server upon lobby service request.
   *
   * @param gameId the id of the game we want to delete
   */
  @DeleteMapping(path = "/api/splendor/{gameId}")
  public void deleteGame(@PathVariable(required = true, name = "gameId") long gameId) {
    gameRegistry.remove("" + gameId);
  }

  /**
   * Launches game.
   *
   * @param gameId  the id of the game
   * @param sessionString the session data for the game to create
   * @throws JsonProcessingException when JSON processing error occurs
   */
  @PutMapping("/api/games/{gameId}")
  public void launchGame(
      @PathVariable(required = true, name = "gameId") String gameId,
      @RequestBody String sessionString) throws JsonProcessingException {

    JSONObject session = (JSONObject) JsonHandler.decodeJsonRequest(sessionString);
    String saveId = (String) session.get("savegame");

    if (!saveId.isEmpty()) {
      Game save = saves.get(saveId);
      gameRegistry.put(gameId, save);
      save.setLaunched();
    } else {
      //players
      JSONArray players = (JSONArray) session.get("players");
      ArrayList<String> usernamesList = new ArrayList<>();
      for (Object player : players) {
        usernamesList.add((String) ((JSONObject) player).get("name"));
      }
      String[] usernames = usernamesList.toArray(new String[0]);
      //variant
      String variant = (String) session.get("gameServer");
      //creator
      String creator = (String) session.get("creator");
      //create new game
      gameRegistry.put(gameId, new Game(gameId, variant, usernames, creator));
    }
  }
}


