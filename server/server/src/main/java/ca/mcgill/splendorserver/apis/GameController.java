package ca.mcgill.splendorserver.apis;

import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.GameConfigData;
import ca.mcgill.splendorserver.models.GameData;
import ca.mcgill.splendorserver.models.TurnData;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
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
   * Sole constructor.  
   * (For invocation by subclass constructors, typically implicit.)
   */
  public GameController() {
    
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
    System.out.println(gameRegistry.get(gameId).getCurrentPlayer().getId());
    gameRegistry.get(gameId).updateGame(turn);
    System.out.println(gameRegistry.get(gameId).getCurrentPlayer().getId());
    System.out.println("Ended turn on game with ID: " + gameId);
    
    return true;
  }
  
  /**
   * Getter for the saved games.
   *
   * @return the saved games data
   */
  @GetMapping(path = {"/saves"}, produces = "application/json; charset=UTF-8")
  @ResponseBody
  public HashMap<String, Game> getSaves() {

    try {
      // if we decide to all use java version 11 or later,
      // replace lines below with the commented out line
      List<String> savesLines = Files.readAllLines(FileSystems.getDefault().getPath("saves.json"));
      String saves = "";
      for (String line : savesLines) {
        saves += line;
      }
      //String saves = Files.readString(FileSystems.getDefault().getPath("saves.json"));
      return (HashMap<String, Game>) JsonHandler.decodeJsonRequest(saves);
    } catch (Exception e) {
      return null;
    }

  }

}
