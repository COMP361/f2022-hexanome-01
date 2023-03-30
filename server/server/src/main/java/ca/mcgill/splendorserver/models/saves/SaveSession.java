package ca.mcgill.splendorserver.models.saves;

import ca.mcgill.splendorserver.models.Game;
import java.util.Set;

/**
 * Session based on a save of a game.
 */
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
