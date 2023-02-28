package ca.mcgill.splendorserver.models.expansion;

import java.util.ArrayList;

import org.json.simple.JSONObject;

import ca.mcgill.splendorserver.models.Player;

/**
 * Model class for trading post ability from Splendor trading post extension.
 */
public class TradingPost implements Unlockable {

	private int id;
	private Condition condition; //condition to unlock this post
	//action
	private ArrayList<Player> owners; //list of players who have unlocked this post in current game
	
  public TradingPost(JSONObject json) {
    // TODO Auto-generated constructor stub
	  owners = new ArrayList<Player>();
  }

  @Override
  public void observe(Player player) {
    // TODO Auto-generated method stub

  }

  @Override
  public void use() {
    // TODO Auto-generated method stub

  }

  @Override
  public int getId() {
    // TODO Auto-generated method stub
    return id;
  }

}
