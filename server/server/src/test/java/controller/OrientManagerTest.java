package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import ca.mcgill.splendorserver.controllers.OrientManager;
import ca.mcgill.splendorserver.controllers.SaveManager;
import ca.mcgill.splendorserver.apis.GameController;
import ca.mcgill.splendorserver.apis.JsonHandler;
import ca.mcgill.splendorserver.controllers.GameManager;
import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.Inventory;
import ca.mcgill.splendorserver.models.Player;
import ca.mcgill.splendorserver.models.Token;
import ca.mcgill.splendorserver.models.communicationbeans.SessionData;
import ca.mcgill.splendorserver.models.registries.CardRegistry;
import ca.mcgill.splendorserver.models.registries.UnlockableRegistry;
import utils.ControllerTestUtils;
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

/**
 * Tester for Orient endpoints and functionality
 */
public class OrientManagerTest {

	@SuppressWarnings("unchecked")
	@Test
	  public void requestResponseTestNormal() throws JsonProcessingException {
		JSONObject invalidAction = new JSONObject();
	    invalidAction.put("status", "failure");
	    invalidAction.put("message", "Invalid action.");
	    
	    JSONObject gameNotFound = new JSONObject();
	    gameNotFound.put("status", "failure");
	    gameNotFound.put("message", "Game not found.");

	    JSONObject playerNotTurn = new JSONObject();
	    playerNotTurn.put("status", "failure");
	    playerNotTurn.put("message", "Cannot make move outside of turn.");
	    
		
	    SessionData dummy = ControllerTestUtils.createDummySessionData();
	    GameManager gameManager = new GameManager();
	    gameManager.launchGame("TestGame", dummy);
	    HashMap<String, Game> gameRegistry = gameManager.getGameRegistry();

	    GameController gc = new GameController(gameManager, new SaveManager());
	    JSONObject request = new JSONObject();
	    Game game = gameRegistry.get("TestGame");
	    Board board = game.getBoard();
	    Inventory testInventory = board.getInventory("testCreator");
	    
	    request.put("playerId", "testCreator");
	    request.put("cardId", board.getCards().getRows().get(CardLevel.LEVEL1)[0]);
	    ResponseEntity<String> response = gc.purchaseCard("TestGame", request);

	    try {
	      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
	    } catch (AssertionError e) {
	    	fail("exception thrown");
	    }
	    
	    Token[] tokens = {Token.RED, Token.BLUE, Token.GREEN, Token.WHITE, Token.BLACK, Token.GOLD};
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
	    } catch (AssertionError e) { }
	    
	    response = gc.satchel("TestGame", request);
	    try {
	      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
	      fail("expected exception not thrown");
	    } catch (AssertionError e) { }
	    
	    response = gc.dominoSatchel("TestGame", request);
	    try {
	      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
	      fail("expected exception not thrown");
	    } catch (AssertionError e) { }
	    
	    request.replace("cardId", board.getCards().getRows().get(CardLevel.LEVEL1)[0]);
	    response = gc.domino("TestGame", request);
	    try {
	      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
	      fail("expected exception not thrown");
	    } catch (AssertionError e) { }
	    
	    //reserve card from board
	    request.replace("cardId", board.getCards().getRows().get(CardLevel.LEVEL1)[0]);
	    request.put("source", "board");
	    response = gc.reserveCard("TestGame", request);
	    try {
	      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
	      fail("expected exception not thrown");
	    } catch (AssertionError e) { }
	    
	    //reserve card from deck
	    //request.replace("cardId", board.getCards().drawCardFromDeck(CardLevel.LEVEL1) + "");
	    request.replace("source", "deck");
	    request.put("deckId", "LEVEL1");
	    response = gc.reserveCard("TestGame", request);
	    try {
	      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
	      fail("expected exception not thrown");
	    } catch (AssertionError e) { }
	    
	    request.put("nobleId", board.getNobles().getNobles()[0]);
	    response = gc.reserveNoble("TestGame", request);
	    try {
	      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
	      fail("expected exception not thrown");
	    } catch (AssertionError e) { }
	    
