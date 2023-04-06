package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import ca.mcgill.splendorserver.apis.GameController;
import ca.mcgill.splendorserver.controllers.GameManager;
import ca.mcgill.splendorserver.controllers.OrientManager;
import ca.mcgill.splendorserver.controllers.SaveManager;
import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.Inventory;
import ca.mcgill.splendorserver.models.Noble;
import ca.mcgill.splendorserver.models.Player;
import ca.mcgill.splendorserver.models.Token;
import ca.mcgill.splendorserver.models.communicationbeans.SessionData;
import ca.mcgill.splendorserver.models.registries.CardRegistry;
import ca.mcgill.splendorserver.models.registries.NobleRegistry;
import ca.mcgill.splendorserver.models.registries.UnlockableRegistry;
import ca.mcgill.splendorserver.models.board.Board;
import ca.mcgill.splendorserver.models.board.CardBank;
import ca.mcgill.splendorserver.models.board.CitiesBoard;
import ca.mcgill.splendorserver.models.cards.Card;
import ca.mcgill.splendorserver.models.cards.CardLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import com.fasterxml.jackson.core.JsonProcessingException;

import utils.ControllerTestUtils;

/**
 * Tester for Game controller endpoints and functionality.
 */
public class GameManagerTest extends ControllerTestUtils {
	
	
  @Test
  public void launchGameTest() {
    SessionData dummy = createDummySessionData();
    GameManager gameManager = new GameManager();
    gameManager.launchGame("TestGame", dummy);
    HashMap<String, Game> gameRegistry = gameManager.getGameRegistry();
    assertTrue(gameRegistry.containsKey("TestGame"));
  }

  @Test
  public void getGameTest() {
    SessionData dummy = createDummySessionData();
    GameManager gameManager = new GameManager();
    gameManager.launchGame("TestGame", dummy);
    Game game = gameManager.getGame("TestGame");
    assertEquals("TestGame", game.getId());
    assertEquals(null, gameManager.getGame("FakeGame"));
  }
  
