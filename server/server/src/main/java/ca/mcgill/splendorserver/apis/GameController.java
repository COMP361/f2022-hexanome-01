package ca.mcgill.splendorserver.apis;

import ca.mcgill.splendorserver.models.Card;
import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.Noble;
import ca.mcgill.splendorserver.models.SessionData;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

  private HashMap<String, Game> gameRegistry = new HashMap<String, Game>();
  private HashMap<String, Game> saves = new HashMap<>();
  
  private static HashMap<String, Card> cardRegistry = new HashMap<>();
  private static HashMap<String, Noble> nobleRegistry = new HashMap<>();

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
   * @param data   the game data of the take tokens action
   * @return success flag
   * @throws JsonProcessingException when JSON processing error occurs
   */
  @PostMapping("/api/action/takeTokens/{gameId}")
  public ResponseEntity<HttpStatus> takeTokensAction(@PathVariable String gameId,
                            @RequestBody JSONObject data) throws JsonProcessingException {
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
                            @RequestBody JSONObject data) throws JsonProcessingException {
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
                            @RequestBody JSONObject data) throws JsonProcessingException {
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
                            @RequestBody JSONObject data) throws JsonProcessingException {
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
   * @param gameId the id of the game
   * @param session the session data for the game to create
   * @throws JsonProcessingException when JSON processing error occurs
   */
  @PutMapping("/api/splendor/{gameId}")
  public void launchGame(@PathVariable(required = true, name = "gameId") String gameId,
                            @RequestBody SessionData session) {

    String saveId = session.getSavegame();
    String[] playerList = session.getPlayers();
    String variant = session.getVariant();
    String creator = session.getCreator();

    Game save = saves.get(saveId);

    if (save != null) {
      gameRegistry.put(saveId, save);
      save.setLaunched();
      Game game = save;
    }
    else {
      Game game = new Game(saveId, variant, playerList, creator);
    }
  }

  /**
   * Getter for the noble registry.
   *
   * @return all noble tiles
   */
  public static HashMap<String, Noble> getAllNobles() {
    return nobleRegistry;
  }
}


