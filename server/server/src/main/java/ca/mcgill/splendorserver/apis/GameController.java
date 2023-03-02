package ca.mcgill.splendorserver.apis;

import java.util.ArrayList;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import ca.mcgill.splendorserver.controllers.GameManager;
import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.Noble;
import ca.mcgill.splendorserver.models.SessionData;
import ca.mcgill.splendorserver.models.board.Board;

/**
 * Game controller class for the server.
 */
@RestController
public class GameController {

  private final Logger logger;
  
  private JSONObject gameNotFound;
  private JSONObject playerNotTurn;
  
  private JSONObject invalidAction;

  @Autowired
  private GameManager gameManager;

  /**
   * Sole constructor.
   * (For invocation by subclass constructors, typically implicit.)
   */
@SuppressWarnings("unchecked")
public GameController() {
    logger = LoggerFactory.getLogger(GameController.class);
  
    gameNotFound = new JSONObject();
    gameNotFound.put("status", "failure");
    gameNotFound.put("message", "Game not found.");
    
    playerNotTurn = new JSONObject();
    playerNotTurn.put("status", "failure");
    playerNotTurn.put("message", "Cannot make move outside of turn.");
    
    invalidAction = new JSONObject();
    invalidAction.put("status", "failure");
    invalidAction.put("message", "Invalid action.");
  
  }

@SuppressWarnings({ "unused", "unchecked" })
private ResponseEntity<String> errorResponse(String message) {
    JSONObject error = new JSONObject();
    error.put("status", "failure");
    error.put("message", "Error 500: " + message);
    return ResponseEntity.status(500).body(error.toJSONString());
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
	try {
		Optional<Board> boardOptional = GameManager.getGameBoard(gameId);
		if (boardOptional.isPresent()) {
			return ResponseEntity.ok(boardOptional.get().toJson().toJSONString());
		} else {
			return ResponseEntity.badRequest().body(gameNotFound.toJSONString());
		}
	}
	catch (Exception e) {
		return errorResponse(e.getMessage());
	}
  }

  /**
   * Returns the game with the given id.
   * Used for testing.
   *
   * @param gameId the gameId
   * @return the gameid for now
   */
  @GetMapping("/api/games/{gameId}")
  public ResponseEntity<String> getGame(@PathVariable String gameId) {
	try {
		Game game = GameManager.getGame(gameId);
		if (game != null) {
			return ResponseEntity.ok(game.getId());
		} else {
			return ResponseEntity.badRequest().body(gameNotFound.toJSONString());
		}
	}
	catch (Exception e) {
		return errorResponse(e.getMessage());
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
  @PostMapping("/api/action/{gameId}/takeTokens")
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
  @SuppressWarnings("unchecked")
  @PostMapping("/api/action/{gameId}/purchaseCard")
  public ResponseEntity<String> purchaseCard(@PathVariable String gameId,
                                                               @RequestBody JSONObject data) {
	
	try {
		String playerId = (String) data.get("playerId");

		//parse data
		//if card's type is not sacrifice, check to see if we can purchase it with tokens
		//  if can purchase it, acquire it (acquire needs to be different method, since sometimes can get cards for free)
		//  call orientManager to deal with the cards action (so that we dont bloat this class)
		//if cards type WAS sacrifice, ping client to ask what cards they want to use (pass list of cards?)

		Game game = GameManager.getGame(gameId);
		if (game == null)
			return ResponseEntity.badRequest().body(gameNotFound.toJSONString());

		if (!game.getCurrentPlayer().equals(playerId)) {
			return ResponseEntity.badRequest().body(playerNotTurn.toJSONString());
		}
		
		int cardId = (int) data.get("cardId");
		
		ArrayList<Noble> noblesVisiting = GameManager.purchaseCard(game, playerId, cardId);
		if (noblesVisiting == null)
			return ResponseEntity.ok().body(invalidAction.toJSONString());
		
		JSONObject response = new JSONObject();
		response.put("status", "success");
		response.put("noblesVisiting", JSONArray.toJSONString(noblesVisiting));
		
		return ResponseEntity.ok(response.toJSONString());
	}
	catch (Exception e) {
		return errorResponse(e.getMessage());
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
  @PostMapping("/api/action/{gameId}/reserveCard")
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
  @PostMapping("/api/action/{gameId}/claimNoble")
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
    GameManager.deleteGame("" + gameId);
  }

  /**
   * Launches game.
   *
   * @param gameId  the id of the game
   * @param session the session data for the game to create
   * @return that the launch was successful
   * @throws JsonProcessingException when JSON processing error occurs
   */
  @PutMapping("/api/games/{gameId}")
  public ResponseEntity<HttpStatus> launchGame(
      @PathVariable(required = true, name = "gameId") String gameId,
      @RequestBody SessionData session) throws JsonProcessingException {
    System.out.println("launching");
    GameManager.launchGame(gameId, session);

    return ResponseEntity.ok(HttpStatus.OK);
  }
}