  @Test
  public void getNobleTest() {
    SessionData dummy = createDummySessionData();
    GameManager gameManager = new GameManager();
    gameManager.launchGame("TestGame", dummy);
    Game game = gameManager.getGame("TestGame");
    Board board = game.getBoard();
    Inventory testInventory = board.getInventory("testCreator");

    Noble noble = NobleRegistry.of(board.getNobles().getNobles()[0]);
    gameManager.acquireNoble(NobleRegistry.of(board.getNobles().getNobles()[0]), board, testInventory);
    assertEquals(noble, testInventory.getNobles().get(0));

    Noble nobleToReserve = NobleRegistry.of(board.getNobles().getNobles()[1]);
    OrientManager.reserveNoble(nobleToReserve, board, testInventory);
    assertEquals(nobleToReserve, testInventory.getReservedNobles().get(0));
    
    assertTrue("succeeded when should have failed noble1", !gameManager.acquireNoble(null, board, testInventory));
    assertTrue("succeeded when should have failed noble2", !gameManager.acquireNoble(new Noble(69), board, testInventory));
    Noble test = new Noble(69);
    testInventory.getReservedNobles().add(test);
    assertTrue("failed when should have succeeded noble3", gameManager.acquireNoble(test, board, testInventory));
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void freeTokensTest() throws JsonProcessingException {
	    SessionData dummy = createDummySessionData();
	    GameManager gameManager = new GameManager();
	    gameManager.launchGame("TestGame", dummy);
	    Game game = gameManager.getGame("TestGame");
	    GameController gc = new GameController(gameManager, new SaveManager());
	    JSONObject data = new JSONObject();
	    data.put("playerId", "fake");
	    
	    JSONObject gameNotFound = new JSONObject();
	    gameNotFound.put("status", "failure");
	    gameNotFound.put("message", "Game not found.");

	    JSONObject playerNotTurn = new JSONObject();
	    playerNotTurn.put("status", "failure");
	    playerNotTurn.put("message", "Cannot make move outside of turn.");
	    
	    ResponseEntity<String> response = gc.freeTokens("fake", data);
		assertEquals(gameNotFound.toJSONString(), response.getBody());
	    response = gc.freeTokens("TestGame", data);
		assertEquals(playerNotTurn.toJSONString(), response.getBody());
	    data.replace("playerId", "testCreator");
	    
	    gc.freeTokens("TestGame", data);
	    assertEquals(9999, game.getPlayers()[0].getInventory().getTokens().checkAmount(Token.RED));
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void selectCityTest() throws JsonProcessingException {
	    SessionData dummy = createDummyCities();
	    GameManager gameManager = new GameManager();
	    gameManager.launchGame("TestGame", dummy);
	    Game game = gameManager.getGame("TestGame");
	    GameController gc = new GameController(gameManager, new SaveManager());
	    JSONObject data = new JSONObject();
	    data.put("cityId", ((CitiesBoard) game.getBoard()).getCities()[0]);
	    game.getCurrentPlayer().getInventory().getUnlockables().add(UnlockableRegistry.of(((CitiesBoard) game.getBoard()).getCities()[0]));
	    
	    data.put("playerId", "fake");
	    
	    JSONObject gameNotFound = new JSONObject();
	    gameNotFound.put("status", "failure");
	    gameNotFound.put("message", "Game not found.");

	    JSONObject playerNotTurn = new JSONObject();
	    playerNotTurn.put("status", "failure");
	    playerNotTurn.put("message", "Cannot make move outside of turn.");
	    
	    ResponseEntity<String> response = gc.chooseCity("fake", data);
		assertEquals(gameNotFound.toJSONString(), response.getBody());
	    data.replace("playerId", "testCreator");
	    
	    gc.chooseCity("TestGame", data);
	    
	    assertTrue("player has a city when it should have remvoed it", game.getCurrentPlayer().getInventory().getUnlockables().isEmpty());
	    

  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void miscTest() {
		JSONObject invalidAction = new JSONObject();
	    invalidAction.put("status", "failure");
	    invalidAction.put("message", "Invalid action.");
	  
	    SessionData dummy = createDummySessionData();
	    GameManager gameManager = new GameManager();
	    gameManager.launchGame("TestGame", dummy);
	    GameController gc = new GameController(gameManager, new SaveManager());
	    
	    
	    ResponseEntity<String> response = gc.getBoard("TestGame");
	    try {
	    	assertEquals(ResponseEntity.badRequest().body("No board"), response);
	    	fail("expected error not thrown");
	    } catch (AssertionError e) { }
	    
	    response = gc.getGame("TestGame");
	    try {
	    	assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
	    	fail("expected error not thrown");
	    } catch (AssertionError e) { }
  }
  
  @Test
  public void longPollingTest() {
	    SessionData dummy = createDummySessionData();
	    GameManager gameManager = new GameManager();
	    gameManager.launchGame("TestGame", dummy);
	    GameController gc = new GameController(gameManager, new SaveManager());
	    Optional<Board> board = gameManager.getGameBoard("TestGame");
	    
	    DeferredResult<String> response = gc.getBoard("TestGame", ""); //first call to long poll
	    while (!response.hasResult()) { //wait for return value
	    	
	    }
	    String hash = board.get().toJson().toJSONString();
	    assertEquals(response.getResult().toString(), hash); //validate return
	    
	    response = gc.getBoard("TestGame", hash); //pass it same as last hash
	    while (!response.hasResult()) { //wait for return value
	    	
	    }
	    assertEquals(response.getResult().toString(), hash); //validate return
	    
	    board.get().getCards().draw(CardLevel.LEVEL1, 0);
	    response = gc.getBoard("TestGame", hash); //pass it same as last hash
	    while (!response.hasResult()) { //wait for return value
	    	
	    }
	    assertEquals(response.getResult().toString(), board.get().toJson().toJSONString()); //validate return
	    
	    gameManager.deleteGame("TestGame"); //test pass lack of a board
	    response = gc.getBoard("TestGame", hash); //pass it same as last hash
	    while (!response.hasResult()) { //wait for return value
	    	
	    }
	    assertEquals(response.getResult().toString(), "End Session"); //validate return
	    
	    ResponseEntity<String> error = gc.errorResponse(new Exception("TEST"));
	    assertEquals("{\"message\":\"Error 500: TEST\",\"status\":\"failure\"}", error.getBody());
	    
	    gc.deleteGame(42069);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void endTurnTest() throws JsonProcessingException {
	    SessionData dummyOrient = createDummySessionData();
	    SessionData dummyTP = createDummyTradingPost();
	    SessionData dummyCity = createDummyCities();
	    GameManager gameManager = new GameManager();
	    gameManager.launchGame("TestOrient", dummyOrient);
	    gameManager.launchGame("TestTP", dummyTP);
	    gameManager.launchGame("TestCity", dummyCity);
	    Game gameOrient = gameManager.getGame("TestOrient");
	    Game gameTP = gameManager.getGame("TestTP");
	    Game gameCity = gameManager.getGame("TestCity");
	    GameController gc = new GameController(gameManager, new SaveManager());
	    JSONObject data = new JSONObject();
	    data.put("status", "success");
	    
	    ResponseEntity<String> response = gc.endTurnAction("TestOrient");
	    assertEquals(data.toString(), response.getBody().toString());
	    assertTrue("wrongPlayer", !gameOrient.getCurrentPlayer().getUsername().equals("testCreator"));
	    
	    response = gc.endTurnAction("TestTP");
	    assertEquals(data.toString(), response.getBody().toString());
	    assertTrue("wrongPlayer", !gameTP.getCurrentPlayer().getUsername().equals("testCreator"));
	    
	    response = gc.endTurnAction("TestCity");
	    assertEquals(data.toString(), response.getBody().toString());
	    
	    gameCity.getCurrentPlayer().getInventory().getUnlockables().add(UnlockableRegistry.of(14));
	    response = gc.endTurnAction("TestCity");
	    assertEquals(data.toString(), response.getBody().toString());
	    
	    gameCity.getCurrentPlayer().getInventory().getUnlockables().add(UnlockableRegistry.of(0));
	    gameCity.getCurrentPlayer().getInventory().getUnlockables().add(UnlockableRegistry.of(1));
	    response = gc.endTurnAction("TestCity");
	    assertTrue("didnt understand multiple city unlocks", !data.toString().equals(response.getBody().toString()));  
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void winTest() throws JsonProcessingException {
	    SessionData dummyOrient = createDummySessionData();
	    SessionData dummyCity = createDummyCities();
	    GameManager gameManager = new GameManager();
	    gameManager.launchGame("TestOrient", dummyOrient);
	    gameManager.launchGame("TestCity", dummyCity);
	    Game gameOrient = gameManager.getGame("TestOrient");
	    Game gameCity = gameManager.getGame("TestCity");
	    GameController gc = new GameController(gameManager, new SaveManager());
	    JSONObject data = new JSONObject();
	    data.put("status", "success");
	    
	    //test regular win conditions
	    gameOrient.getPlayers()[0].getInventory().changePoints(200);
	    gameOrient.getPlayers()[1].getInventory().changePoints(100);
	    assertEquals(gameOrient.getPlayerIds()[0], gameOrient.checkWinState());
	    gameOrient.getPlayers()[1].getInventory().changePoints(100);
	    assertEquals(gameOrient.getPlayerIds()[1] + ", " + gameOrient.getPlayerIds()[0], gameOrient.checkWinState());
	    gameOrient.getPlayers()[1].getInventory().addCard(CardRegistry.of(0));
	    gameOrient.getPlayers()[1].getInventory().changePoints(-CardRegistry.of(0).getPoints());
	    assertEquals(gameOrient.getPlayerIds()[0], gameOrient.checkWinState());
	    
	    //test city win conditions
	    gameCity.getPlayers()[0].getInventory().getUnlockables().add(UnlockableRegistry.of(0));
	    assertEquals(gameCity.getPlayerIds()[0], gameCity.checkWinState());
	    gameCity.getPlayers()[1].getInventory().getUnlockables().add(UnlockableRegistry.of(0));
	    assertEquals(gameCity.getPlayerIds()[0] + ", " + gameCity.getPlayerIds()[1], gameCity.checkWinState());
	    gameCity.getPlayers()[1].getInventory().addCard(CardRegistry.of(0));
	    assertEquals(gameCity.getPlayerIds()[0], gameCity.checkWinState());
	    gameCity.getPlayers()[1].getInventory().getUnlockables().clear();
	    gameCity.getPlayers()[1].getInventory().getUnlockables().add(UnlockableRegistry.of(14));
	    assertEquals(gameCity.getPlayerIds()[1], gameCity.checkWinState());
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void tokenReturnTest() throws JsonProcessingException {
	    SessionData dummy = createDummySessionData();
	    GameManager gameManager = new GameManager();
	    GameController gc = new GameController(gameManager, new SaveManager());
	    gc.launchGame("TestGame", dummy);
	    Game game = gameManager.getGame("TestGame");
	    
	    Token[] add = {Token.RED};
	    ArrayList<String> tokens = new ArrayList<String>();
	    tokens.add(Token.RED.toString());
	    game.getCurrentPlayer().getInventory().addTokens(add);
	    
	    JSONObject data = new JSONObject(), success = new JSONObject();
	    success.put("status", "success");
	    
	    data.put("playerId", "fake");
	    
	    JSONObject gameNotFound = new JSONObject();
	    gameNotFound.put("status", "failure");
	    gameNotFound.put("message", "Game not found.");

	    JSONObject playerNotTurn = new JSONObject();
	    playerNotTurn.put("status", "failure");
	    playerNotTurn.put("message", "Cannot make move outside of turn.");
	    
	    ResponseEntity<String> response = gc.returnTokens("fake", data);
		assertEquals(gameNotFound.toJSONString(), response.getBody());
	    response = gc.returnTokens("TestGame", data);
		assertEquals(playerNotTurn.toJSONString(), response.getBody());
	    data.replace("playerId", "testCreator");
	    
	    data.replace("playerId", "testCreator");
	    data.put("tokens", tokens);
	    
	    gc.returnTokens("TestGame", data);
	    assertEquals(0, game.getCurrentPlayer().getInventory().getTokens().checkAmount(Token.RED));
	    response = gc.returnTokens("TestGame", data);
	    assertTrue("returned tokens when it shouldnt have", !success.toString().equals(response.getBody().toString()));
  }
  
  @Test
  public void cardTests() {
	    SessionData dummy = createDummySessionData();
	    GameManager gameManager = new GameManager();
	    GameController gc = new GameController(gameManager, new SaveManager());
	    gc.launchGame("TestGame", dummy);
	    Game game = gameManager.getGame("TestGame");
	    Board board = game.getBoard();
	    Inventory testInventory = board.getInventory("testCreator");
	    
	    assertTrue("succeeded when should have failed cards1", !gameManager.acquireCard(null, board, testInventory));
	    
	    Card test = new Card(420, CardLevel.LEVEL1);
	    
	    assertTrue("succeeded when should have failed cards2", !gameManager.acquireCard(test, board, testInventory));
	    
	    testInventory.getReservedCards().add(test);
	    
	    assertTrue("failed when should have succeeded cards2", gameManager.acquireCard(test, board, testInventory));
  }
}