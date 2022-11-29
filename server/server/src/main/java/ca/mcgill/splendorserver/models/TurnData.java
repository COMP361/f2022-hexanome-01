package ca.mcgill.splendorserver.models;

/**
 * Data passed for each turn.
 */
public class TurnData {
  private CardData cardTaken;
  private NobleData nobleTaken;
  
  /**
   * Constructs turn data.
   *
   * @param cardTaken card taken of null if none
   * @param nobleTaken noble taken or null if none
   */
  public TurnData(CardData cardTaken, NobleData nobleTaken) {
    this.cardTaken = cardTaken;
    this.nobleTaken = nobleTaken;
  }
  
  /**
   * Gets card taken.
   *
   * @return row index
   */
  public CardData getCardTaken() {
    return this.cardTaken;
  }
  
  /**
   * Gets noble taken.
   *
   * @return noble index
   */
  public NobleData getNobleTaken() {
    return this.nobleTaken;
  }
}
