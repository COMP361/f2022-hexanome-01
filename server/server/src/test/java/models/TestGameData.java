package models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.*;

import ca.mcgill.splendorserver.models.*;

public class TestGameData {
	@Test
	public void testMethods() {
		GameData test = new GameData();
		PlayerData[] temp = {new PlayerData()};
		NobleData[] temp1 = {new NobleData()};
		CardData[][] temp2 = {{new CardData()}, {new CardData()}};
		
		test.setCards(temp2);
		test.setNobles(temp1);
		test.setPlayers(temp);
		
		for(int i = 0; i < temp.length;i++)
			assertEquals(test.getPlayers()[i], temp[i]);
		for(int i = 0; i < temp1.length;i++)
			assertEquals(test.getNobles()[i], temp1[i]);
		for(int i = 0; i < temp2.length;i++)
			for(int j = 0;j < temp2[i].length;j++)
			assertEquals(test.getCards()[i][j], temp2[i][j]);
	}
}