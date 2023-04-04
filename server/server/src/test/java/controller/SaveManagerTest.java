package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
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

	private static Player josh = new Player("josh");
	private static Player emma = new Player("emma");
	private static Player jeremy = new Player("jeremy");
	
	private static Game game = new Game("test", "josh", new Player[] {josh, emma, jeremy}, "splendor");

	private static boolean saved;

	@BeforeClass
	public static void initSaveDir() {
		saveManager.initPlayer("josh");
		saveManager.initPlayer("emma");
		
		gc.launchGame("test", ControllerTestUtils.createDummySave());
	}

	@Test
	public void saveAndLoadSaveGameTest() {
		gc.save("test");
		List<SaveSession> saves = saveManager.getAllSavedGames();
		
		assertTrue(saved);
		
		SaveSession save = saveManager.loadGame(saves.get(0).getSavegameid(), "josh");
		assertEquals("josh", save.getGame().getCreatorId());
		assertEquals(3, save.getGame().getPlayers().length);
		assertEquals("splendor", save.getGame().getVariant());
	}

	@Test
	public void incorrectCreatorSaveGameTest() {
		gc.save("test");

		//Game game = new Game("test", "josh", new Player[] {josh, emma, jeremy}, "splendor");
		//saved = saveManager.saveGame(game);

		List<SaveSession> saves = saveManager.getAllSavedGames();
		
		SaveSession save = saveManager.loadGame(saves.get(0).getSavegameid(), "emma");
		assertNull(save);
	}

	@Test
	public void saveAndLoadPlayedSaveGameTest() {

		//Game game = new Game("test", "josh", new Player[] {josh, emma, jeremy}, "splendor");
		game.getCurrentPlayer().acquireCard(CardRegistry.of(1));
		gc.save("test");
		
		List<SaveSession> saves = saveManager.getAllSavedGames();

		SaveSession save = saveManager.loadGame(saves.get(0).getSavegameid(), "josh");
		assertSame(1, save.getGame().getCurrentPlayer().getLastAcquired().getId());
	}

	@After
	public void deleteSave() {
		
		for (SaveSession ss : saveManager.getAllSavedGames()) {
			saveManager.deleteTestSavefile(ss.getSavegameid(), "josh");
			saveManager.deleteTestSavefile(ss.getSavegameid(), "emma");
			saveManager.deleteTestSavefile(ss.getSavegameid(), "jeremy");
		}
	}

}
