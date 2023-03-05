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

  /**
   * Constructor.
   *
   * @param json a JSONObject of the trading post
   */
  public TradingPost(JSONObject json) {
    id = Integer.parseInt(json.get("id").toString());
    condition = 
        new Condition((JSONObject) JsonHandler.decodeJsonRequest(json.get("condition").toString()));
    switch (id) {
      case 15: action = null; 
               break;
      case 16: action = null; 
               break;
      case 17: action = new DoubleGold(); 
               break;
      case 18: action = new DynamicPointAction(); 
               break;
      case 19: action = new FlatPointAction(5); 
               break;
      default: break;
    }
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
