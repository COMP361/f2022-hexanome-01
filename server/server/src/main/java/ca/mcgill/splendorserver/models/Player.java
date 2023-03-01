package ca.mcgill.splendorserver.models;

import ca.mcgill.splendorserver.models.cards.Card;
import ca.mcgill.splendorserver.models.expansion.Unlockable;
import java.util.ArrayList;

/**
 * Model class for a Splendor player.
 */
public class Player {

  private String username;
  private Inventory inventory;
  private Card lastAcquired;

  /**
   * Getter for inventory field.
   *
   * @return inventory of this player.
   */
  public Inventory getInventory() {
    return inventory;
  }  
  
  /**
   * Add the card to the player's inventory.
   *
   * @param card the card to add
   */
  public void acquireCard(Card card) {
    inventory.addCard(card);
  }

  /**
   * Remove a card from the player's inventory.
   *
   * @param card the card to remove
   * @return whether the card was removed successfully
   */
  public boolean removeCard(Card card) {
    return inventory.removeCard(card);
  }

  /**
   * Getter for the card last acquired by the player.
   *
   * @return the card last acquired by the player.
   */
  public Card getLastAcquired() {
    return lastAcquired;
  }
}
