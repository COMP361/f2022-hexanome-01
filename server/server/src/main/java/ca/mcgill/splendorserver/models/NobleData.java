package ca.mcgill.splendorserver.models;

/**
 * Stores and manages nobles data.
 */
public class NobleData {
  private int id;
  private int points;
  private int red;
  private int blue;
  private int green;
  private int brown;
  private int white;
  
  public void setId(int id) {
    this.id = id;
  }

  public void setPoints(int points) {
    this.points = points;
  }

  public void setRed(int red) {
    this.red = red;
  }

  public void setBlue(int blue) {
    this.blue = blue;
  }

  public void setGreen(int green) {
    this.green = green;
  }

  public void setBrown(int brown) {
    this.brown = brown;
  }

  public void setWhite(int white) {
    this.white = white;
  }

  public int getId() {
    return id;
  }

  public int getPoints() {
    return points;
  }

  public int getRed() {
    return red;
  }

  public int getBlue() {
    return blue;
  }

  public int getGreen() {
    return green;
  }

  public int getBrown() {
    return brown;
  }

  public int getWhite() {
    return white;
  }
}
