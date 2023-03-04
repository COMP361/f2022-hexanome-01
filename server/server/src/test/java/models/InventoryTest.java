package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;

import ca.mcgill.splendorserver.apis.GameController;
import ca.mcgill.splendorserver.controllers.GameManager;
import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.Inventory;
import ca.mcgill.splendorserver.models.board.Board;
import ca.mcgill.splendorserver.models.cards.CardLevel;
import ca.mcgill.splendorserver.models.communicationbeans.ReserveCardData;
import ca.mcgill.splendorserver.models.communicationbeans.SessionData;
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
	    
	    
	    assertEquals("{\"acquiredNobles\":\"[]\",\"reservedNobles\":\"[]\",\"reservedCards\":\"[]\",\"tokens\":\"{\\\"RED\\\":0,\\\"GREEN\\\":0,\\\"WHITE\\\":0,\\\"BLUE\\\":0,\\\"BLACK\\\":0,\\\"GOLD\\\":0}\",\"acquiredCards\":\"[]\"}", 
	    		testInventory.toJsonString());
	    
	    assertEquals("{\"acquiredNobles\":[],\"reservedNobles\":[],\"reservedCards\":[],\"tokens\":{\"red\":0,\"gold\":0,\"green\":0,\"white\":0,\"blue\":0,\"black\":0},\"bonuses\":{\"red\":0,\"gold\":0,\"green\":0,\"white\":0,\"blue\":0,\"black\":0},\"acquiredCards\":[],\"points\":0}", 
	    		testInventory.toJson().toString());
	}
	    
}
