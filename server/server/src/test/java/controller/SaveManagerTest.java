package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.mcgill.splendorserver.apis.GameController;
import ca.mcgill.splendorserver.controllers.GameManager;
import ca.mcgill.splendorserver.controllers.SaveManager;
import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.Player;
import ca.mcgill.splendorserver.models.registries.CardRegistry;
import ca.mcgill.splendorserver.models.saves.SaveSession;
import utils.ControllerTestUtils;

public class SaveManagerTest {
	public static SaveManager saveManager = new SaveManager();
	public static GameManager gameManager = new GameManager();
	public static GameController gc = new GameController(gameManager, saveManager);

    private static final String randName1 = "test_dthyjgeui37gw8e7rtfgeo8yergdwiue";
    private static final String randName2 = "test_hed7837gurfuhsiuey3wrefhuweyeuhu";
    private static final String randName3 = "test_fe8d37tr8dwgey8dgw8g8dheg8deg8uw";

	private static Player josh = new Player(randName1);
	private static Player emma = new Player(randName2);
    private static Player jeremy = new Player(randName3);
	
	private static Game game = new Game("test", randName1, new Player[] {josh, emma, jeremy}, "splendor");
	
	private static String saveId = "";
	
	@BeforeClass
	public static void setTest() {
	  saveManager.test = true;
	}

	@Before
	public void initSaveDir() {
		saveManager.initPlayer(randName1);
		saveManager.initPlayer(randName2);
	}

	@Test
	public void saveAndLoadSaveGameTest() {
        saveId = saveManager.saveGame(game);

		SaveSession save = saveManager.loadGame(saveId, randName1);
		assertTrue("invalid save launch", save.isValidLaunch("splendor", 3));
		assertTrue("invalid save launch", !save.isValidLaunch("FAKE", 3));
		assertTrue("invalid save launch", !save.isValidLaunch("splendor", 0));
		assertEquals(randName1, save.getGame().getCreatorId());
		assertEquals(3, save.getGame().getPlayers().length);
		assertEquals("splendor", save.getGame().getVariant());
		assertEquals(saveId, save.getSavegameid());
		
		save.reassignPlayers(new String[] {josh.getUsername(), emma.getUsername(), jeremy.getUsername()});
		assertEquals(josh.getUsername(), save.getPlayersRequired()[0]);
		
		save.reassignPlayers(new String[] {josh.getUsername(), "emma", jeremy.getUsername()});
		assertEquals(josh.getUsername(), save.getPlayersRequired()[0]);
		
		save.reassignPlayers(new String[] {"josh", "jeremy", "emma"});
		assertEquals("josh", save.getPlayersRequired()[0]);
		
		assertTrue("Not getting list of saves", saveManager.getAllSavedGames().size() > 0);
	}

	@Test
	public void incorrectCreatorSaveGameTest() {
        saveId = saveManager.saveGame(game);

		//Game game = new Game("test", "josh", new Player[] {josh, emma, jeremy}, "splendor");
		//saved = saveManager.saveGame(game);
		
		SaveSession save = saveManager.loadGame(saveId, randName2);
		assertNull(save);
	}

	@Test
	public void saveAndLoadPlayedSaveGameTest() {
        game.getCurrentPlayer().getInventory().addCard(CardRegistry.of(1));
        saveId = saveManager.saveGame(game);

        SaveSession save = saveManager.loadGame(saveId, randName1);

		assertSame(1, save.getGame().getCurrentPlayer().getInventory().getCards().get(0).getId());
	}
	
	@Test
	public void saveThroughEndpointTest() {
        gc.launchGame("test", ControllerTestUtils.createDummySave(randName1, randName2, randName3));
        gc.save("test");
        
        saveId = "";
		
        assertEquals(1, saveManager.countSavedGamesOfUser(randName1));
	}

	@After
	public void deleteSave() {
	  saveManager.deleteSavedGamesOfUser(randName1);
      saveManager.deleteSavedGamesOfUser(randName2);
	}

}
