package ca.mcgill.splendorserver.apis;

import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.GameConfigData;
import ca.mcgill.splendorserver.models.GameData;
import ca.mcgill.splendorserver.models.PlayerData;
import ca.mcgill.splendorserver.models.SessionData;
import ca.mcgill.splendorserver.models.TurnData;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.HashMap;
import java.util.Map;
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
  
  private HashMap<String, Game> gameRegistry = 
      new HashMap<String, Game>(Map.of("test", new Game()));
  private HashMap<String, Game> saves = new HashMap<>();
  
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
   * Launches game.
   *
   * @param gameId the id of the game
   * @param session the session data for the game to create
   * @throws JsonProcessingException when JSON processing error occurs
   */
  @PutMapping("/api/games/{gameId}")
  public void launchGame(@PathVariable(required = true, name = "gameId") String gameId,
                            @RequestBody SessionData session) throws JsonProcessingException {

    if (session != null) {
      if (saves.containsKey(session.getSavegame())) { //starting from saved game
        String oldId = saves.get(session.getSavegame()).getId();
        gameRegistry.put(gameId, gameRegistry.remove(oldId));
        if (gameRegistry.get(gameId) == null) { //backup in case the former didn't work
          addNewGame(gameId, saves.get(session.getSavegame()).getPlayers());
        }
      } else { //starting new game
        addNewGame(gameId, session.getPlayers());
      }

      if (gameRegistry.get(gameId) != null) {
        System.out.println("Launched game with id: " + gameId);
      } else {
        System.out.println("Could not launch game with id: " + gameId);
      }
    } else {
      System.out.println("Could not launch game with id: " + gameId
          + " because no session data was received");
    }
  }

  /**
   * Adds a game to the game registry.
   *
   * @param gameId the id of the game as per its session id in the LobbyService
   * @param players array of four PlayerData slots representing the players currently
   *                registered in the session in the LobbyService
   */
  private void addNewGame(String gameId, PlayerData[] players) {
    gameRegistry.put(gameId, new Game());
    gameRegistry.get(gameId).setPlayers(players);
  }
}


