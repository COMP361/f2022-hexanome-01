package models;

import static org.junit.Assert.assertEquals;

import org.junit.*;

import ca.mcgill.splendorserver.models.*;

public class TestGameData {
	@Test
	public void testMethods() {
		GameData test = new GameData(new Game());
		PlayerData[] temp = {new PlayerData("playerId")};
		NobleData[] temp1 = {new NobleData()};
		CardData[] temp2 = {new CardData()};
		test.setNobles(temp1);
		test.setPlayers(temp);
		test.setGameId("test game");
		test.setCurrentPlayer(temp[0]);
		test.setExRow1(temp2);
		test.setExRow2(temp2);
		test.setExRow3(temp2);
		test.setRow1(temp2);
		test.setRow2(temp2);
		test.setRow3(temp2);

		for(int i = 0; i < temp.length;i++)
			assertEquals(test.getPlayers()[i], temp[i]);
		for(int i = 0; i < temp1.length;i++)
			assertEquals(test.getNobles()[i], temp1[i]);
		assertEquals(test.getExRow1()[0], temp2[0]);
		assertEquals(test.getExRow2()[0], temp2[0]);
		assertEquals(test.getExRow3()[0], temp2[0]);
		assertEquals(test.getRow1()[0], temp2[0]);
		assertEquals(test.getRow2()[0], temp2[0]);
		assertEquals(test.getRow3()[0], temp2[0]);
		assertEquals(test.getGameId(), "test game");
		assertEquals(test.getCurrentPlayer(), temp[0]);
	}
}
