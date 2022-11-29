package ca.mcgill.splendorserver.apis;

import ca.mcgill.splendorserver.models.GameData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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

  @PutMapping(path = {"/GameId/{gameId}"}, consumes = "application/json; charset=UTF-8")
  public String lauchGame(@PathVariable String gameId, 
      @RequestBody GameData game) throws JsonProcessingException {
    return createGame(game);
  }

}
