package ca.mcgill.splendorserver.models;


/**
 * Game config data.
 * 
 */
public class GameConfigData {
  
  private String gameName;
  private String hostId;
  private String[] playerIds;
  
  private CardData[] deck1;
  private CardData[] deck2;
  private CardData[] deck3;
  private CardData[] exDeck1;
  private CardData[] exDeck2;
  private CardData[] exDeck3;
  
  private NobleData[] allNobles;
  
  /**
   * Gets game name.
   *
   * @return gameName
   */
  public String getGameName() {
    return gameName;
  }
  
  /**
   * Gets host ID.
   *
   * @return hostId
   */
  public String getHostId() {
    return hostId;
  }
  
  /**
   * Gets player IDs.
   *
   * @return hostId
   */
  public String[] getPlayerIds() {
    return playerIds;
  }
  
  /**
   * Gets level 1 deck.
   *
   * @return deck1
   */
  public CardData[] getDeck1() {
    return deck1;
  }
  
  /**
   * Gets level 2 deck.
   *
   * @return deck2
   */
  public CardData[] getDeck2() {
    return deck2;
  }
  
  /**
   * Gets level 3 deck.
   *
   * @return deck3
   */
  public CardData[] getDeck3() {
    return deck3;
  }
  
  /**
   * Gets level 1 expansion deck.
   *
   * @return expansion deck1
   */
  public CardData[] getExDeck1() {
    return exDeck1;
  }
  
  /**
   * Gets level 2 expansion deck.
   *
   * @return expansion deck2
   */
  public CardData[] getExDeck2() {
    return exDeck2;
  }
  
  /**
   * Gets level 3 expansion deck.
   *
   * @return expansion deck13
   */
  public CardData[] getExDeck3() {
    return exDeck3;
  }
  
  /**
   * Gets all nobles available.
   *
   * @return nobles
   */
  public NobleData[] getAllNobles() {
    return allNobles;
  }
}
