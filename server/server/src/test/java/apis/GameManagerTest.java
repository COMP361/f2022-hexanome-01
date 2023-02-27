package apis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import ca.mcgill.splendorserver.apis.GameController;
import ca.mcgill.splendorserver.apis.JsonHandler;
import ca.mcgill.splendorserver.controllers.GameManager;
import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.SessionData;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;
import utils.APITestUtils;

/**
 * Tester for Game controller endpoints and functionality
 */
public class GameManagerTest extends APITestUtils {

    @Test
    public void launchGameTest(){
        SessionData dummy = createDummySessionData();
        GameManager gameManager = new GameManager();
        gameManager.launchGame("TestGame", dummy);
        HashMap<String, Game> gameRegistry= gameManager.getGameRegistry();
        assertTrue(gameRegistry.containsKey("TestGame"));
    }

    @Test
    public void getGameTest(){
        SessionData dummy = createDummySessionData();
        GameManager gameManager = new GameManager();
        gameManager.launchGame("TestGame", dummy);
        Game game = gameManager.getGame("TestGame");
        assertEquals("TestGame", game.getId());
    }
}
