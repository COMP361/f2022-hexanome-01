package ca.mcgill.splendorserver.apis;

import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.GameConfigData;
import ca.mcgill.splendorserver.models.GameData;
import ca.mcgill.splendorserver.models.TurnData;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * Game controller class for the server.
 */
@RestController
public class GameController {
  
  private HashMap<String, Game> gameRegistry = 
      new HashMap<String, Game>(Map.of("test", new Game()));

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
      return null;
    }
    
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
  @PostMapping("/endturn/{gameId}")
  public boolean updateGame(@PathVariable String gameId,
      @RequestBody TurnData turn) throws JsonProcessingException {

    if (turn == null) {
      return false;
    }

    gameRegistry.get(gameId).updateGame(turn);
    
    return true;
  }
  
  

}
