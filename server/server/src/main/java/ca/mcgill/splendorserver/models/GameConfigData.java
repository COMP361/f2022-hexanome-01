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

  /**
   * sets game name.
   *
   * @param gameName the gameName to set
   */
  public void setGameName(String gameName) {
    this.gameName = gameName;
  }

  /**
   * sets hostId.
   *
   * @param hostId the hostId to set
   * 
   */
  public void setHostId(String hostId) {
    this.hostId = hostId;
  }

  /**
   * sets playerIds.
   *
   * @param playerIds the playerIds to set
   * 
   */
  public void setPlayerIds(String[] playerIds) {
    this.playerIds = playerIds;
  }

  /**
   * sets deck1.
   *
   * @param deck1 the deck1 to set
   * 
   */
  public void setDeck1(CardData[] deck1) {
    this.deck1 = deck1;
  }

  /**
   * sets deck2.
   *
   * @param deck2 the deck2 to set
   * 
   */
  public void setDeck2(CardData[] deck2) {
    this.deck2 = deck2;
  }

  /**
   * sets deck3.
   *
   * @param deck3 the deck3 to set
   * 
   */
  public void setDeck3(CardData[] deck3) {
    this.deck3 = deck3;
  }

  /**
   * sets exdeck1.
   *
   * @param exDeck1 the exDeck1 to set
   * 
   */
  public void setExDeck1(CardData[] exDeck1) {
    this.exDeck1 = exDeck1;
  }

  /**
   * sets exdeck2.
   *
   * @param exDeck2 the exDeck2 to set
   * 
   */
  public void setExDeck2(CardData[] exDeck2) {
    this.exDeck2 = exDeck2;
  }

  /**
   * sets exdeck3.
   *
   * @param exDeck3 the exDeck3 to set
   * 
   */
  public void setExDeck3(CardData[] exDeck3) {
    this.exDeck3 = exDeck3;
  }

  /**
   * sets all nobles.
   *
   * @param allNobles the allNobles to set
   * 
   */
  public void setAllNobles(NobleData[] allNobles) {
    this.allNobles = allNobles;
  }
}
