package ca.mcgill.splendorserver.models;

/**
 * Data passed for each turn.
 */
public class TurnData {
  private int rowCardTaken;
  private int colCardTaken;
  private int nobleTaken;
  
  /**
   * Constructs turn data.
   *
   * @param rowCardTaken index of row of card taken of -1 if none
   * @param colCardTaken index of col of card taken of -1 if none
   * @param nobleTaken index of noble taken or -1 if none
   */
  public TurnData(int rowCardTaken, int colCardTaken, int nobleTaken) {
    this.rowCardTaken = rowCardTaken;
    this.colCardTaken = colCardTaken;
    this.nobleTaken = nobleTaken;
  }
  
  /**
   * Gets index of row of card taken of -1 if none.
   *
   * @return row index
   */
  public int getRowCardTaken() {
    return this.rowCardTaken;
  }
  
  /**
   * Gets index of row of card taken of -1 if none.
   *
   * @return row index
   */
  public int getColCardTaken() {
    return this.colCardTaken;
  }
  
  /**
   * Gets index of noble taken or -1 if none.
   *
   * @return noble index
   */
  public int getNobleTaken() {
    return this.nobleTaken;
  }
}
