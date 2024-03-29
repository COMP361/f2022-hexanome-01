package ca.mcgill.splendorserver.models.expansion;

import ca.mcgill.splendorserver.apis.JsonHandler;
import ca.mcgill.splendorserver.models.Player;
import java.io.Serializable;
import org.json.simple.JSONObject;

/**
 * Model class for trading post from Splendor trading post extension.
 */
public class TradingPost implements Unlockable, Serializable {

  private static final long serialVersionUID = -3196160135947464174L;
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
      case 15: action = new FreeToken(); 
               break;
      case 16: action = new ExtraToken(); 
               break;
      case 17: action = new DoubleGold(); 
               break;
      case 18: action =  new FlatPointAction(5);
               break;
      case 19: action = new DynamicPointAction(); 
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
            player.getInventory().changePoints(1);  
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
            player.getInventory().changePoints(-1);  
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
