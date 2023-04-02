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
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void freeTokensTest() throws JsonProcessingException {
	    SessionData dummy = createDummySessionData();
	    GameManager gameManager = new GameManager();
	    gameManager.launchGame("TestGame", dummy);
	    Game game = gameManager.getGame("TestGame");
	    GameController gc = new GameController(gameManager);
	    JSONObject data = new JSONObject();
	    data.put("playerId", "testCreator");
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
	    GameController gc = new GameController(gameManager);
	    JSONObject data = new JSONObject();
	    data.put("playerId", "testCreator");
	    data.put("cityId", ((CitiesBoard) game.getBoard()).getCities()[0]);
	    game.getCurrentPlayer().getInventory().getUnlockables().add(UnlockableRegistry.of(((CitiesBoard) game.getBoard()).getCities()[0]));
	     
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
	    GameController gc = new GameController(gameManager);
	    
	    
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
	    GameController gc = new GameController(gameManager);
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
	    assertEquals(response.getResult().toString(), ""); //validate return
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
	    GameController gc = new GameController(gameManager);
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
	    GameController gc = new GameController(gameManager);
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
	    assertEquals(gameCity.getPlayerIds()[0], gameCity.checkWinState());
  }

}