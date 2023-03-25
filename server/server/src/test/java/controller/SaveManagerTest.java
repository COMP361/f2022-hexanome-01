package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import ca.mcgill.splendorserver.controllers.SaveManager;
import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.Player;
import ca.mcgill.splendorserver.models.registries.CardRegistry;
import ca.mcgill.splendorserver.models.saves.SaveSession;
import utils.ControllerTestUtils;

public class SaveManagerTest extends ControllerTestUtils {
	
	private Player josh = new Player("josh");
	private Player emma = new Player("emma");
	private Player jeremy = new Player("jeremy");

	@Test
	public void saveAndLoadSaveGameTest() {
		Game game = new Game("testGame", "josh", new Player[] {josh, emma, jeremy}, "splendor");
		SaveManager.saveGame(game);
		
		SaveSession save = SaveManager.loadGame("testGame", "josh");
		assertEquals("josh", save.getGame().getCreatorId());
		assertEquals(3, save.getGame().getPlayers().length);
		assertEquals("splendor", save.getGame().getVariant());
	}
	
	@Test
	public void incorrectCreatorSaveGameTest() {
		Game game = new Game("testGame", "josh", new Player[] {josh, emma, jeremy}, "splendor");
		SaveManager.saveGame(game);
		
		SaveSession save = SaveManager.loadGame("testGame", "emma");
		assertNull(save);
	}
	
	@Test
	public void saveAndLoadPlayedSaveGameTest() {
		Game game = new Game("testGame", "josh", new Player[] {josh, emma, jeremy}, "splendor");
		game.getCurrentPlayer().acquireCard(CardRegistry.of(1));
		
		SaveManager.saveGame(game);
		
		SaveSession save = SaveManager.loadGame("testGame", "josh");
		assertSame(1, save.getGame().getCurrentPlayer().getLastAcquired().getId());
	}
	
}
