package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import ca.mcgill.splendorserver.controllers.OrientManager;
import ca.mcgill.splendorserver.apis.GameController;
import ca.mcgill.splendorserver.apis.JsonHandler;
import ca.mcgill.splendorserver.controllers.GameManager;
import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.Inventory;
import ca.mcgill.splendorserver.models.Player;
import ca.mcgill.splendorserver.models.communicationbeans.SessionData;
import ca.mcgill.splendorserver.models.registries.CardRegistry;
import ca.mcgill.splendorserver.models.board.Board;
import ca.mcgill.splendorserver.models.board.CardBank;
import ca.mcgill.splendorserver.models.cards.Card;
import ca.mcgill.splendorserver.models.cards.CardLevel;
import ca.mcgill.splendorserver.models.communicationbeans.ReserveCardData;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import utils.ControllerTestUtils;
/**
 * Tester for Orient endpoints and functionality
 */
public class OrientManagerTest {

	@Test
	  public void requestResponseTest() {
	    SessionData dummy = ControllerTestUtils.createDummySessionData();
	    GameManager gameManager = new GameManager();
	    gameManager.launchGame("TestGame", dummy);
	    HashMap<String, Game> gameRegistry = gameManager.getGameRegistry();


	    
	    GameController gc = new GameController();
	    JSONObject request = new JSONObject();
	    Game game = gameRegistry.get("TestGame");
	    Board board = game.getBoard();
	    Inventory testInventory = board.getInventory("testCreator");
	    String[] tokens = {"RED", "BLUE", "GREEN", "WHITE", "BLACK"};
	    testInventory.addTokens(tokens);
	    testInventory.addTokens(tokens);
	    testInventory.addTokens(tokens);
	    testInventory.addTokens(tokens);
	    testInventory.addTokens(tokens);
	    testInventory.addTokens(tokens);
	    testInventory.addTokens(tokens);
	    testInventory.addTokens(tokens);
	    testInventory.addTokens(tokens);
	    testInventory.addTokens(tokens);
	    testInventory.addTokens(tokens);
	    testInventory.addTokens(tokens);
	    
	    request.put("playerId", "testCreator");
	    request.put("cardId", board.getCards().getRows().get(CardLevel.ORIENT_LEVEL2)[0] + "");
	    ResponseEntity<String> response = gc.purchaseCard("TestGame", request);
	    System.out.println(request.toJSONString());
	    System.out.println(response.getBody());
	    
	  }
	
	  @Test
	  public void satchelTest() {
	    SessionData dummy = ControllerTestUtils.createDummySessionData();
	    GameManager gameManager = new GameManager();
	    gameManager.launchGame("TestGame", dummy);
	    HashMap<String, Game> gameRegistry = gameManager.getGameRegistry();

	    Inventory testInventory = new Inventory();
	    testInventory.addCard(CardRegistry.of(1));
	    JSONObject result1 = OrientManager.satchel(CardRegistry.of(1), testInventory);
	    String options1 = (String)result1.get("options");
	    assertEquals("[]", options1);
	    
	    JSONObject result2 = OrientManager.satchel(CardRegistry.of(2), testInventory);
	    String options2 = ((JSONArray)JsonHandler.decodeJsonRequest((String)result2.get("options"))).toJSONString();
	    assertEquals("[1]", options2);
	  }
	  
	  @Test
	  public void dominoTest() {
	    SessionData dummy = ControllerTestUtils.createDummySessionData();
	    GameManager gameManager = new GameManager();
	    gameManager.launchGame("TestGame", dummy);
	    HashMap<String, Game> gameRegistry = gameManager.getGameRegistry();

	    Inventory testInventory = new Inventory();
	    Game game = gameRegistry.get("TestGame");
	    Board board = game.getBoard();
	    ArrayList<Integer> list = new ArrayList<Integer>();
	    for (int i : board.getCards().getRows().get(CardLevel.LEVEL2)) {
	    	list.add(i);
	    }
	    for (int i : board.getCards().getRows().get(CardLevel.ORIENT_LEVEL2)) {
	    	list.add(i);
	    }
	    String cards = JSONArray.toJSONString(list);
	    
	    JSONObject result1 = OrientManager.domino(CardRegistry.of(1), board, testInventory);
	    String options1 = (String)result1.get("options");
	    assertEquals(cards, options1);
	    
	  }
	  
	  @Test
	  public void reserveTest() {
	    SessionData dummy = ControllerTestUtils.createDummySessionData();
	    GameManager gameManager = new GameManager();
	    gameManager.launchGame("TestGame", dummy);
	    HashMap<String, Game> gameRegistry = gameManager.getGameRegistry();

	    Inventory testInventory = new Inventory();
	    Game game = gameRegistry.get("TestGame");
	    Board board = game.getBoard();
	    ArrayList<Integer> list = new ArrayList<Integer>();
	    for (int i : board.getNobles().getNobles()) {
	    	list.add(i);
	    }
	    
	    String nobles = JSONArray.toJSONString(list);
	    JSONObject result1 = OrientManager.reserve(CardRegistry.of(1), board);
	    String options1 = (String)result1.get("options");
	    assertEquals(nobles, options1);
	    
	  }
	
}
