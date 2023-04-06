package ca.mcgill.splendorserver.models.expansion;

import ca.mcgill.splendorserver.models.Player;

/**
 * Interface for TradingPosts and cities from Cities extensions.
 */
public interface Unlockable {

  void observe(Player player);

  void use(Player player);

  int getId();
}
