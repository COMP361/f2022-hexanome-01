package ca.mcgill.splendorserver.models.board;

import ca.mcgill.splendorserver.models.Noble;

/**
 * Model class holding all Splendor noble tiles on the board.
 */
public class NobleBank {

  private Noble[] nobles;
  private int size;

  /**
   * Constructor.
   *
   * @param size the number of nobles to set for the game
   */
  public NobleBank(int size) {
    this.nobles = new Noble[size];
    this.size = size;
  }

  /**
   * Add a noble to the bank of nobles.
   *
   * @param noble the noble to add
   * @return whether the noble was added successfully
   */
  public boolean add(Noble noble) {
    for (int i = 0; i < size; i++) {
      if (nobles[i] == null) {
        nobles[i] = noble;
        return true;
      }
    }
    return false;
  }

  /**
   * Remove a noble from the bank of nobles.
   *
   * @param noble the noble to remove
   * @return whether the noble was removed successfully
   */
  public boolean remove(Noble noble) {
    for (int i = 0; i < size; i++) {
      if (noble.getId() == nobles[i].getId()) {
        nobles[i] = null;
        return true;
      }
    }
    return false;
  }

  /**
   * Getter for all the nobles on the board.
   *
   * @return an array of all the nobles on the baord
   */
  public Noble[] getNobles() {
    return nobles;
  }
}
