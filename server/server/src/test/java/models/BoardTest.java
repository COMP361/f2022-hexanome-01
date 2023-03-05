package models;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.junit.Test;

import ca.mcgill.splendorserver.apis.GameController;
import ca.mcgill.splendorserver.controllers.GameManager;
import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.Inventory;
import ca.mcgill.splendorserver.models.board.Board;
import ca.mcgill.splendorserver.models.board.CitiesBoard;
import ca.mcgill.splendorserver.models.cards.CardLevel;
import ca.mcgill.splendorserver.models.communicationbeans.SessionData;
import utils.ControllerTestUtils;

public class BoardTest {
	@Test
	public void boardTest() {
	    SessionData dummy = ControllerTestUtils.createDummySessionData();
	    GameManager gameManager = new GameManager();
	    gameManager.launchGame("TestGame", dummy);
	    HashMap<String, Game> gameRegistry = gameManager.getGameRegistry();
	    GameController gc = new GameController();
	    Game game = gameRegistry.get("TestGame");
	    Board board = game.getBoard();
	    CitiesBoard cBoard = new CitiesBoard("testCreator", game.getPlayers());
	    Inventory testInventory = board.getInventory("testCreator");
	    
	    JSONObject json = board.toJson();
	    assertEquals(board.getNobles().getNobles()[0], ((JSONArray) json.get("nobles")).get(0));
	    
	    JSONObject json2 = cBoard.toJson();
	    assertEquals(cBoard.getCities()[0], ((JSONArray) json2.get("cities")).get(0));
	    
	    cBoard.remove(0);
	    assertEquals(-1, cBoard.getCities()[0]);
	}
}
