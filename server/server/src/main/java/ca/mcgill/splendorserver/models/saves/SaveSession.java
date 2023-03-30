package ca.mcgill.splendorserver.models.saves;

import ca.mcgill.splendorserver.models.Game;
import java.util.Arrays;
import java.util.Set;

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

  public boolean isValidLaunch(String variant, String[] players) {
    return variant.equals(game.getVariant()) && Arrays.equals(players, playersRequired);
  }

  public Game getGame() {
    return game;
  }

  public String getSavegameid() {
    return savegameid;
  }

}
