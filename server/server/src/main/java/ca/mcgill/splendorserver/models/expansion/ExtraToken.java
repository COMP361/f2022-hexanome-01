package ca.mcgill.splendorserver.models.expansion;

import ca.mcgill.splendorserver.models.Player;

/**
 * Trading post action that gets an extra token on taking
 * two of the same colour.
 */
public class ExtraToken implements Action {

  @Override
  public void activate(Player player) {
    player.getInventory().incrementPosts();
  }

  @Override
  public void deactivate(Player player) {
    player.getInventory().decrementPosts();
  }

  @Override
  public void initiate(Player player) {

  }

}
