package ca.mcgill.splendorserver.models.expansion;

import ca.mcgill.splendorserver.models.Player;

/**
 * Trading post action that doubles the worth of gold tokens.
 * i.e. adds points according to number of posts activated.
 */
public class DoubleGold implements Action {

  @Override
  public void activate(Player player) {
    player.setDoubleGold(true);
    player.getInventory().incrementPosts();
  }

  @Override
  public void deactivate(Player player) {
    player.setDoubleGold(false);
    player.getInventory().decrementPosts();
  }

  @Override
  public void initiate(Player player) {

  }
}
