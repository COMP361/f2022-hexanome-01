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
  }

  @Override
  public void deactivate(Player player) {
    player.setDoubleGold(false);
  }

  @Override
  public void initiate(Player player) {

  }
}
