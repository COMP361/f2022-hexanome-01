package ca.mcgill.splendorserver.models.saves;

import java.util.Set;

import ca.mcgill.splendorserver.models.Game;

public class SaveSession {

  private Game game;
  private Set<String> playersRequired;
	
  public SaveSession(Game game) {
    this.game = game;
	playersRequired = game.playerIdSet();
  }
	
  public boolean isValidLaunch(String variant, String[] players) {
	return variant.equals(game.getVariant()) && Set.of(players).equals(playersRequired);
  }
	
  public Game getGame() {
	return game;
  }
	
}