	    request.replace("nobleId", board.getNobles().getNobles()[1]);
	    response = gc.claimNobleAction("TestGame", request);
	    try {
	      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
	      fail("expected exception not thrown");
	    } catch (AssertionError e) { }
	  }
	
	@SuppressWarnings("unchecked")
	@Test
	  public void requestResponseTestOrient() throws JsonProcessingException {
		JSONObject invalidAction = new JSONObject();
	    invalidAction.put("status", "failure");
	    invalidAction.put("message", "Invalid action.");
		
	    SessionData dummy = ControllerTestUtils.createDummySessionData();
	    GameManager gameManager = new GameManager();
	    gameManager.launchGame("TestGame", dummy);
	    HashMap<String, Game> gameRegistry = gameManager.getGameRegistry();

	    GameController gc = new GameController(gameManager, new SaveManager());
	    JSONObject request = new JSONObject();
	    Game game = gameRegistry.get("TestGame");
	    Board board = game.getBoard();
	    Inventory testInventory = board.getInventory("testCreator");
	    
	    Token[] tokens = {Token.RED, Token.BLUE, Token.GREEN, Token.WHITE, Token.BLACK, Token.GOLD};
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
	    request.put("cardId", board.getCards().getRows().get(CardLevel.ORIENTLEVEL1)[0]);
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
	    
	    request.replace("cardId", board.getCards().getRows().get(CardLevel.ORIENTLEVEL1)[0]);
	    response = gc.purchaseCard("TestGame", request);
	    try {
	      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
	      fail("expected exception not thrown");
	    }
	    catch (AssertionError e) { }
	    
	    request.replace("cardId", board.getCards().getRows().get(CardLevel.ORIENTLEVEL1)[0]);
	    response = gc.purchaseCard("TestGame", request);
	    try {
	      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
	      fail("expected exception not thrown");
	    }
	    catch (AssertionError e) { }
	    
	    request.replace("cardId", board.getCards().getRows().get(CardLevel.ORIENTLEVEL2)[0]);
	    response = gc.dominoSatchel("TestGame", request);
	    try {
	      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
	      fail("expected exception not thrown");
	    }
	    catch (AssertionError e) { }
	    
	    request.replace("cardId", board.getCards().getRows().get(CardLevel.ORIENTLEVEL2)[0]);
	    response = gc.domino("TestGame", request);
	    try {
	      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
	      fail("expected exception not thrown");
	    }
	    catch (AssertionError e) { }
	    
	    request.replace("cardId", board.getCards().getRows().get(CardLevel.ORIENTLEVEL2)[0]);
	    response = gc.domino("TestGame", request);
	    try {
	      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
	      fail("expected exception not thrown");
	    }
	    catch (AssertionError e) { }
	    
	    
	    JSONArray tokens2 = new JSONArray();
	    tokens2.add("RED");
	    tokens2.add("RED");
	    request.put("tokens", tokens2);
	    response = gc.takeTokens("TestGame", request);
	    try {
	      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
	      fail("expected exception not thrown");
	    } catch (AssertionError e) { }
	    
	    tokens2.clear();
	    tokens2.add("RED");
	    tokens2.add("GREEN");
	    tokens2.add("BLUE");
	    request.replace("tokens", tokens2);
	    response = gc.takeTokens("TestGame", request);
	    try {
	      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
	      fail("expected exception not thrown");
	    } catch (AssertionError e) { }
	    
	    
	    testInventory.getUnlockables().add(UnlockableRegistry.of(15));
	    testInventory.getUnlockables().add(UnlockableRegistry.of(16));
	    testInventory.getUnlockables().add(UnlockableRegistry.of(17));
	    testInventory.getUnlockables().add(UnlockableRegistry.of(18));
	    testInventory.getUnlockables().add(UnlockableRegistry.of(19));
	    tokens2.clear();
	    tokens2.add("RED");
	    tokens2.add("RED");
	    tokens2.add("BLUE");
	    request.replace("tokens", tokens2);
	    game.setVariant("tradingposts");
	    response = gc.takeTokens("TestGame", request);
	    try {
	      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
	      fail("expected exception not thrown");
	    } catch (AssertionError e) { }
	    
	    tokens2.clear();
	    tokens2.add("RED");
	    tokens2.add("RED");
	    tokens2.add("RED");
	    request.replace("tokens", tokens2);
	    response = gc.takeTokens("TestGame", request);
	    try {
	      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
	      fail("expected exception not thrown");
	    } catch (AssertionError e) { }
	  }
	
	  @Test
	  public void satchelTest() {
	    SessionData dummy = ControllerTestUtils.createDummySessionData();
	    GameManager gameManager = new GameManager();
	    gameManager.launchGame("TestGame", dummy);

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
	  
	  @SuppressWarnings("unchecked")
	  @Test
	  public void sacrificeChoiceTest() throws JsonProcessingException {
			JSONObject invalidAction = new JSONObject();
		    invalidAction.put("status", "failure");
		    invalidAction.put("message", "Invalid action.");
			
		    SessionData dummy = ControllerTestUtils.createDummySessionData();
		    GameManager gameManager = new GameManager();
		    gameManager.launchGame("TestGame", dummy);
		    HashMap<String, Game> gameRegistry = gameManager.getGameRegistry();

		    GameController gc = new GameController(gameManager, new SaveManager());
		    JSONObject request = new JSONObject();
		    Game game = gameRegistry.get("TestGame");
		    Board board = game.getBoard();
		    Inventory testInventory = board.getInventory("testCreator");
		    
		    testInventory.addCard(CardRegistry.of(4));
		    testInventory.addCard(CardRegistry.of(5));
		    
		    request.put("playerId", "testCreator");
		    request.put("cardId1", 4);
		    request.put("cardId2", 5);
		    request.put("originalId", 131);
		    gc.sacrifice("TestGame", request);
		    
		    assertEquals(1, testInventory.getCards().size());
		    assertEquals(131, testInventory.getCards().get(0).getId().intValue());
		    
		    
		    testInventory.addCard(CardRegistry.of(125));
		    
		    request.replace("cardId1", 125);
		    request.remove("cardId2");
		    request.put("originalId", 131);
		    gc.sacrifice("TestGame", request);
		    
		    assertEquals(2, testInventory.getCards().size());
		    assertEquals(131, testInventory.getCards().get(1).getId().intValue());
		    
		    
		    testInventory.addCard(CardRegistry.of(125));
		    
		    request.put("cardId2", 125);
		    request.remove("cardId1");
		    request.put("originalId", 131);
		    gc.sacrifice("TestGame", request);
		    
		    assertEquals(3, testInventory.getCards().size());
		    assertEquals(131, testInventory.getCards().get(2).getId().intValue());
	  }
	
	  @SuppressWarnings("unchecked")
	  @Test
	  public void sacrificeActionTest() throws JsonProcessingException {
			JSONObject invalidAction = new JSONObject();
		    invalidAction.put("status", "failure");
		    invalidAction.put("message", "Invalid action.");
			
		    SessionData dummy = ControllerTestUtils.createDummySessionData();
		    GameManager gameManager = new GameManager();
		    gameManager.launchGame("TestGame", dummy);
		    HashMap<String, Game> gameRegistry = gameManager.getGameRegistry();

		    GameController gc = new GameController(gameManager, new SaveManager());
		    JSONObject request = new JSONObject();
		    Game game = gameRegistry.get("TestGame");
		    Board board = game.getBoard();
		    Inventory testInventory = board.getInventory("testCreator");
		    
		    testInventory.addCard(CardRegistry.of(4));
		    testInventory.addCard(CardRegistry.of(5));
		    testInventory.addCard(CardRegistry.of(1));
		    testInventory.addCard(CardRegistry.of(3));
		    
		    request.put("playerId", "testCreator");
		    request.put("cardId", 131);
		    ResponseEntity<String> response = gc.purchaseCard("TestGame", request); 
		    try { //test trying to buy sacrifice card with 2 valid cards
			      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
			      fail("expected exception not thrown");
			    } catch (AssertionError e) { }
		    
		    testInventory.removeCard(CardRegistry.of(4));
		    testInventory.getCards().get(0).addSatchel();
		    
		    response = gc.purchaseCard("TestGame", request);
		    try { //test trying to buy sacrifice card with 1 valid card with satchel
			      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
			      fail("expected exception not thrown");
			    } catch (AssertionError e) { }
		    
		    testInventory.removeCard(CardRegistry.of(5));
		    
		    response = gc.purchaseCard("TestGame", request);
		    try { //test invalid purchase attempt
			      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
			    } catch (AssertionError e) { 
			    	fail("exception thrown");
			    }
		    
		    testInventory.removeCard(CardRegistry.of(1));
		    testInventory.removeCard(CardRegistry.of(3));
		    
		    //following tests are invalid purchases, but for other necessary colours
		    request.replace("cardId", 120);
		    response = gc.purchaseCard("TestGame", request);
		    try { //test invalid purchase attempt
			      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
			    } catch (AssertionError e) { 
			    	fail("exception thrown");
			    }
		    
		    request.replace("cardId", 121);
		    response = gc.purchaseCard("TestGame", request);
		    try { //test invalid purchase attempt
			      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
			    } catch (AssertionError e) { 
			    	fail("exception thrown");
			    }
		    
		    request.replace("cardId", 122);
		    response = gc.purchaseCard("TestGame", request);
		    try { //test invalid purchase attempt
			      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
			    } catch (AssertionError e) { 
			    	fail("exception thrown");
			    }
		    
		    request.replace("cardId", 130);
		    response = gc.purchaseCard("TestGame", request);
		    try { //test invalid purchase attempt
			      assertEquals(ResponseEntity.ok().body(invalidAction.toJSONString()), response);
			    } catch (AssertionError e) { 
			    	fail("exception thrown");
			    }
	  }
	  
	  @Test
	  public void endpointFailure() throws JsonProcessingException {
			JSONObject invalidAction = new JSONObject();
		    invalidAction.put("status", "failure");
		    invalidAction.put("message", "Invalid action.");
		    
		    JSONObject gameNotFound = new JSONObject();
		    gameNotFound.put("status", "failure");
		    gameNotFound.put("message", "Game not found.");

		    JSONObject playerNotTurn = new JSONObject();
		    playerNotTurn.put("status", "failure");
		    playerNotTurn.put("message", "Cannot make move outside of turn.");
		    
		    SessionData dummy = ControllerTestUtils.createDummySessionData();
		    GameManager gameManager = new GameManager();
		    gameManager.launchGame("TestGame", dummy);
		    Game game = gameManager.getGame("TestGame");
		    GameController gc = new GameController(gameManager, new SaveManager());
		    
		    JSONObject data = new JSONObject();
		    data.put("playerId", "fake");
		    
		    ResponseEntity<String> response;
		    
		    response = gc.claimNobleAction("fake", data);
			assertEquals(gameNotFound.toJSONString(), response.getBody());
		    response = gc.claimNobleAction("TestGame", data);
			assertEquals(playerNotTurn.toJSONString(), response.getBody());
			
		    response = gc.domino("fake", data);
			assertEquals(gameNotFound.toJSONString(), response.getBody());
		    response = gc.domino("TestGame", data);
			assertEquals(playerNotTurn.toJSONString(), response.getBody());
			
		    response = gc.dominoSatchel("fake", data);
			assertEquals(gameNotFound.toJSONString(), response.getBody());
		    response = gc.dominoSatchel("TestGame", data);
			assertEquals(playerNotTurn.toJSONString(), response.getBody());
			
		    response = gc.purchaseCard("fake", data);
			assertEquals(gameNotFound.toJSONString(), response.getBody());
		    response = gc.purchaseCard("TestGame", data);
			assertEquals(playerNotTurn.toJSONString(), response.getBody());
			
		    response = gc.reserveCard("fake", data);
			assertEquals(gameNotFound.toJSONString(), response.getBody());
		    response = gc.reserveCard("TestGame", data);
			assertEquals(playerNotTurn.toJSONString(), response.getBody());
			
		    response = gc.reserveNoble("fake", data);
			assertEquals(gameNotFound.toJSONString(), response.getBody());
		    response = gc.reserveNoble("TestGame", data);
			assertEquals(playerNotTurn.toJSONString(), response.getBody());
			
		    response = gc.sacrifice("fake", data);
			assertEquals(gameNotFound.toJSONString(), response.getBody());
		    response = gc.sacrifice("TestGame", data);
			assertEquals(playerNotTurn.toJSONString(), response.getBody());
			
		    response = gc.satchel("fake", data);
			assertEquals(gameNotFound.toJSONString(), response.getBody());
		    response = gc.satchel("TestGame", data);
			assertEquals(playerNotTurn.toJSONString(), response.getBody());
			
		    response = gc.takeTokens("fake", data);
			assertEquals(gameNotFound.toJSONString(), response.getBody());
		    response = gc.takeTokens("TestGame", data);
			assertEquals(playerNotTurn.toJSONString(), response.getBody());
			
		    response = gc.save("fake");
			assertEquals(gameNotFound.toJSONString(), response.getBody());
			
			response = gc.getGame("fake");
			assertEquals(gameNotFound.toJSONString(), response.getBody());

			response = gc.endTurnAction("fake");
			assertEquals(gameNotFound.toJSONString(), response.getBody());
	  }
}
