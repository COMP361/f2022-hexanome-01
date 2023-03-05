package ca.mcgill.splendorserver.models;

/**
 * Enumeration of the possible token colours.
 */
public enum Token {
  GOLD("Gold"), BLUE("Blue"), GREEN("Green"), RED("Red"), WHITE("White"), BLACK("Black");
  
  /**
  * converter of string to token enum.

  * @param name given token name
  */
  public static Token valueOfIgnoreCase(String name) {
    switch (name) {
      case "null": return null;
      case "X": return null;
      case "G": return GREEN;
      case "R": return RED;
      case "J": return GOLD;
      case "B": return BLUE;
      case "K": return BLACK;
      case "W": return WHITE;
      default: return valueOf(name.toUpperCase());
    }
  }

  private final String displayName; 

  Token(String displayName) {
    this.displayName = displayName;
  }

  @Override
  public String toString() {
    return this.displayName.toLowerCase(); 
  }
}
