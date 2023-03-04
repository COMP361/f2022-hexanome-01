package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;

import utils.ControllerTestUtils;
/**
 * Tester for Orient endpoints and functionality
 */
public class OrientManagerTest {

	@Test
	  public void requestResponseTestNormal() throws JsonProcessingException {
		JSONObject invalidAction = new JSONObject();
	    invalidAction.put("status", "failure");
	    invalidAction.put("message", "Invalid action.");
		
	    SessionData dummy = ControllerTestUtils.createDummySessionData();
	    GameManager gameManager = new GameManager();
	    gameManager.launchGame("TestGame", dummy);
	    HashMap<String, Game> gameRegistry = gameManager.getGameRegistry();

	    GameController gc = new GameController();
	    JSONObject request = new JSONObject();
	    Game game = gameRegistry.get("TestGame");
	    Board board = game.getBoard();
	    Inventory testInventory = board.getInventory("testCreator");
	    
	    request.put("playerId", "testCreator");
	    request.put("cardId", board.getCards().getRows().get(CardLevel.LEVEL1)[0] + "");
	    ResponseEntity<String> response = gc.purchaseCard("TestGame", request);

	    try {
	      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
	    }
	    catch (AssertionError e) {
	    	fail("exception thrown");
	    }
	    
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
	    
	    response = gc.purchaseCard("TestGame", request);
	    try {
	      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
	      fail("expected exception not thrown");
	    }
	    catch (AssertionError e) {
	    	
	    }
	    
	    response = gc.satchel("TestGame", request);
	    try {
	      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
	      fail("expected exception not thrown");
	    }
	    catch (AssertionError e) { }
	    
	    response = gc.dominoSatchel("TestGame", request);
	    try {
	      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
	      fail("expected exception not thrown");
	    }
	    catch (AssertionError e) { }
	    
	    request.replace("cardId", board.getCards().getRows().get(CardLevel.LEVEL1)[0] + "");
	    response = gc.domino("TestGame", request);
	    try {
	      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
	      fail("expected exception not thrown");
	    }
	    catch (AssertionError e) { }
	    
	    request.replace("cardId", board.getCards().getRows().get(CardLevel.LEVEL1)[0] + "");
	    response = gc.reserveCardAction("TestGame", request);
	    try {
	      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
	      fail("expected exception not thrown");
	    }
	    catch (AssertionError e) { }
	    
	    request.put("nobleId", board.getNobles().getNobles()[0] + "");
	    response = gc.reserveNoble("TestGame", request);
	    try {
	      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
	      fail("expected exception not thrown");
	    }
	    catch (AssertionError e) { }
	    
	    request.replace("nobleId", board.getNobles().getNobles()[1] + "");
	    response = gc.claimNobleAction("TestGame", request);
	    try {
	      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
	      fail("expected exception not thrown");
	    }
	    catch (AssertionError e) { }
	  }
	
