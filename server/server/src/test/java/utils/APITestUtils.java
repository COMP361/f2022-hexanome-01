package utils;

import ca.mcgill.splendorserver.models.SessionData;

public class APITestUtils {

  public SessionData createDummySessionData() {
    SessionData dummy = new SessionData("testCreator", "Orient",
        new SessionData.LobbyServicePlayerData[] {
            new SessionData.LobbyServicePlayerData("testPlayer1", "blue")}, "");
    return dummy;
  }
}
