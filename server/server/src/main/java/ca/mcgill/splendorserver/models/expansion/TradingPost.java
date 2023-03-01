package ca.mcgill.splendorserver.models.expansion;

import ca.mcgill.splendorserver.apis.JsonHandler;
import ca.mcgill.splendorserver.models.Player;
import java.util.ArrayList;
import org.json.simple.JSONObject;

/**
 * Model class for trading post from Splendor trading post extension.
 */
public class TradingPost implements Unlockable {

  private int id;
  private Condition condition; //condition to unlock this post
  private Action action;
  //private ArrayList<Player> owners; //list of players who have unlocked this post in current game

  /**
   * Constructor.
   */
  public TradingPost(JSONObject json) {
    id = Integer.parseInt(json.get("id").toString());
    condition = 
        new Condition((JSONObject) JsonHandler.decodeJsonRequest(json.get("condition").toString()));
    switch (id) {
      case 0: break;
      case 1: break;
      case 2: break;
      case 3: break;
      case 4: break;
      default: break;
    }
    //owners = new ArrayList<Player>();
  }
  
  /**
   * Getter for trading posts' action.
   *
   * @return this trading posts' action.
   */
  public Action getAction() {
    return action;
  }

  @Override
  public void observe(Player player) {
    if (!player.getInventory().getUnlockables().contains(this)
          && condition.checkCondition(player.getInventory())) {
      //owners.add(player);
      player.getInventory().getUnlockables().add(this);
      action.activate(player);
      if (!(action instanceof DynamicPointAction)) {
        for (Unlockable u : player.getInventory().getUnlockables()) {
          if (u instanceof TradingPost 
                && ((TradingPost) u).getAction() instanceof DynamicPointAction) {
            player.getInventory().incrementPosts();  
            return;
          }
        }
      }
    } else if (player.getInventory().getUnlockables().contains(this)
          && !condition.checkCondition(player.getInventory())) {
      //owners.remove(player);
      action.deactivate(player);
      player.getInventory().getUnlockables().remove(this);
      if (!(action instanceof DynamicPointAction)) {
        for (Unlockable u : player.getInventory().getUnlockables()) {
          if (u instanceof TradingPost 
                  && ((TradingPost) u).getAction() instanceof DynamicPointAction) {
            player.getInventory().decrementPosts();  
            return;
          }
        }
      }
    }
  }

  @Override
  public void use(Player player) {
    action.initiate(player);
  }

  @Override
  public int getId() {
    return id;
  }

}
