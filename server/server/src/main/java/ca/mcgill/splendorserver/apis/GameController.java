package ca.mcgill.splendorserver.apis;

import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.GameConfigData;
import ca.mcgill.splendorserver.models.GameData;
import ca.mcgill.splendorserver.models.TurnData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.util.ArrayList;
import java.util.HashMap;
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

  private HashMap<String, GameData> games = new HashMap<String, GameData>();
  
  private HashMap<String, Game> gameRegistry = new HashMap<String, Game>();
  private ArrayList<String> playerRegistry = new ArrayList<String>();

  /**
   * Getter for the game.
   *
   * @param gameId the id of the game
   * @return the game data
   */
  @GetMapping(path = {"/GameId", "/GameId/{gameId}"}, produces = "application/json; charset=UTF-8")
  @ResponseBody
  public GameData getGame(@PathVariable(required = false, name = "gameId") String gameId) {
    if (gameId != null) {
      return games.get(gameId);
    } else {
      return null;
    }
  }

  /**
   * Creates a game session.
   *
   * @param game the game data for the game to create
   * @return json of the game data for the created game
   * @throws JsonProcessingException when JSON processing error occurs
   */
  @PostMapping("/Game")
  public String createGame(@RequestBody GameData game) throws JsonProcessingException {

    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    String json = ow.writeValueAsString(game);
    games.put(game.getGameId(), game);
    System.out.println(json);

    return game.getGameId();
  }

  /**
   * Launches game.
   *
   * @param gameId the id of the game
   * @param game the game data for the game with id as above
   * @return the string of JSON game data
   * @throws JsonProcessingException when JSON has a processing error
   */
  @PutMapping(path = {"/GameId/{gameId}"}, consumes = "application/json; charset=UTF-8")
  public String launchGame(@PathVariable String gameId,
      @RequestBody GameData game) throws JsonProcessingException {
    return "";
  }


  /**
   * Registers a new player with server.
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
    
    return true;
  }


  /**
   * Ends turn.
   *
   * @param gameId the id of the game
   * @param turn the game data for the game to create
   * @return success flag
   * @throws JsonProcessingException when JSON processing error occurs
   */
  @PostMapping("/register/{gameId}")
  public boolean updateGame(@PathVariable String gameId,
      @RequestBody TurnData turn) throws JsonProcessingException {

    if (turn == null) {
      return false;
    }

    gameRegistry.get(gameId).updateGame(turn);
    
    return true;
  }
  
  

}
