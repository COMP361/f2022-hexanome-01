package ca.mcgill.splendorserver.models;

public class GameData {
	private CardData[][] cards;
	private NobleData[] noblesDisplayed;
    private PlayerData[] playersInGame; //idk where this data is stored/how to get this data
    
	public void setCards(CardData[][] cards) {
		this.cards = cards;
	}

	public void setNobles(NobleData[] noblesDisplayed) {
		this.noblesDisplayed = noblesDisplayed;
	}

	public void setPlayers(PlayerData[] playersInGame) {
		this.playersInGame = playersInGame;
	}

    public CardData[][] getCards() {
    	return cards;
    }
    
    public NobleData[] getNobles() {
    	return noblesDisplayed;
    }
    
    public PlayerData[] getPlayers() {
    	return playersInGame;
    }
}
