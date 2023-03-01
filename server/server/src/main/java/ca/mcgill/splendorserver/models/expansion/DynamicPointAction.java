package ca.mcgill.splendorserver.models.expansion;

import ca.mcgill.splendorserver.models.Player;

/**
 * Trading post action that adds a dynamic amount of points to player's score.
 * i.e. adds points according to number of posts activated.
 */
public class DynamicPointAction implements Action {

  @Override
  public void activate(Player player) {
    player.getInventory().incrementPosts();
    player.getInventory().changePoints(player.getInventory().getPosts());
  }

  @Override
  public void deactivate(Player player) {
    player.getInventory().changePoints(-player.getInventory().getPosts());
    player.getInventory().decrementPosts();
  }

  @Override
  public void initiate(Player player) {
    //not applicable
  }
}
