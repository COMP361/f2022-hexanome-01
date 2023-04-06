package ca.mcgill.splendorserver.models;

import ca.mcgill.splendorserver.models.cards.Card;
import java.io.Serializable;

/**
 * Model class for a Splendor player.
 */
public class Player implements Serializable {

  private static final long serialVersionUID = 5868176809531230694L;
  private String username;
  private Inventory inventory;

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
   * Remove a card from the player's inventory.
   *
   * @param card the card to remove
   * @return whether the card was removed successfully
   */
  public boolean removeCard(Card card) {
    return inventory.removeCard(card);
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
