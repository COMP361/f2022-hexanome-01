package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.mcgill.splendorserver.apis.GameController;
import ca.mcgill.splendorserver.controllers.GameManager;
import ca.mcgill.splendorserver.controllers.SaveManager;
import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.Player;
import ca.mcgill.splendorserver.models.cards.Card;
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
	
	private static Game game = new Game("test", "josh", new Player[] {josh, emma, jeremy}, "splendor");

	@BeforeClass
	public static void initSaveDir() {
		saveManager.initPlayer(randName1);
		saveManager.initPlayer(randName2);
	}

	@Test
	public void saveAndLoadSaveGameTest() {
        String saveId = saveManager.saveGame(game);

		SaveSession save = saveManager.loadGame(saveId, randName1);
		assertEquals(randName1, save.getGame().getCreatorId()); //HAHHHHH
		assertEquals(3, save.getGame().getPlayers().length);
		assertEquals("splendor", save.getGame().getVariant());
	}

	@Test
	public void incorrectCreatorSaveGameTest() {
		gc.launchGame("test", ControllerTestUtils.createDummySave(randName1, randName2, randName3));
		gc.save("test");

		//Game game = new Game("test", "josh", new Player[] {josh, emma, jeremy}, "splendor");
		//saved = saveManager.saveGame(game);

		List<SaveSession> saves = saveManager.getAllSavedGames();
		
		SaveSession save = saveManager.loadGame(saves.get(0).getSavegameid(), randName2);
		assertNull(save);
	}

	@Test
	public void saveAndLoadPlayedSaveGameTest() {
        gc.launchGame("test", ControllerTestUtils.createDummySave(randName1, randName2, randName3));
        gc.save("test");
        gameManager.getGame("test").getCurrentPlayer().getInventory().addCard(CardRegistry.of(1));
        String saveId = saveManager.saveGame(game);

        SaveSession save = saveManager.loadGame(saveId, randName1);

		assertSame(1, save.getGame().getCurrentPlayer().getInventory().getCards().get(0).getId()); //HAHAHAH
	}

	@After
	public void deleteSave() {

        saveManager.deleteTestSavefile("test", randName1);

	}

}
