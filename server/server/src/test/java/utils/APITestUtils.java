package utils;

import ca.mcgill.splendorserver.models.LobbyServicePlayerData;
import ca.mcgill.splendorserver.models.SessionData;
import java.util.LinkedList;

public class APITestUtils {

  public SessionData createDummySessionData() {
    LobbyServicePlayerData creator = new LobbyServicePlayerData("testCreator", "blue");
    LobbyServicePlayerData playerTest = new LobbyServicePlayerData("testPlayer", "red");
    LinkedList<LobbyServicePlayerData> playersTest = new LinkedList<>();
    playersTest.add(creator);
    playersTest.add(playerTest);
    SessionData dummy = new SessionData("testCreator", "Orient", playersTest, "");
    return dummy;
  }
}
