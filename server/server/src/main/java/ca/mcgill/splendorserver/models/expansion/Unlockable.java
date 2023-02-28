package ca.mcgill.splendorserver.models.expansion;

import ca.mcgill.splendorserver.models.Player;

/**
 * Interface for abilities from TradingPosts and cities from Cities extensions.
 */
public interface Unlockable {

  public void observe(Player player);

  public void use();

  public int getId();
}
