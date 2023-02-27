package apis;

import static org.junit.Assert.assertEquals;

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
        Game test = gameManager.launchGame("TestGame", dummy);
        Game output = gameManager.getGame("TestGame");
        assertEquals(test.getId(), test.getId());
    }
}