	@Test
	  public void requestResponseTestOrient() throws JsonProcessingException {
		JSONObject invalidAction = new JSONObject();
	    invalidAction.put("status", "failure");
	    invalidAction.put("message", "Invalid action.");
		
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
	    testInventory.addTokens(tokens);
	    testInventory.addTokens(tokens);
	    
	    request.put("playerId", "testCreator");
	    request.put("cardId", board.getCards().getRows().get(CardLevel.ORIENTLEVEL1)[0] + "");
	    gameManager.acquireCard(CardRegistry.of(0), board, testInventory);
	    gameManager.acquireCard(CardRegistry.of(2), board, testInventory);
	    gameManager.acquireCard(CardRegistry.of(3), board, testInventory);
	    gameManager.acquireCard(CardRegistry.of(4), board, testInventory);
	    ResponseEntity<String> response = gc.purchaseCard("TestGame", request);
	    
	    response = gc.purchaseCard("TestGame", request);
	    try {
	      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
	      fail("expected exception not thrown");
	    }
	    catch (AssertionError e) { }
	    
	    request.replace("cardId", board.getCards().getRows().get(CardLevel.ORIENTLEVEL1)[0] + "");
	    response = gc.purchaseCard("TestGame", request);
	    try {
	      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
	      fail("expected exception not thrown");
	    }
	    catch (AssertionError e) { }
	    
	    request.replace("cardId", board.getCards().getRows().get(CardLevel.ORIENTLEVEL1)[0] + "");
	    response = gc.purchaseCard("TestGame", request);
	    try {
	      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
	      fail("expected exception not thrown");
	    }
	    catch (AssertionError e) { }
	    
	    request.replace("cardId", board.getCards().getRows().get(CardLevel.ORIENTLEVEL2)[0] + "");
	    response = gc.dominoSatchel("TestGame", request);
	    try {
	      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
	      fail("expected exception not thrown");
	    }
	    catch (AssertionError e) { }
	    
	    request.replace("cardId", board.getCards().getRows().get(CardLevel.ORIENTLEVEL2)[0] + "");
	    response = gc.domino("TestGame", request);
	    try {
	      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
	      fail("expected exception not thrown");
	    }
	    catch (AssertionError e) { }
	    
	    request.replace("cardId", board.getCards().getRows().get(CardLevel.ORIENTLEVEL2)[0] + "");
	    response = gc.domino("TestGame", request);
	    try {
	      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
	      fail("expected exception not thrown");
	    }
	    catch (AssertionError e) { }
	    
	    request.replace("cardId", board.getCards().getRows().get(CardLevel.ORIENTLEVEL2)[0] + "");
	    response = gc.domino("TestGame", request);
	    try {
	      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
	      fail("expected exception not thrown");
	    }
	    catch (AssertionError e) { }
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
	    for (int i : board.getCards().getRows().get(CardLevel.ORIENTLEVEL2)) {
	    	list.add(i);
	    }
	    String cards = JSONArray.toJSONString(list);
	    
	    JSONObject result1 = OrientManager.domino(CardRegistry.of(1), board, testInventory);
	    String options1 = (String)result1.get("options");
	    assertEquals(cards, options1);
	  }
	  
	  @Test
	  public void dominoOptionsTest() {
	    SessionData dummy = ControllerTestUtils.createDummySessionData();
	    GameManager gameManager = new GameManager();
	    gameManager.launchGame("TestGame", dummy);
	    HashMap<String, Game> gameRegistry = gameManager.getGameRegistry();

	    Game game = gameRegistry.get("TestGame");
	    Board board = game.getBoard();
	    ArrayList<Integer> list = new ArrayList<Integer>();
	    for (int i : board.getCards().getRows().get(CardLevel.LEVEL1)) {
	    	list.add(i);
	    }
	    for (int i : board.getCards().getRows().get(CardLevel.ORIENTLEVEL1)) {
	    	list.add(i);
	    }
	    String cards = JSONArray.toJSONString(list);
	    
	    ArrayList<Integer> result1 = OrientManager.getDominoOptions(board, 1);
	    assertEquals(list, result1);
	    
	    list.clear();
	    for (int i : board.getCards().getRows().get(CardLevel.LEVEL2)) {
	    	list.add(i);
	    }
	    for (int i : board.getCards().getRows().get(CardLevel.ORIENTLEVEL2)) {
	    	list.add(i);
	    }
	    
	    result1 = OrientManager.getDominoOptions(board, 2);
	    assertEquals(list, result1);
	    
	    list.clear();
	    for (int i : board.getCards().getRows().get(CardLevel.LEVEL3)) {
	    	list.add(i);
	    }
	    for (int i : board.getCards().getRows().get(CardLevel.ORIENTLEVEL3)) {
	    	list.add(i);
	    }
	    
	    result1 = OrientManager.getDominoOptions(board, 3);
	    assertEquals(list, result1);
	    
	    list.clear();
	    
	    result1 = OrientManager.getDominoOptions(board, 4);
	    assertEquals(list, result1);
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
