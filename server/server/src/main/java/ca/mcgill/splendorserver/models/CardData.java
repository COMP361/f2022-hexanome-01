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

  public int getId() {
    return id;
  }

  /**
   * Sets id.
   * 
   * @param id
   */
  public void setId(int id) {
    this.id = id;
  }

  public int getPoints() {
    return points;
  }

  /**
   * Sets points.
   * 
   * @param points
   */
  public void setPoints(int points) {
    this.points = points;
  }

  public char getBonus() {
    return bonus;
  }

  /**
   * Sets bonus.
   * 
   * @param bonus
   */
  public void setBonus(char bonus) {
    this.bonus = bonus;
  }

  public int getBonusAmount() {
    return bonusAmount;
  }

  /**
   * Sets bonusAmount.
   * 
   * @param bonusAmounts
   */
  public void setBonusAmount(int bonusAmount) {
    this.bonusAmount = bonusAmount;
  }

  /**
   * Gets red.
   * 
   * @return red
   */
  public int getRed() {
    return red;
  }

  /**
   * Sets red.
   * 
   * @param red
   */
  public void setRed(int red) {
    this.red = red;
  }

  /**
   * Gets blue.
   * 
   * @return blue
   */
  public int getBlue() {
    return blue;
  }

  /**
   * Sets blue.
   * 
   * @param blue
   */
  public void setBlue(int blue) {
    this.blue = blue;
  }

  /**
   * Gets green.
   * 
   * @return green
   */
  public int getGreen() {
    return green;
  }

  /**
   * Sets green.
   * 
   * @param green
   */
  public void setGreen(int green) {
    this.green = green;
  }

  /**
   * Gets brown.
   * 
   * @return brown
   */
  public int getBrown() {
    return brown;
  }

  /**
   * Sets brown.
   * 
   * @param brown
   */
  public void setBrown(int brown) {
    this.brown = brown;
  }

  /**
   * Gets white.
   * 
   * @return white
   */
  public int getWhite() {
    return white;
  }

  /**
   * Sets white.
   * 
   * @param white
   */
  public void setWhite(int white) {
    this.white = white;
  }
}
