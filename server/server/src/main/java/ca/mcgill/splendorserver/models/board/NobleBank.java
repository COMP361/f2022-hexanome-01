package ca.mcgill.splendorserver.models.board;

import ca.mcgill.splendorserver.models.JsonStringafiable;
import ca.mcgill.splendorserver.models.registries.NobleRegistry;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;
import org.json.simple.JSONArray;

/**
 * Model class holding all Splendor noble tiles on the board.
 */
public class NobleBank implements JsonStringafiable {

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

  @Override
  public String toJsonString() {
    return JSONArray.toJSONString(Arrays.asList(nobles));
  }
}
