package utils;

import ca.mcgill.splendorserver.models.LobbyServicePlayerData;
import ca.mcgill.splendorserver.models.communicationbeans.SessionData;
import java.util.LinkedList;

public class ControllerTestUtils {

  public static SessionData createDummySessionData() {
    LobbyServicePlayerData creator = new LobbyServicePlayerData("testCreator", "blue");
    LobbyServicePlayerData playerTest = new LobbyServicePlayerData("testPlayer", "red");
    LinkedList<LobbyServicePlayerData> playersTest = new LinkedList<>();
    playersTest.add(creator);
    playersTest.add(playerTest);
    SessionData dummy = new SessionData("testCreator", "Orient", playersTest, "");
    return dummy;
  }
}