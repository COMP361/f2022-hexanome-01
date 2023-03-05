package ca.mcgill.splendorserver.models.board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import org.json.simple.JSONArray;

import ca.mcgill.splendorserver.models.Inventory;
import ca.mcgill.splendorserver.models.Noble;
import ca.mcgill.splendorserver.models.registries.NobleRegistry;

/**
 * Model class holding all Splendor noble tiles on the board.
 */
public class NobleBank {

  private int[] nobles;
  private int size;

  /**
   * Constructor.
   *
   * @param size the number of nobles to set for the game
   */
  public NobleBank(int size) {
    this.size = size;
    nobles = new int[size];

    Stack<Integer> nobleIds = NobleRegistry.getIds();
    Collections.shuffle(nobleIds);
    for (int i = 0; i < size; i++) {
      nobles[i] = nobleIds.pop();
    }
  }

  /**
   * Add a noble to the bank of nobles.
   *
   * @param nobleId the id of the noble to add
   * @return whether the noble was added successfully
   */
  public boolean add(int nobleId) {
    for (int i = 0; i < size; i++) {
      if (nobles[i] == -1) {
        nobles[i] = nobleId;
        return true;
      }
    }
    return false;
  }

  /**
   * Remove a noble from the bank of nobles.
   *
   * @param index the index of the noble to remove
   * @return whether the noble was removed successfully
   */
  public boolean remove(int index) {
    if (index >= size || nobles[index] == -1) {
      return false;
    }
    nobles[index] = -1;
    return true;
  }

  /**
   * Getter for all the nobles on the board.
   *
   * @return an array of all the nobles on the baord
   */
  public int[] getNobles() {
    return nobles;
  }

  /**
   * Method that checks if a given noble is on the board.
   *
   * @return whether or not this noble is on the board
   */
  public boolean contains(int nobleId) {
    for (int i = 0; i < nobles.length; i++) {
      if (nobleId == nobles[i]) {
        return true;
      }
    }
    return false;
  }

  /**
   * Check whether any noble in the noble bank is impressed given a certain inventory.
   *
   * @param inventory the inventory that could impress one or many nobles
   * @return the nobles that were impressed
   */
  public ArrayList<Integer> attemptImpress(Inventory inventory) {
    ArrayList<Integer> impressed = new ArrayList<Integer>();
    for (int nobleId : getNobles()) {
      if (nobleId == -1) {
        continue;
      }
      Noble noble = NobleRegistry.of(nobleId);
      if (noble.impressed(inventory.getBonuses())) {
        impressed.add(noble.getId());
      }
    }
    return impressed;
  }

  /**
   * Getter for the nobles on the board as a JSONArray.
   *
   * @return the nobles on the board as a JSONArray
   */
  public JSONArray toJson() {
    JSONArray json = new JSONArray();
    for (int i = 0; i < size; i++) {
      json.add(nobles[i]);
    }
    return json;
  }
}
