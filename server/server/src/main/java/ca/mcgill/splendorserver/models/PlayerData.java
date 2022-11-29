package ca.mcgill.splendorserver.models;

/**
 * Stores and manages player-specific game state information.
 */
public class PlayerData {
  private String username;
  private CardData[] inventory;
  private NobleData[] nobles;
  private int[] discounts = new int[5]; //order: red, green, blue, brown, white

  /**
   * Getter for username.
   *
   * @return username string
   */
  public String getUsername() {
    return username;
  }

  /**
   * Setter for username.
   *
   * @param username username string
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Getter for inventory as card data.
   *
   * @return inventory as card data array
   */
  public CardData[] getInventory() {
    return inventory;
  }

  /**
   * Setter for inventory as card data.
   *
   * @param inventory card data array
   */
  public void setInventory(CardData[] inventory) {
    this.inventory = inventory;
  }

  /**
   * Getter for noble data.
   *
   * @return data for nobles
   */
  public NobleData[] getNobles() {
    return nobles;
  }
  
  /**
   * Sets nobles.
   *
   * @param nobles data for nobles
   */
  public void setNobles(NobleData[] nobles) {
    this.nobles = nobles;
  }
  
  /**
   * Gets discounts.
   *
   * @return discounts as array of int
   */
  public int[] getDiscounts() {
    return discounts;
  }
  
  /**
   * Sets discounts.
   *
   * @param discounts discounts as array of int
   */
  public void setDiscounts(int[] discounts) {
    this.discounts = discounts;
  }

}
