package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

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
	
	

	private Player josh = new Player("josh");
	private Player emma = new Player("emma");
	private Player jeremy = new Player("jeremy");
	
	Game game = new Game("test", "josh", new Player[] {josh, emma, jeremy}, "splendor");
	private boolean saved = saveManager.saveGame(game);


	@BeforeClass
	public static void initSaveDir() {
		gc.launchGame("test", ControllerTestUtils.createDummySave());
		saveManager.initPlayer("josh");
		saveManager.initPlayer("emma");
	}

	@Test
	public void saveAndLoadSaveGameTest() {
		gc.save("test");
		List<SaveSession> saves = saveManager.getAllSavedGames();
		
		SaveSession save = saveManager.loadGame(saves.get(0).getSavegameid(), "josh");
		assertEquals("josh", save.getGame().getCreatorId());
		assertEquals(3, save.getGame().getPlayers().length);
		assertEquals("splendor", save.getGame().getVariant());
	}

	@Test
	public void incorrectCreatorSaveGameTest() {

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
		saveManager.saveGame(game);
		
		List<SaveSession> saves = saveManager.getAllSavedGames();

		SaveSession save = saveManager.loadGame(saves.get(0).getSavegameid(), "josh");
		//assertSame(1, save.getGame().getCurrentPlayer().getLastAcquired().getId());
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
