package ca.mcgill.splendorserver.models;

/**
 * Enumeration of the possible token colours.
 */
public enum Token {
  GOLD("Gold"), BLUE("Blue"), GREEN("Green"), RED("Red"), WHITE("White"), BLACK("Black"), NONE("None");
  
  public static Token valueOfIgnoreCase(String name) {
	if (name.equals("null")) return NONE;
    return valueOf(name.toUpperCase());
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
