package ca.mcgill.splendorserver.models.saves;

import ca.mcgill.splendorserver.models.Game;

public class LobbyServiceSaveData {

	private String gamename;
	private String[] players;
	private String savegameid;
	
	public LobbyServiceSaveData(String gamename, String[] players, String savegameid) {
		this.setGamename(gamename);
		this.setPlayers(players);
		this.setSavegameid(savegameid);
	}
	
	public LobbyServiceSaveData(Game game) {
		this.setGamename(game.getVariant());
		this.setPlayers((String[]) game.playerIdSet().toArray());
		this.setSavegameid(game.getId());
	}

	public String getGamename() {
		return gamename;
	}

	public void setGamename(String gamename) {
		this.gamename = gamename;
	}

	public String[] getPlayers() {
		return players;
	}

	public void setPlayers(String[] players) {
		this.players = players;
	}

	public String getSavegameid() {
		return savegameid;
	}

	public void setSavegameid(String savegameid) {
		this.savegameid = savegameid;
	}
	
}
