package ca.mcgill.splendorserver.models.expansion;

import ca.mcgill.splendorserver.models.Player;

/**
 * Trading post action that adds a fixed amount of points to player's score.
 */
public class FlatPointAction implements Action {

  private int points;

  /**
   * Constructor.
   *
   * @param points the points being added by a trading post ability
   */
  public FlatPointAction(int points) {
    this.points = points;
  }

  @Override
  public void activate(Player player) {
    player.getInventory().changePoints(points);
    player.getInventory().incrementPosts();
  }

  @Override
  public void deactivate(Player player) {
    player.getInventory().changePoints(-points);
    player.getInventory().decrementPosts();
  }

  @Override
  public void initiate(Player player) {
    //not applicable
  }
}
