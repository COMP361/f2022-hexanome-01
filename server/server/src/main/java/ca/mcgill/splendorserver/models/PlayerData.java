package ca.mcgill.splendorserver.models;

/**
 * Stores and manages player-specific game state information.
 */
public class PlayerData {
  private String username;
  private String accessToken;
  private String refreshToken;
  private String expiresIn; //should password be added?
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
   * Getter for access token.
   *
   * @return access token string
   */
  public String getAccessToken() {
    return accessToken;
  }

  /**
   * Setter for access token.
   *
   * @param accessToken access token string
   */
  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  /**
   * Getter for refresh token.
   *
   * @return refresh token string
   */
  public String getRefreshToken() {
    return refreshToken;
  }

  /**
   * Setter for refresh token.
   *
   * @param refreshToken refresh token string
   */
  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  /**
   * Getter for expiry time.
   *
   * @return expiry time of access token
   */
  public String getExpiresIn() {
    return expiresIn;
  }

  /**
   * Setter for expiry time.
   *
   * @param expiresIn expiry time of access token
   */
  public void setExpiresIn(String expiresIn) {
    this.expiresIn = expiresIn;
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
