package ca.mcgill.splendorserver.apis;

import ca.mcgill.splendorserver.controllers.GameManager;
import ca.mcgill.splendorserver.controllers.OrientManager;
import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.Inventory;
import ca.mcgill.splendorserver.models.Noble;
import ca.mcgill.splendorserver.models.board.Board;
import ca.mcgill.splendorserver.models.cards.Card;
import ca.mcgill.splendorserver.models.communicationbeans.ReserveCardData;
import ca.mcgill.splendorserver.models.communicationbeans.SessionData;
import ca.mcgill.splendorserver.models.registries.CardRegistry;
import ca.mcgill.splendorserver.models.registries.NobleRegistry;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import org.springframework.web.context.request.async.DeferredResult;

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
  
  private ExecutorService threads = Executors.newFixedThreadPool(5);

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

  @SuppressWarnings({"unused", "unchecked"})
  private ResponseEntity<String> errorResponse(String message) {
    JSONObject error = new JSONObject();
    error.put("status", "failure");
    error.put("message", "Error 500: " + message);
    return ResponseEntity.status(500).body(error.toJSONString());
  }

  /**
   * Getter for the board.
   *
   * @param gameId the id of the game to get
   * @return success flag
   * @throws JsonProcessingException when JSON processing error occurs
   */
  @GetMapping("/api/games/{gameId}/board")
  public DeferredResult<String> getBoard(@PathVariable String gameId) {
	  DeferredResult<String> result = new DeferredResult<>(5000L);
	  result.onTimeout(() -> result.setResult(gameNotFound.toJSONString()));
	  threads.execute(() -> {
		  try {		
			  Optional<Board> boardOptional;
			  do {
				  boardOptional = GameManager.getGameBoard(gameId);
				  if (boardOptional.isPresent()) {
					  result.setResult(boardOptional.get().toJson().toJSONString());
				  }
			  }
			  while (boardOptional.isEmpty());
		  }
		  catch (Exception e) {
				logger.error(e.getStackTrace().toString());
			  result.setErrorResult(errorResponse(e.getMessage()).getBody());
		  }
	  });
	  return result;
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
    } catch (Exception e) {
		logger.error(e.getStackTrace().toString());
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
  public ResponseEntity<String> takeTokens(@PathVariable String gameId,
                                           @RequestBody JSONObject data)
      throws JsonProcessingException {
    try {
      //check validity of request
      String playerId = (String) data.get("playerId");
      Game game = GameManager.getGame(gameId);
      if (game == null) {
        return ResponseEntity.badRequest().body(gameNotFound.toJSONString());
      }
      if (!game.getCurrentPlayer().equals(playerId)) {
        return ResponseEntity.badRequest().body(playerNotTurn.toJSONString());
      }

      //perform taking the tokens
      JSONArray tokens = (JSONArray) data.get("tokens");
      String[] tokenStrings = new String[tokens.size()];
      for (int i = 0; i < tokenStrings.length; i++) {
        tokenStrings[i] = (String) tokens.get(i);
      }
      JSONObject response = GameManager.takeTokens(game, playerId, tokenStrings);

      //return the result of taking the tokens
      return ResponseEntity.ok(response.toJSONString());
    } catch (Exception e) {
		logger.error(e.getStackTrace().toString());
      return errorResponse(e.getMessage());
    }
  }

  /**
   * Purchases a card.
   *
   * @param gameId the id of the game
   * @param data   the game data of the take tokens action
   * @return success flag
   * @throws JsonProcessingException when JSON processing error occurs
   */
  @SuppressWarnings("unchecked")
  @PostMapping("/api/action/{gameId}/purchaseCard")
  public ResponseEntity<String> purchaseCard(@PathVariable String gameId,
                                             @RequestBody JSONObject data) 
                                            	      throws JsonProcessingException {
    try {
      String playerId = (String) data.get("playerId");

      //parse data
      //if card's type is not sacrifice, check to see if we can purchase it with tokens
      //if we can purchase it, acquire it
      //(acquire needs to be different method, since sometimes can get cards for free)
      //call orientManager to deal with the cards action (so that we don't bloat this class)
      //if cards type WAS sacrifice, ping client to ask what cards they want to use
      //(pass list of cards?)

      Game game = GameManager.getGame(gameId);
      if (game == null) {
        return ResponseEntity.badRequest().body(gameNotFound.toJSONString());
      }
      if (!game.getCurrentPlayer().getUsername().equals(playerId)) {
        return ResponseEntity.badRequest().body(playerNotTurn.toJSONString());
      }

      int cardId = (int) data.get("cardId");
      JSONObject response = GameManager.purchaseCard(game, playerId, cardId);
      if (response == null) {
        return ResponseEntity.ok().body(invalidAction.toJSONString());
      }
      response.put("status", "success");

      return ResponseEntity.ok(response.toJSONString());
    } catch (Exception e) {
		logger.error(e.getStackTrace().toString());
      return errorResponse(e.getMessage());
    }
  }

  /**
   * Performs the satchel action associated with a domino card.
   *
   * @param gameId the id of the game
   * @param data   the game data of the take tokens action
   * @return success flag
   * @throws JsonProcessingException when JSON processing error occurs
   */
  @SuppressWarnings("unchecked")
  @PostMapping("/api/action/{gameId}/dominoSatchel")
  public ResponseEntity<String> dominoSatchel(@PathVariable String gameId,
                                              @RequestBody JSONObject data)
                                            	      throws JsonProcessingException {
    try {
      String playerId = (String) data.get("playerId");

      Game game = GameManager.getGame(gameId);
      if (game == null) {
        return ResponseEntity.badRequest().body(gameNotFound.toJSONString());
      }
      if (!game.getCurrentPlayer().equals(playerId)) {
        return ResponseEntity.badRequest().body(playerNotTurn.toJSONString());
      }

      int cardId = (int) data.get("cardId");
      JSONObject response = new JSONObject();
      Board board = game.getBoard();
      Card card = CardRegistry.of(cardId);
      Inventory inventory = board.getInventory(playerId);

      if (!OrientManager.addSatchel(card, inventory)) {
        return ResponseEntity.ok().body(invalidAction.toJSONString());
      }
      response.put("type", "DOMINO2");
      ArrayList<Card> choices = OrientManager.getDominoOptions(board, 1);
      response.put("options", JSONArray.toJSONString(choices));

      response.put("status", "success");

      return ResponseEntity.ok(response.toJSONString());
    } catch (Exception e) {
		logger.error(e.getStackTrace().toString());
      return errorResponse(e.getMessage());
    }
  }

  /**
   * Attaches a satchel to the selected card.
   *
   * @param gameId the id of the game
   * @param data   the game data of the take tokens action
   * @return success flag
   * @throws JsonProcessingException when JSON processing error occurs
   */
  @SuppressWarnings("unchecked")
  @PostMapping("/api/action/{gameId}/satchel")
  public ResponseEntity<String> satchel(@PathVariable String gameId,
                                        @RequestBody JSONObject data)
                                        	      throws JsonProcessingException {
    try {
      String playerId = (String) data.get("playerId");

      Game game = GameManager.getGame(gameId);
      if (game == null) {
        return ResponseEntity.badRequest().body(gameNotFound.toJSONString());
      }
      if (!game.getCurrentPlayer().equals(playerId)) {
        return ResponseEntity.badRequest().body(playerNotTurn.toJSONString());
      }

      int cardId = (int) data.get("cardId");
      JSONObject response = new JSONObject();
      Board board = game.getBoard();
      Card card = CardRegistry.of(cardId);
      Inventory inventory = board.getInventory(playerId);

      if (!OrientManager.addSatchel(card, inventory)) {
        return ResponseEntity.ok().body(invalidAction.toJSONString());
      }
      response.put("action", "none");

      response.put("noblesVisiting",
          JSONArray.toJSONString(board.getNobles().attemptImpress(inventory)));

      response.put("options", JSONArray.toJSONString(new ArrayList<Card>()));

      response.put("status", "success");

      return ResponseEntity.ok(response.toJSONString());
    } catch (Exception e) {
		logger.error(e.getStackTrace().toString());
      return errorResponse(e.getMessage());
    }
  }

  /**
   * Reserves the selected noble.
   *
   * @param gameId the id of the game
   * @param data   the game data of the take tokens action
   * @return success flag
   * @throws JsonProcessingException when JSON processing error occurs
   */
  @SuppressWarnings("unchecked")
  @PostMapping("/api/action/{gameId}/reserveNoble")
  public ResponseEntity<String> reserveNoble(@PathVariable String gameId,
                                             @RequestBody JSONObject data)
                                            	      throws JsonProcessingException {
    try {
      String playerId = (String) data.get("playerId");

      Game game = GameManager.getGame(gameId);
      if (game == null) {
        return ResponseEntity.badRequest().body(gameNotFound.toJSONString());
      }
      if (!game.getCurrentPlayer().equals(playerId)) {
        return ResponseEntity.badRequest().body(playerNotTurn.toJSONString());
      }

      int nobleId = (int) data.get("nobleId");
      JSONObject response = new JSONObject();
      Board board = game.getBoard();
      Noble noble = NobleRegistry.of(nobleId);
      Inventory inventory = board.getInventory(playerId);

      if (!OrientManager.reserveNoble(noble, board, inventory)) {
        return ResponseEntity.ok().body(invalidAction.toJSONString());
      }
      response.put("action", "none");

      response.put("noblesVisiting",
          JSONArray.toJSONString(board.getNobles().attemptImpress(inventory)));

      response.put("options", JSONArray.toJSONString(new ArrayList<Card>()));

      response.put("status", "success");

      return ResponseEntity.ok(response.toJSONString());
    } catch (Exception e) {
		logger.error(e.getStackTrace().toString());
      return errorResponse(e.getMessage());
    }
  }

  /**
   * Takes selected card for free due to domino effect.
   *
   * @param gameId the id of the game
   * @param data   the game data of the take tokens action
   * @return success flag
   * @throws JsonProcessingException when JSON processing error occurs
   */
  @SuppressWarnings("unchecked")
  @PostMapping("/api/action/{gameId}/domino")
  public ResponseEntity<String> domino(@PathVariable String gameId,
                                       @RequestBody JSONObject data)
                                    		      throws JsonProcessingException {
    try {
      String playerId = (String) data.get("playerId");

      Game game = GameManager.getGame(gameId);
      if (game == null) {
        return ResponseEntity.badRequest().body(gameNotFound.toJSONString());
      }
      if (!game.getCurrentPlayer().equals(playerId)) {
        return ResponseEntity.badRequest().body(playerNotTurn.toJSONString());
      }

      int cardId = (int) data.get("cardId");
      Board board = game.getBoard();
      Card card = CardRegistry.of(cardId);
      Inventory inventory = board.getInventory(playerId);

      if (!GameManager.acquireCard(card, board, inventory)) {
        return ResponseEntity.ok().body(invalidAction.toJSONString());
      }
      
      JSONObject response = GameManager.determineBody(card, board, inventory);
      response.put("status", "success");

      return ResponseEntity.ok(response.toJSONString());
    } catch (Exception e) {
		logger.error(e.getStackTrace().toString());
      return errorResponse(e.getMessage());
    }
  }


  /**
   * Takes token.
   *
   * @param gameId          the id of the game
   * @param reserveCardData the game data of the take reserve card action
   * @return success flaggit 
   */
  @PostMapping("/api/action/{gameId}/reserveCard")
  public ResponseEntity<String> reserveCardAction(@PathVariable String gameId,
                                                  @RequestBody ReserveCardData reserveCardData)
                                                	      throws JsonProcessingException {

    boolean success = gameManager.reserveCard(gameId, reserveCardData);
    JSONObject response = new JSONObject();
    if (success) {
      response.put("status", "success");
    } else {
      response.put("status", "failure");
    }
    return ResponseEntity.ok(response.toJSONString());
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
      @RequestBody SessionData session)
    	      throws JsonProcessingException {
	try {
		System.out.println("launching: " + gameId);
		GameManager.launchGame(gameId, session);

		return ResponseEntity.ok(HttpStatus.OK);
	}
	catch (Exception e) {
		logger.error(e.getStackTrace().toString());
		return ResponseEntity.status(500).body(HttpStatus.INTERNAL_SERVER_ERROR);
	}
  }
}