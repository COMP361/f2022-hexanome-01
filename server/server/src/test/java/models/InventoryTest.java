package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;

import ca.mcgill.splendorserver.apis.GameController;
import ca.mcgill.splendorserver.apis.JsonHandler;
import ca.mcgill.splendorserver.controllers.GameManager;
import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.Inventory;
import ca.mcgill.splendorserver.models.Noble;
import ca.mcgill.splendorserver.models.Token;
import ca.mcgill.splendorserver.models.board.Board;
import ca.mcgill.splendorserver.models.cards.Card;
import ca.mcgill.splendorserver.models.cards.CardLevel;
import ca.mcgill.splendorserver.models.communicationbeans.SessionData;
import ca.mcgill.splendorserver.models.expansion.City;
import ca.mcgill.splendorserver.models.expansion.TradingPost;
import ca.mcgill.splendorserver.models.registries.CardRegistry;
import ca.mcgill.splendorserver.models.registries.NobleRegistry;
import ca.mcgill.splendorserver.models.registries.UnlockableRegistry;
import utils.ControllerTestUtils;

/**
 * Tester for general inventory functionality
 */
public class InventoryTest {
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
	    Token[] tokens = {Token.RED, Token.BLUE, Token.GREEN, Token.WHITE, Token.BLACK};
	    testInventory.addTokens(tokens);
	    testInventory.addTokens(tokens);
	    testInventory.addTokens(tokens);
	    testInventory.addTokens(tokens);
	    testInventory.addTokens(tokens);
	    testInventory.addTokens(tokens);
	    testInventory.getUnlockables().add(UnlockableRegistry.of(15));
	    testInventory.getUnlockables().add(UnlockableRegistry.of(16));
	    testInventory.getUnlockables().add(UnlockableRegistry.of(17));
	    testInventory.getUnlockables().add(UnlockableRegistry.of(18));
	    testInventory.getUnlockables().add(UnlockableRegistry.of(19));
	    testInventory.getUnlockables().add(UnlockableRegistry.of(0));
	    testInventory.addCard(CardRegistry.of(0));
	    testInventory.reserve(CardRegistry.of(1));
	    testInventory.addNobleToInventory((NobleRegistry.of(0)));
	    testInventory.reserveNoble((NobleRegistry.of(1)));
	    
	    JSONObject actual1 = testInventory.toJson();
	    assertEquals(testInventory.getPoints(), actual1.get("points"));
	    assertEquals(15, testInventory.getTradingPosts()[0].getId());
	    
	    assertTrue("cannot buy card", testInventory.getTokens().canPurchase(CardRegistry.of(3)));
	    
	    assertEquals(6, testInventory.getTokens().checkAmount(Token.RED));
	    testInventory.getTokens().removeAll(tokens);
	    assertEquals(5, testInventory.getTokens().checkAmount(Token.RED));
	}
	   
	@Test
	public void nobleBankTest() {
	    SessionData dummy = ControllerTestUtils.createDummySessionData();
	    GameManager gameManager = new GameManager();
	    gameManager.launchGame("TestGame", dummy);
	    HashMap<String, Game> gameRegistry = gameManager.getGameRegistry();
	    GameController gc = new GameController();
	    Game game = gameRegistry.get("TestGame");
	    Board board = game.getBoard();
	    Inventory testInventory = board.getInventory("testCreator");
	    
	    int originalNoble = board.getNobles().getNobles()[0];
	    board.getNobles().remove(0);
	    assertEquals(-1, board.getNobles().getNobles()[0]);
	    board.getNobles().add(originalNoble);
	    assertEquals(originalNoble, board.getNobles().getNobles()[0]);
	    
	    JSONArray json = board.getNobles().toJson();
	    assertEquals(originalNoble, json.get(0));
	    gameManager.acquireNoble(NobleRegistry.of(originalNoble), board, testInventory);
	    assertEquals(originalNoble, testInventory.getNobles().get(0).getId());
	}
	
	@Test
	public void cardBankTest() {
	    SessionData dummy = ControllerTestUtils.createDummySessionData();
	    GameManager gameManager = new GameManager();
	    gameManager.launchGame("TestGame", dummy);
	    HashMap<String, Game> gameRegistry = gameManager.getGameRegistry();
	    GameController gc = new GameController();
	    Game game = gameRegistry.get("TestGame");
	    Board board = game.getBoard();
	    Inventory testInventory = board.getInventory("testCreator");
	    
	    int originalCard = board.getCards().getRows().get(CardLevel.LEVEL1)[0];
	    int drawnCard = board.getCards().draw(CardLevel.LEVEL1, 0);
	    
	    assertEquals(originalCard, drawnCard);
	    
	    JSONArray[] json = board.getCards().toJson();
	    assertEquals(board.getCards().getRows().get(CardLevel.LEVEL1)[0], ((JSONArray) json[0].get(0)).get(0));
	}
	
	@Test
	public void acquireingPostTest() {
	    SessionData dummy = ControllerTestUtils.createDummySessionData();
	    GameManager gameManager = new GameManager();
	    gameManager.launchGame("TestGame", dummy);
	    HashMap<String, Game> gameRegistry = gameManager.getGameRegistry();
	    GameController gc = new GameController();
	    Game game = gameRegistry.get("TestGame");
	    Board board = game.getBoard();
	    Inventory testInventory = board.getInventory("testCreator");
	    testInventory.addNobleToInventory(NobleRegistry.of(0));
	    
	    for(int i = 10;i < 36;i++)
	    	testInventory.addCard(CardRegistry.of(i));
	    
	    for (TradingPost tp : UnlockableRegistry.getTradingPosts())
	    	tp.observe(game.getCurrentPlayer());
	    
	    for (City c : UnlockableRegistry.getCities())
	    	c.observe(game.getCurrentPlayer());
	    
	    assertEquals(15, testInventory.getUnlockables().get(0).getId());
	    assertEquals(13, testInventory.getPoints());
	    
	    
	    testInventory.getCards().clear();
	    
	    for (TradingPost tp : UnlockableRegistry.getTradingPosts())
	    	tp.observe(game.getCurrentPlayer());
	    
	    assertEquals(3, testInventory.getPoints());
	}
}
