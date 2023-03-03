package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import ca.mcgill.splendorserver.controllers.GameManager;
import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.Player;
import ca.mcgill.splendorserver.models.communicationbeans.SessionData;
import ca.mcgill.splendorserver.models.board.CardBank;
import ca.mcgill.splendorserver.models.cards.Card;
import ca.mcgill.splendorserver.models.cards.CardLevel;
import ca.mcgill.splendorserver.models.communicationbeans.ReserveCardData;
import java.util.HashMap;
import org.junit.Test;
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
  public void reserveCardTest() {
    SessionData dummy = createDummySessionData();
    GameManager gameManager = new GameManager();
    gameManager.launchGame("TestGame", dummy);
    Game game = gameManager.getGame("TestGame");
    CardBank cards = game.getBoard().getCards();
    int cardId = cards.getRows().get(CardLevel.LEVEL1)[0];
    ReserveCardData reserveCardData = new ReserveCardData("testPlayer", cardId, "");
    gameManager.reserveCard("TestGame", reserveCardData);

    boolean cardWasReserved = false;
    for (Player player : game.getPlayers()) {
      if (player.getUsername().equals("testPlayer")) {
        for (Card card : player.getInventory().getReservedCards()) {
          if (card.getId() == reserveCardData.getCard()) {
            cardWasReserved = true;
          }
        }
      }
    }
    assertTrue("Player didn't reserve card", cardWasReserved);
    assertTrue("Invalid Game", !gameManager.reserveCard("FakeGame", reserveCardData));
    //game.getBoard().setCards(new CardBank());
    //assertTrue("Empty Deck", !gameManager.reserveCard("TestGame", reserveCardData));
  }

  @Test
  public void reserveCardFromDeck() {
    SessionData dummy = createDummySessionData();
    GameManager gameManager = new GameManager();
    gameManager.launchGame("TestGame", dummy);
    Game game = gameManager.getGame("TestGame");
    ReserveCardData reserveCardData = new ReserveCardData("testPlayer", -1, "Level3");
    gameManager.reserveCard("TestGame", reserveCardData);

    boolean cardWasReserved = false;
    for (Player player : game.getPlayers()) {
      if (player.getUsername().equals("testPlayer")) {
        System.out.println(player.getInventory().getReservedCards().size());
        if (!player.getInventory().getReservedCards().isEmpty()) {
          cardWasReserved = true;
        }
      }
    }
    assertTrue(cardWasReserved);
  }
}
