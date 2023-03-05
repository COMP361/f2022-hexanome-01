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
import ca.mcgill.splendorserver.models.Token;
import ca.mcgill.splendorserver.models.board.Board;
import ca.mcgill.splendorserver.models.cards.Card;
import ca.mcgill.splendorserver.models.cards.CardLevel;
import ca.mcgill.splendorserver.models.communicationbeans.SessionData;
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
	    String[] tokens = {"RED", "BLUE", "GREEN", "WHITE", "BLACK"};
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
	}
	    
}
