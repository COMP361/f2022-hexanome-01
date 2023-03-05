package ca.mcgill.splendorserver.models.board;

import ca.mcgill.splendorserver.models.JsonStringafiable;
import ca.mcgill.splendorserver.models.Token;
import ca.mcgill.splendorserver.models.cards.Card;
import java.util.HashMap;
import org.json.simple.JSONObject;

/**
 * Model class for a bank of Splendor tokens.
 */
public class TokenBank implements JsonStringafiable {

  private HashMap<Token, Integer> quantities;

  /**
   * Constructor.
   */
  public TokenBank() {
    quantities = new HashMap<Token, Integer>();
    for (Token t : Token.values()) {
      quantities.put(t, 0);
    }
  }

  /**
   * Parametric constructor.
   *
   * @param number the number of tokens with which to initialize the bank
   */
  public TokenBank(int number) {
    quantities = new HashMap<Token, Integer>();
    for (Token t : Token.values()) {
      quantities.put(t, number);
    }
  }

  /**
   * Check remaining quantity of a specific token in the bank.
   *
   * @param token the colour of the token to check.
   * @return the quantity in storage.
   */
  public int checkAmount(Token token) {
    return quantities.get(token);
  }

  /**
   * Add one token of the given colours to the bank.
   *
   * @param tokens the colours of the tokens to add
   * @return whether the addition wqs successful
   */
  public boolean addAll(String[] tokens) {
    for (String s : tokens) {
      if (!addOne(s)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Adds a given amount of a given token to the token bank.
   *
   * @param token the token that is being added
   * @param amount the number of tokens that should be added
   * @return whether adding the tokens was successful
   */
  public boolean addRepeated(String token, int amount) {
    try {
      Integer current = quantities.get(Token.valueOf(token));
      quantities.put(Token.valueOf(token), current + amount);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Add one token of a given colour.
   *
   * @param token the colour of the token to add
   * @return whether the token was added successfully
   */
  public boolean addOne(String token) {
    return addRepeated(token, 1);
  }

  /**
   * Remove one token of the given colours from the bank.
   *
   * @param tokens the colours of the tokens to remove
   * @return whether the removal was successful
   */
  public boolean removeAll(String[] tokens) {
    for (String s : tokens) {
      if (!removeOne(s)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Remove one token of a given colour.
   *
   * @param token the colour of the token to remove
   * @return whether the removal was successful
   */
  public boolean removeOne(String token) {
    return removeRepeated(token, 1);
  }

  /**
   * Removes a given amount of a given token from the token bank.
   *
   * @param token the token to remove
   * @param amount the number of tokens to remove
   * @return whether the removal was successful
   */
  public boolean removeRepeated(String token, int amount) {
    try {
      Integer current = quantities.get(Token.valueOf(token));
      if (current == 0) {
        return false;
      }
      quantities.put(Token.valueOf(token), current - amount);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Check if the tokens required to purchase are card are available in this bank.
   *
   * @param card the card whose cost is to be compared to the bank
   * @return whether the card can be purchased with this bank
   */
  public boolean canPurchase(Card card) {
    HashMap<Token, Integer> cost = card.getCost();
    for (Token t : Token.values()) {
      if (t.equals(Token.GOLD)) {
        continue;
      }
      if (quantities.get(t) < cost.get(t)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks whether there are more than 10 tokens in the token bank.
   *
   * @return the number of tokens over the 10 token limit in the token bank
   */
  public int checkOverflow() {
    int total = 0;
    for (int quantity : quantities.values()) {
      total += quantity;
    }
    //overflow is more than 10 tokens
    if (total > 10) {
      return total - 10;
    } else {
      return 0;
    }
  }

  /**
   * Checks the token bank's quantity of a given token,
   * for checking the validity of a take tokens action.
   *
   * @param token the token whose quantity is being checked
   * @return the quantity of the given token
   */
  public int checkQuantity(Token token) {
    return quantities.get(token);
  }

  @Override
  public String toJsonString() {
    return JSONObject.toJSONString(quantities);
  }

  /**
   * Getter for the tokens as a JSONObject.
   *
   * @return the tokens as a JSONObject (tokens identified by lowercase colour)
   */
  @SuppressWarnings("unchecked")
public JSONObject toJson() {
    JSONObject json = new JSONObject();
    for (Token colour : quantities.keySet()) {
      json.put(colour.toString().toLowerCase(), quantities.get(colour));
    }
    return json;
  }
}
