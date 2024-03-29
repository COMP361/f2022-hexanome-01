package ca.mcgill.splendorserver.apis;

import ca.mcgill.splendorserver.controllers.GameManager;
import ca.mcgill.splendorserver.controllers.OrientManager;
import ca.mcgill.splendorserver.controllers.SaveManager;
import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.Inventory;
import ca.mcgill.splendorserver.models.Noble;
import ca.mcgill.splendorserver.models.Token;
import ca.mcgill.splendorserver.models.board.Board;
import ca.mcgill.splendorserver.models.board.CitiesBoard;
import ca.mcgill.splendorserver.models.cards.Card;
import ca.mcgill.splendorserver.models.cards.CardType;
import ca.mcgill.splendorserver.models.communicationbeans.SessionData;
import ca.mcgill.splendorserver.models.expansion.City;
import ca.mcgill.splendorserver.models.expansion.FreeToken;
import ca.mcgill.splendorserver.models.expansion.TradingPost;
import ca.mcgill.splendorserver.models.expansion.Unlockable;
import ca.mcgill.splendorserver.models.registries.CardRegistry;
import ca.mcgill.splendorserver.models.registries.NobleRegistry;
import ca.mcgill.splendorserver.models.registries.UnlockableRegistry;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.codec.digest.DigestUtils;
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
import org.springframework.web.bind.annotation.RequestParam;
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
  private JSONObject noUpdates;
  private JSONObject saveException;

  @Autowired
  private GameManager gameManager;
  @Autowired
  private SaveManager saveManager;

  private ExecutorService threads = Executors.newFixedThreadPool(5);

  /**
   * Main constructor.
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

    noUpdates = new JSONObject();
    noUpdates.put("status", "timeout");
    noUpdates.put("message", "No new updates.");

    saveException = new JSONObject();
    saveException.put("status", "failure");
    saveException.put("message", "Save threw an exception.");
  }

  /**
   * Constructor for tests, where autowiring isnt a thing.
   * (For invocation by subclass constructors, typically implicit.)
   *
   * @param gm Game manager
   * @param sm Save manager
   */
  @SuppressWarnings("unchecked")
  public GameController(GameManager gm, SaveManager sm) {
    gameManager = gm;
    saveManager = sm;
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

    noUpdates = new JSONObject();
    noUpdates.put("status", "timeout");
    noUpdates.put("message", "No new updates.");

    saveException = new JSONObject();
    saveException.put("status", "failure");
    saveException.put("message", "Save threw an exception.");
  }

  /**
   * generator for error responses.
   *
   * @param e exception to report
   * @return the response with the error
   */
  @SuppressWarnings("unchecked")
  public ResponseEntity<String> errorResponse(Exception e) {
    logger.error(e.getMessage());
    JSONObject error = new JSONObject();
    error.put("status", "failure");
    error.put("message", "Error 500: " + e.getMessage());
    return ResponseEntity.status(500).body(error.toJSONString());
  }

  /**
   * Getter for the board.
   *
   * @param gameId the id of the game to get the board of.
   * @return success flag
   */
  @GetMapping("/api/games/{gameId}/immediateBoard")
  public ResponseEntity<String> getBoard(@PathVariable String gameId) {
    Optional<Board> boardOptional = gameManager.getGameBoard(gameId);
    if (boardOptional.isPresent()) {
      return ResponseEntity.ok(boardOptional.get().toJson().toJSONString());
    } else {
      return ResponseEntity.badRequest().body("No board");
    }
  }

  /**
   * Getter for the board.
   *
   * @param gameId   the id of the game to get
   * @param lastHash of the previous attempt
   * @return success flag
   */
  @GetMapping("/api/games/{gameId}/board")
  public DeferredResult<String> getBoard(@PathVariable String gameId, @RequestParam(value = "hash",
      defaultValue = "") String lastHash) {
    //System.out.println("received request");
    DeferredResult<String> result = new DeferredResult<>(5000L);
    //timeout should result in a 408 error
    result.onTimeout(() ->
        result.setErrorResult(ResponseEntity.status(408).body(noUpdates.toJSONString())));
    threads.execute(() -> {
      //System.out.println("before try block");
      try {
        //TO DO: investigate whether thread pool can be timed to end
        //SEE: point 3.3 in https://www.baeldung.com/java-stop-execution-after-certain-time
        long end = System.currentTimeMillis() + 5000; //terminates the thread after 5 seconds
        Optional<Board> boardOptional;
        //return the board as soon as there is a board if this is the first request
        if (lastHash.isEmpty()) {
          //System.out.println("last hash is empty");
          do {
            boardOptional = gameManager.getGameBoard(gameId);
            if (boardOptional.isPresent()) {
              //System.out.println("board is present");
              result.setResult(boardOptional.get().toJson().toJSONString());
            }
          } while (!boardOptional.isPresent() && System.currentTimeMillis() < end);
        } else { //return the board if there's been an update since this isn't the first request
          //System.out.println("last hash is not empty:" + lastHash);
          do {
            //System.out.println("UPDATE");
            if (gameManager.getGame(gameId) == null && gameManager.getPastGame(gameId) != null) {
              //System.out.println("CALLED END SESSION");
              result.setResult("End Session");
              break;
            } else {
              boardOptional = gameManager.getGameBoard(gameId);
              //check if there's been an update on the board
              if (!boardOptional.isPresent()) {
                //an empty board when the previous board wasn't empty is an update
                //the previous board couldn't have been empty since there was a hash provided
                result.setResult("");
              } else if (!DigestUtils.md5Hex(boardOptional.get().toJson().toJSONString()).equals(
                  lastHash)) {
                result.setResult(boardOptional.get().toJson().toJSONString());
              }
            }
          } while (boardOptional.isPresent() && DigestUtils.md5Hex(
              boardOptional.get().toJson().toJSONString()).equals(lastHash)
              && System.currentTimeMillis() < end);
        }
      } catch (Exception e) {
        result.setErrorResult(errorResponse(e).getBody());
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
      Game game = gameManager.getGame(gameId);
      if (game != null) {
        return ResponseEntity.ok(game.getId());
      } else {
        return ResponseEntity.badRequest().body(gameNotFound.toJSONString());
      }
    } catch (Exception e) {
      return errorResponse(e);
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
  @SuppressWarnings("unchecked")
  @PostMapping("/api/action/{gameId}/takeTokens")
  public ResponseEntity<String> takeTokens(@PathVariable String gameId,
                                           @RequestBody JSONObject data)
      throws JsonProcessingException {
    try {
      //check validity of request
      String playerId = (String) data.get("playerId");
      Game game = gameManager.getGame(gameId);
      if (game == null) {
        return ResponseEntity.badRequest().body(gameNotFound.toJSONString());
      }
      if (!game.getCurrentPlayer().getUsername().equals(playerId)) {
        return ResponseEntity.badRequest().body(playerNotTurn.toJSONString());
      }

      //perform taking the tokens
      ArrayList<String> tokens = (ArrayList<String>) data.get("tokens");
      Token[] tokensArray = new Token[tokens.size()];
      for (int i = 0; i < tokensArray.length; i++) {
        tokensArray[i] = Token.valueOfIgnoreCase((String) tokens.get(i));
      }
      JSONObject response = gameManager.takeTokens(game, playerId, tokensArray);
      if (response == null) {
        return ResponseEntity.ok().body(invalidAction.toJSONString());
      }
      
      Board board = game.getBoard();
      Inventory inventory = board.getInventory(playerId);
      JSONArray noblesVisiting = gameManager.checkImpressedNobles(inventory, board);
      response.put("noblesVisiting", noblesVisiting);

      response.put("status", "success");
      //return the result of taking the tokens
      return ResponseEntity.ok(response.toJSONString());
    } catch (Exception e) {
      return errorResponse(e);
    }
  }

  /**
   * Returns token.
   *
   * @param gameId the id of the game
   * @param data   the game data of the return tokens action
   * @return success flag
   * @throws JsonProcessingException when JSON processing error occurs
   */
  @SuppressWarnings("unchecked")
  @PostMapping("/api/action/{gameId}/returnTokens")
  public ResponseEntity<String> returnTokens(@PathVariable String gameId,
                                             @RequestBody JSONObject data)
      throws JsonProcessingException {
    try {
      //check validity of request
      String playerId = (String) data.get("playerId");
      Game game = gameManager.getGame(gameId);
      if (game == null) {
        return ResponseEntity.badRequest().body(gameNotFound.toJSONString());
      }
      if (!game.getCurrentPlayer().getUsername().equals(playerId)) {
        return ResponseEntity.badRequest().body(playerNotTurn.toJSONString());
      }

      //perform returning the tokens
      ArrayList<String> tokens = (ArrayList<String>) data.get("tokens");
      Token[] tokensArray = new Token[tokens.size()];
      for (int i = 0; i < tokensArray.length; i++) {
        tokensArray[i] = Token.valueOfIgnoreCase((String) tokens.get(i));
      }

      if (!gameManager.returnTokens(game, playerId, tokensArray)) {
        return ResponseEntity.ok().body(invalidAction.toJSONString());
      }

      JSONObject response = new JSONObject();
      
      Board board = game.getBoard();
      Inventory inventory = board.getInventory(playerId);
      JSONArray noblesVisiting = gameManager.checkImpressedNobles(inventory, board);
      response.put("noblesVisiting", noblesVisiting);
      
      response.put("status", "success");
      //return the result of taking the tokens
      return ResponseEntity.ok(response.toJSONString());
    } catch (Exception e) {
      return errorResponse(e);
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

      Game game = gameManager.getGame(gameId);
      if (game == null) {
        return ResponseEntity.badRequest().body(gameNotFound.toJSONString());
      }
      if (!game.getCurrentPlayer().getUsername().equals(playerId)) {
        return ResponseEntity.badRequest().body(playerNotTurn.toJSONString());
      }

      //check validity of chosen card
      int cardId = (int) data.get("cardId");
      JSONObject response = gameManager.purchaseCard(game, playerId, cardId);
      if (response == null) {
        return ResponseEntity.ok().body(invalidAction.toJSONString());
      }

      response.put("status", "success");

      return ResponseEntity.ok(response.toJSONString());
    } catch (Exception e) {
      return errorResponse(e);
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

      Game game = gameManager.getGame(gameId);
      if (game == null) {
        return ResponseEntity.badRequest().body(gameNotFound.toJSONString());
      }
      if (!game.getCurrentPlayer().getUsername().equals(playerId)) {
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
      response.put("type", CardType.DOMINO2.toString());
      ArrayList<Integer> choices = OrientManager.getDominoOptions(board, 1);
      response.put("options", JSONArray.toJSONString(choices));

      response.put("status", "success");

      return ResponseEntity.ok(response.toJSONString());
    } catch (Exception e) {
      return errorResponse(e);
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

      Game game = gameManager.getGame(gameId);
      if (game == null) {
        return ResponseEntity.badRequest().body(gameNotFound.toJSONString());
      }
      if (!game.getCurrentPlayer().getUsername().equals(playerId)) {
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

      JSONArray noblesVisiting = gameManager.checkImpressedNobles(inventory, board);

      if (gameManager.checkFreeToken(inventory)) {
        response.replace("action", "token");
      }

      response.put("noblesVisiting", noblesVisiting);

      response.put("options", JSONArray.toJSONString(new ArrayList<Integer>()));

      response.put("status", "success");

      return ResponseEntity.ok(response.toJSONString());
    } catch (Exception e) {
      return errorResponse(e);
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

      Game game = gameManager.getGame(gameId);
      if (game == null) {
        return ResponseEntity.badRequest().body(gameNotFound.toJSONString());
      }
      if (!game.getCurrentPlayer().getUsername().equals(playerId)) {
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

      JSONArray noblesVisiting = gameManager.checkImpressedNobles(inventory, board);

      if (gameManager.checkFreeToken(inventory)) {
        response.replace("action", "token");
      }

      response.put("noblesVisiting", noblesVisiting);

      response.put("options", JSONArray.toJSONString(new ArrayList<Integer>()));

      response.put("status", "success");

      return ResponseEntity.ok(response.toJSONString());
    } catch (Exception e) {
      return errorResponse(e);
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

      Game game = gameManager.getGame(gameId);
      if (game == null) {
        return ResponseEntity.badRequest().body(gameNotFound.toJSONString());
      }
      if (!game.getCurrentPlayer().getUsername().equals(playerId)) {
        return ResponseEntity.badRequest().body(playerNotTurn.toJSONString());
      }

      int cardId = (int) data.get("cardId");
      Board board = game.getBoard();
      Card card = CardRegistry.of(cardId);
      Inventory inventory = board.getInventory(playerId);

      if (!gameManager.acquireCard(card, board, inventory)) {
        return ResponseEntity.ok().body(invalidAction.toJSONString());
      }

      //result cannot be null due to nature of domino
      JSONObject response = gameManager.purchaseCardBody(card, board, inventory);

      response.put("status", "success");

      return ResponseEntity.ok(response.toJSONString());
    } catch (Exception e) {
      return errorResponse(e);
    }
  }


  /**
   * Takes token.
   *
   * @param gameId the id of the game
   * @param data   the game data of the take reserve card action
   * @return success flaggit
   * @throws JsonProcessingException when JSON processing error occurs
   */
  @SuppressWarnings("unchecked")
  @PostMapping("/api/action/{gameId}/reserveCard")
  public ResponseEntity<String> reserveCard(@PathVariable String gameId,
                                            @RequestBody JSONObject data)
      throws JsonProcessingException {
    try {
      String playerId = (String) data.get("playerId");

      Game game = gameManager.getGame(gameId);
      if (game == null) {
        return ResponseEntity.badRequest().body(gameNotFound.toJSONString());
      }
      if (!game.getCurrentPlayer().getUsername().equals(playerId)) {
        return ResponseEntity.badRequest().body(playerNotTurn.toJSONString());
      }

      String source = (String) data.get("source");

      int cardId = (int) data.get("cardId");
      String deckId = (String) data.get("deckId");

      JSONObject response = gameManager.reserveCard(game, playerId, source, cardId, deckId);
      if (response != null) {
        response.put("status", "success");
      } else {
        response = new JSONObject();
        response.put("status", "failure");
      }
      return ResponseEntity.ok(response.toJSONString());
    } catch (Exception e) {
      return errorResponse(e);
    }
  }

  /**
   * Claims a noble.
   *
   * @param gameId the id of the game
   * @param data   the game data of the action
   * @return success flag
   * @throws JsonProcessingException when JSON processing error occurs
   */
  @SuppressWarnings("unchecked")
  @PostMapping("/api/action/{gameId}/claimNoble")
  public ResponseEntity<String> claimNobleAction(@PathVariable String gameId,
                                                 @RequestBody JSONObject data)
      throws JsonProcessingException {
    try {
      String playerId = (String) data.get("playerId");

      Game game = gameManager.getGame(gameId);
      if (game == null) {
        return ResponseEntity.badRequest().body(gameNotFound.toJSONString());
      }
      if (!game.getCurrentPlayer().getUsername().equals(playerId)) {
        return ResponseEntity.badRequest().body(playerNotTurn.toJSONString());
      }

      int nobleId = (int) data.get("nobleId");
      Board board = game.getBoard();
      Noble noble = NobleRegistry.of(nobleId);
      Inventory inventory = board.getInventory(playerId);

      if (!gameManager.acquireNoble(noble, board, inventory)) {
        return ResponseEntity.ok().body(invalidAction.toJSONString());
      }

      JSONObject response = new JSONObject();
      response.put("status", "success");

      return ResponseEntity.ok(response.toJSONString());
    } catch (Exception e) {
      return errorResponse(e);
    }
  }

  /**
   * Ends turn, or returns city choices if multiple cities are unlocked.
   *
   * @param gameId the id of the game
   * @return success flag or list of cities
   * @throws JsonProcessingException when JSON processing error occurs
   */
  @SuppressWarnings("unchecked")
  @PostMapping("/api/action/{gameId}/endTurn")
  public ResponseEntity<String> endTurnAction(@PathVariable String gameId)
      throws JsonProcessingException {
    try {
      Game game = gameManager.getGame(gameId);
      if (game == null) {
        return ResponseEntity.badRequest().body(gameNotFound.toJSONString());
      }

      JSONObject response = gameManager.endTurn(game);
      response.put("status", "success");

      return ResponseEntity.ok().body(response.toJSONString());
    } catch (Exception e) {
      return errorResponse(e);
    }
  }


  /**
   * Removes a game from the server upon client request.
   *
   * @param gameId the id of the game we want to delete
   */
  @DeleteMapping(path = "/api/action/{gameId}/delete")
  public void deleteGame(@PathVariable(required = true, name = "gameId") long gameId) {
    gameManager.deleteGame("" + gameId);
  }

  /**
   * Launches game.
   *
   * @param gameId  the id of the game
   * @param session the session data for the game to create
   * @return that the launch was successful
   */
  @PutMapping("/api/games/{gameId}")
  public ResponseEntity<HttpStatus> launchGame(
      @PathVariable(required = true, name = "gameId") String gameId,
      @RequestBody SessionData session) {
    //try {
    System.out.println("launching: " + gameId);
    if (!gameManager.launchGame(gameId, session)) {
      return ResponseEntity.status(500).body(HttpStatus.INTERNAL_SERVER_ERROR); 
      //throw new Exception();
    }

    return ResponseEntity.ok(HttpStatus.OK);
    //} catch (Exception e) {
    //return ResponseEntity.status(500).body(HttpStatus.INTERNAL_SERVER_ERROR);
    //}
  }

  /**
   * Sets chosen city for current player, then ends turn.
   *
   * @param gameId the id of the game
   * @param data   the city data
   * @return success flag
   * @throws JsonProcessingException when JSON processing error occurs
   */
  @SuppressWarnings("unchecked")
  @PostMapping("/api/action/{gameId}/chooseCity")
  public ResponseEntity<String> chooseCity(@PathVariable String gameId,
                                           @RequestBody JSONObject data)
      throws JsonProcessingException {
    try {
      Game game = gameManager.getGame(gameId);
      if (game == null) {
        return ResponseEntity.badRequest().body(gameNotFound.toJSONString());
      }

      int cityId = (int) data.get("cityId");
      for (int i : ((CitiesBoard) game.getBoard()).getCities()) {
        if (i == cityId) {
          UnlockableRegistry.of(cityId).observe(game.getCurrentPlayer());
        }
      }

      if (!game.getCurrentPlayer().getInventory().containsCity()) {
        return ResponseEntity.badRequest().body(invalidAction.toJSONString());
      }

      gameManager.changeTurn(game);

      JSONObject response = new JSONObject();
      response.put("status", "success");

      return ResponseEntity.ok().body(response.toJSONString());
    } catch (Exception e) {
      return errorResponse(e);
    }
  }

  /**
   * Takes many tokens.
   *
   * @param gameId the id of the game
   * @param data   the game data of the take tokens action
   * @return success flag
   * @throws JsonProcessingException when JSON processing error occurs
   */
  @SuppressWarnings("unchecked")
  @PostMapping("/api/action/{gameId}/freeTokens")
  public ResponseEntity<String> freeTokens(@PathVariable String gameId,
                                           @RequestBody JSONObject data)
      throws JsonProcessingException {
    try {
      String playerId = (String) data.get("playerId");

      Game game = gameManager.getGame(gameId);
      if (game == null) {
        return ResponseEntity.badRequest().body(gameNotFound.toJSONString());
      }
      if (!game.getCurrentPlayer().getUsername().equals(playerId)) {
        return ResponseEntity.badRequest().body(playerNotTurn.toJSONString());
      }

      Board board = game.getBoard();
      Inventory inventory = board.getInventory(playerId);

      for (Token token : Token.values()) {
        if (token.equals(Token.GOLD)) {
          continue;
        }
        inventory.getTokens().removeRepeated(token, inventory.getTokens().checkAmount(token));
        inventory.getTokens().addRepeated(token, 9999);
      }

      JSONObject response = new JSONObject();
      response.put("status", "success");

      return ResponseEntity.ok(response.toJSONString());
    } catch (Exception e) {
      return errorResponse(e);
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
  @PostMapping("/api/action/{gameId}/sacrifice")
  public ResponseEntity<String> sacrifice(@PathVariable String gameId,
                                          @RequestBody JSONObject data)
      throws JsonProcessingException {
    try {
      String playerId = (String) data.get("playerId");

      Game game = gameManager.getGame(gameId);
      if (game == null) {
        return ResponseEntity.badRequest().body(gameNotFound.toJSONString());
      }
      if (!game.getCurrentPlayer().getUsername().equals(playerId)) {
        return ResponseEntity.badRequest().body(playerNotTurn.toJSONString());
      }
      int cardId1;
      int cardId2;
      int originalId = (int) data.get("originalId");
      if (data.get("cardId1") != null) {
        cardId1 = (int) data.get("cardId1");
      } else {
        cardId1 = -1;
      }

      if (data.get("cardId2") != null) {
        cardId2 = (int) data.get("cardId2");
      } else {
        cardId2 = -1;
      }

      Board board = game.getBoard();
      Card originalCard = CardRegistry.of(originalId);
      Card card1 = CardRegistry.of(cardId1);
      Card card2 = CardRegistry.of(cardId2);
      Inventory inventory = board.getInventory(playerId);

      if (originalCard == null || !OrientManager.makeSacrifice(card1, card2, inventory)) {
        return ResponseEntity.ok().body(invalidAction.toJSONString());
      }

      inventory.acquireCard(originalCard);
      inventory.addCard(originalCard);

      board.getCards().draw(originalCard);

      JSONObject response = new JSONObject();
      response.put("action", "none");

      JSONArray noblesVisiting = gameManager.checkImpressedNobles(inventory, board);

      if (gameManager.checkFreeToken(inventory)) {
        response.replace("action", "token");
      }

      response.put("noblesVisiting", noblesVisiting);

      response.put("options", JSONArray.toJSONString(new ArrayList<Integer>()));

      response.put("status", "success");

      return ResponseEntity.ok(response.toJSONString());
    } catch (Exception e) {
      return errorResponse(e);
    }
  }

  /**
   * Saves a game with a specified gameId.
   *
   * @param gameId the id of the game to save
   * @return the id of the save if successful, the error message otherwise
   */
  @SuppressWarnings("unchecked")
  @PostMapping("/api/action/{gameId}/save")
  public ResponseEntity<String> save(@PathVariable String gameId) {
    System.out.println("Saving: " + gameId);
    JSONObject response = new JSONObject();
    //get the game to save
    Game game = gameManager.getGame(gameId);
    if (game == null) {
      game = gameManager.getPastGame(gameId);
      if (game == null) {
        return ResponseEntity.ok(gameNotFound.toJSONString());
      }
    }
    //save and get the savegameid of the save
    String successfulSave = saveManager.saveGame(game);
    if (successfulSave == null) {
      return ResponseEntity.ok(saveException.toJSONString());
    }
    response.put("status", "success");
    return ResponseEntity.ok().body(response.toJSONString());
  }
}