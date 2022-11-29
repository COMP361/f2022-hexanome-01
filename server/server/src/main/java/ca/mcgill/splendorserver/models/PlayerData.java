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
  private CardData[] reserved;
  private NobleData[] nobles;
  private int[] discounts = new int[5]; //order: red, green, blue, brown, white
  
  public String getUsername() {
    return username;
  }
  
  public void setUsername(String username) {
    this.username = username;
  }
  
  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public String getExpiresIn() {
    return expiresIn;
  }

  public void setExpiresIn(String expiresIn) {
    this.expiresIn = expiresIn;
  }

  public CardData[] getInventory() {
    return inventory;
  }
  
  public void setInventory(CardData[] inventory) {
    this.inventory = inventory;
  }
  
  public CardData[] getReserved() {
    return reserved;
  }
  
  public void setReserved(CardData[] reserved) {
    this.reserved = reserved;
  }
  
  public NobleData[] getNobles() {
    return nobles;
  }
  
  public void setNobles(NobleData[] nobles) {
    this.nobles = nobles;
  }
  
  public int[] getDiscounts() {
    return discounts;
  }
  
  public void setDiscounts(int[] discounts) {
    this.discounts = discounts;
  }

}
