package ca.mcgill.splendorserver.models;

/**
 * Holds the data for each card as stored on client as well.
 */
public class CardData {
  private int id;
  private int points;
  private char bonus;
  private int bonusAmount;

  private int red;
  private int blue;
  private int green;
  private int brown;
  private int white;

  /**
   * Sole constructor.  (For invocation by subclass constructors, typically
   * implicit.)
   */
  public CardData() {
    
  }
  
  /**
   * Gets id.
   *
   * @return id to get
   */
  public int getId() {
    return id;
  }

  /**
   * Sets id.
   *
   * @param id to set
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Gets points.
   *
   * @return points to get
   */
  public int getPoints() {
    return points;
  }

  /**
   * Sets points.
   *
   * @param points to set
   */
  public void setPoints(int points) {
    this.points = points;
  }

  /**
   * Gets bonus.
   *
   * @return bonus to get
   */
  public char getBonus() {
    return bonus;
  }

  /**
   * Sets bonus.
   *
   * @param bonus to set
   */
  public void setBonus(char bonus) {
    this.bonus = bonus;
  }

  /**
   * Gets bonusAmount.
   *
   * @return bonusAmount to get
   */
  public int getBonusAmount() {
    return bonusAmount;
  }

  /**
   * Sets bonusAmount.
   *
   * @param bonusAmount to be set
   */
  public void setBonusAmount(int bonusAmount) {
    this.bonusAmount = bonusAmount;
  }

  /**
   * Gets red.
   *
   * @return red tokens
   */
  public int getRed() {
    return red;
  }

  /**
   * Sets red.
   *
   * @param red tokens
   */
  public void setRed(int red) {
    this.red = red;
  }

  /**
   * Gets blue.
   *
   * @return blue tokens
   */
  public int getBlue() {
    return blue;
  }

  /**
   * Sets blue.
   *
   * @param blue tokens
   */
  public void setBlue(int blue) {
    this.blue = blue;
  }

  /**
   * Gets green.
   *
   * @return green tokens
   */
  public int getGreen() {
    return green;
  }

  /**
   * Sets green.
   *
   * @param green tokens
   */
  public void setGreen(int green) {
    this.green = green;
  }

  /**
   * Gets brown.
   *
   * @return brown tokens
   */
  public int getBrown() {
    return brown;
  }

  /**
   * Sets brown.
   *
   * @param brown tokens
   */
  public void setBrown(int brown) {
    this.brown = brown;
  }

  /**
   * Gets white.
   *
   * @return white tokens
   */
  public int getWhite() {
    return white;
  }

  /**
   * Sets white.
   *
   * @param white tokens
   */
  public void setWhite(int white) {
    this.white = white;
  }

  /**
   * Compares two card data.
   *
   * @param other to be compared to
   * @return equivalence
   */
  public boolean equals(CardData other) {
    return this.id == other.id;
  }
}
