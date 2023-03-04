package ca.mcgill.splendorserver.models.cards;

/**
 * Enumeration of Splendor development card levels for base and expansion packs.
 */
public enum CardLevel {
  LEVEL1("Level1"),
  LEVEL2("Level2"),
  LEVEL3("Level3"),
  ORIENTLEVEL1("OrientLevel1"),
  ORIENTLEVEL2("OrientLevel2"),
  ORIENTLEVEL3("OrientLevel3");

  public static CardLevel valueOfIgnoreCase(String name) {
    return valueOf(name.toUpperCase());
  }

  private final String displayName; 

  CardLevel(String displayName) {
    this.displayName = displayName;
  }

  @Override
  public String toString() {
    return this.displayName.toLowerCase(); 
  }
}
