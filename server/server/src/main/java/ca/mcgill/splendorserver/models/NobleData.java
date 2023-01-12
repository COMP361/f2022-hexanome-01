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
  
  /**
   * Sole constructor.  (For invocation by subclass constructors, typically
   * implicit.)
   */
  public NobleData() {
    
  }
  
  /**
   * Setter for id.
   *
   * @param id int of id
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Setter for points.
   *
   * @param points int of points
   */
  public void setPoints(int points) {
    this.points = points;
  }

  /**
   * Setter for red.
   *
   * @param red int of red
   */
  public void setRed(int red) {
    this.red = red;
  }

  /**
   * Setter for blue.
   *
   * @param blue int of blue
   */
  public void setBlue(int blue) {
    this.blue = blue;
  }

  /**
   * Setter for green.
   *
   * @param green in of green
   */
  public void setGreen(int green) {
    this.green = green;
  }

  /**
   * Setter for brown.
   *
   * @param brown int of brown
   */
  public void setBrown(int brown) {
    this.brown = brown;
  }

  /**
   * Setter for white.
   *
   * @param white int of white
   */
  public void setWhite(int white) {
    this.white = white;
  }

  /**
   * Setter for id.
   *
   * @return int of id
   */
  public int getId() {
    return id;
  }

  /**
   * Getter for points.
   *
   * @return int of points
   */
  public int getPoints() {
    return points;
  }

  /**
   * Getter for red.
   *
   * @return int of red
   */
  public int getRed() {
    return red;
  }

  /**
   * Getter for blue.
   *
   * @return int of blue
   */
  public int getBlue() {
    return blue;
  }

  /**
   * Getter for green.
   *
   * @return int of green
   */
  public int getGreen() {
    return green;
  }

  /**
   * Getter for brown.
   *
   * @return int of brown
   */
  public int getBrown() {
    return brown;
  }

  /**
   * Getter for white.
   *
   * @return int of white
   */
  public int getWhite() {
    return white;
  }

  /**
   * Compares two noble data.
   *
   * @param other to be compared to
   * @return equivalence
   */
  public boolean equals(NobleData other) {
    return this.id == other.id;
  }
}
