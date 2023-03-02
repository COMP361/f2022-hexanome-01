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
   * Constructor to create a new player. It only takes a
   * username and initializes it with a new inventory.
   *
   * @param username username assign to the player
   */
  public Player(String username) {
    this.username = username;
    this.inventory = new Inventory();
  }

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

  /**
   * returns the username of the player.
   *
   * @return username
   */
  public String getUsername() {
    return username;
  }

  /**
   * sets the username of the player.
   *
   * @param username the username of the player
   */
  public void setUsername(String username) {
    this.username = username;
  }

}
