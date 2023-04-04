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
  
  public static SessionData createDummyCities() {
	    LobbyServicePlayerData creator = new LobbyServicePlayerData("testCreator", "blue");
	    LobbyServicePlayerData playerTest = new LobbyServicePlayerData("testPlayer", "red");
	    LinkedList<LobbyServicePlayerData> playersTest = new LinkedList<>();
	    playersTest.add(creator);
	    playersTest.add(playerTest);
	    SessionData dummy = new SessionData("testCreator", "cities", playersTest, "");
	    return dummy;
	  }
  
  public static SessionData createDummyTradingPost() {
	    LobbyServicePlayerData creator = new LobbyServicePlayerData("testCreator", "blue");
	    LobbyServicePlayerData playerTest = new LobbyServicePlayerData("testPlayer", "red");
	    LinkedList<LobbyServicePlayerData> playersTest = new LinkedList<>();
	    playersTest.add(creator);
	    playersTest.add(playerTest);
	    SessionData dummy = new SessionData("testCreator", "tradingposts", playersTest, "");
	    return dummy;
	  }
  
  public static SessionData createDummySave(String name1, String name2, String name3) {
	    LobbyServicePlayerData creator = new LobbyServicePlayerData(name1, "blue");
	    LobbyServicePlayerData playerTest = new LobbyServicePlayerData(name2, "red");
	    LobbyServicePlayerData player2Test = new LobbyServicePlayerData(name3, "red");
	    LinkedList<LobbyServicePlayerData> playersTest = new LinkedList<>();
	    playersTest.add(creator);
	    playersTest.add(playerTest);
	    playersTest.add(player2Test);
	    SessionData dummy = new SessionData(name1, "splendor", playersTest, "");
	    return dummy;
	  }
}
