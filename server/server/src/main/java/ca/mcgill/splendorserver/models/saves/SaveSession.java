package ca.mcgill.splendorserver.models.saves;

import java.util.Arrays;

import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.Player;

/**
 * Session based on a save of a game.
 */
public class SaveSession {

  private Game game;
  private String[] playersRequired;
  private String savegameid;

  /**
   * Parameterized constructor.
   *
   * @param game the game to save
   * @param savegameid the id of the save
   */
  public SaveSession(Game game, String savegameid) {
    this.game = game;
    playersRequired = game.getPlayerIds();
    this.savegameid = savegameid;
  }

  public boolean isValidLaunch(String variant, int numPlayers) {
    return variant.equals(game.getVariant()) && playersRequired.length == numPlayers;
  }
  
  public void reassignPlayers(String[] players) {
	if (Arrays.equals(playersRequired, players)) {
      return;
	}
	for (int i=0; i<playersRequired.length; i++) {
	  if (!playersRequired[i].equals(players[i])) {
	    int j = Arrays.asList(players).indexOf(playersRequired[i]);
	    if (j != -1) {
	      String tmp = players[i];
	      players[i] = players[j];
	      players[j] = tmp;
	    }
	  }
	}
	playersRequired = players.clone();
	int index = 0;
	for (Player player : game.getPlayers()) {
      player.setUsername(players[index++]);
	}
  }

  public Game getGame() {
    return game;
  }

  public String getSavegameid() {
    return savegameid;
  }

}
