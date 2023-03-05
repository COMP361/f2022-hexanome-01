package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import ca.mcgill.splendorserver.apis.GameController;
import ca.mcgill.splendorserver.controllers.GameManager;
import ca.mcgill.splendorserver.controllers.OrientManager;
import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.Inventory;
import ca.mcgill.splendorserver.models.Player;
import ca.mcgill.splendorserver.models.communicationbeans.SessionData;
import ca.mcgill.splendorserver.models.registries.NobleRegistry;
import ca.mcgill.splendorserver.models.board.Board;
import ca.mcgill.splendorserver.models.board.CardBank;
import ca.mcgill.splendorserver.models.cards.Card;
import ca.mcgill.splendorserver.models.cards.CardLevel;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;

import utils.ControllerTestUtils;

/**
 * Tester for Game controller endpoints and functionality
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
    JSONObject request = new JSONObject();
    Board board = game.getBoard();
    Inventory testInventory = board.getInventory("testCreator");
    
    gameManager.acquireNoble(NobleRegistry.of(board.getNobles().getNobles()[0]), board, testInventory);
    assertEquals(NobleRegistry.of(board.getNobles().getNobles()[0]), testInventory.getNobles().get(0));
    
    OrientManager.reserveNoble(NobleRegistry.of(board.getNobles().getNobles()[0]), board, testInventory);
    assertEquals(NobleRegistry.of(board.getNobles().getNobles()[0]), testInventory.getReservedNobles().get(0));
  }

  @Test
  public void reserveCardTest() throws JsonProcessingException {
    GameController gc = new GameController();
    SessionData dummy = createDummySessionData();
    GameManager gameManager = new GameManager();
    gameManager.launchGame("TestGame", dummy);
    Game game = gameManager.getGame("TestGame");
    CardBank cards = game.getBoard().getCards();
    int cardId = cards.getRows().get(CardLevel.LEVEL1)[0];
    
    JSONObject request = new JSONObject();
    request.put("cardId", cardId + "");
    request.put("playerId", "testCreator");

    //ResponseEntity<String> response = gc.reserveCardAction("TestGame", request);

    boolean cardWasReserved = false;
    for (Player player : game.getPlayers()) {
      if (player.getUsername().equals("testPlayer")) {
        for (Card card : player.getInventory().getReservedCards()) {
          //if (card.getId() == reserveCardData.getCard()) {
            //cardWasReserved = true;
          //}
        }
      }
    }
    //assertTrue("Player didn't reserve card", cardWasReserved);
    //assertTrue("Invalid Game", !gameManager.reserveCard("FakeGame", reserveCardData));
    //game.getBoard().setCards(new CardBank());
    //assertTrue("Empty Deck", !gameManager.reserveCard("TestGame", reserveCardData));
  }

  @Test
  public void reserveCardFromDeck() {
    SessionData dummy = createDummySessionData();
    GameManager gameManager = new GameManager();
    gameManager.launchGame("TestGame", dummy);
    Game game = gameManager.getGame("TestGame");
    String reserveCardData;
	//gameManager.reserveCard("TestGame", reserveCardData);

    boolean cardWasReserved = false;
    for (Player player : game.getPlayers()) {
      if (player.getUsername().equals("testPlayer")) {
        System.out.println(player.getInventory().getReservedCards().size());
        if (!player.getInventory().getReservedCards().isEmpty()) {
          cardWasReserved = true;
        }
      }
    }
    //assertTrue(cardWasReserved);
  }
}
