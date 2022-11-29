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

  public void setId(int id) {
    this.id = id;
  }

  public int getPoints() {
    return points;
  }

  public void setPoints(int points) {
    this.points = points;
  }

  public char getBonus() {
    return bonus;
  }

  public void setBonus(char bonus) {
    this.bonus = bonus;
  }

  public int getBonusAmount() {
    return bonusAmount;
  }

  public void setBonusAmount(int bonusAmount) {
    this.bonusAmount = bonusAmount;
  }

  public int getRed() {
    return red;
  }

  public void setRed(int red) {
    this.red = red;
  }

  public int getBlue() {
    return blue;
  }

  public void setBlue(int blue) {
    this.blue = blue;
  }

  public int getGreen() {
    return green;
  }

  public void setGreen(int green) {
    this.green = green;
  }

  public int getBrown() {
    return brown;
  }

  public void setBrown(int brown) {
    this.brown = brown;
  }

  public int getWhite() {
    return white;
  }

  public void setWhite(int white) {
    this.white = white;
  }
}
