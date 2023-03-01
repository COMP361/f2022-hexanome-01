package ca.mcgill.splendorserver.models.expansion;

import ca.mcgill.splendorserver.models.Player;

/**
 * Interface for TradingPost actions.
 */
public interface Action {

  public void activate(Player player); //called when trading post is activated (i.e. when condition is met)
  
  //called when trading post is deactivated (i.e. when condition is no longer met) 
  public void deactivate(Player player); 
  
  public void initiate(Player player); //called when wish to perform this action

}
