package ca.mcgill.splendorserver.models.cards;

/**
 * Enumeration of Splendor development card abilities.
 */
public enum CardType {
  SACRIFICE("Sacrifice"),
  SATCHEL("Satchel"),
  RESERVE("Reserve"),
  DOMINO1("Domino1"),
  DOMINO2("Domino2"),
  NONE("None");

  public static CardType valueOfIgnoreCase(String name) {
    return valueOf(name.toUpperCase());
  }

  private final String displayName; 

  CardType(String displayName) {
    this.displayName = displayName;
  }

  @Override
  public String toString() {
    return this.displayName.toLowerCase(); 
  }
}
