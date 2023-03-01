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
   * Add one token of a given colour.
   *
   * @param token the colour of the token to add
   * @return whether the token was added successfully
   */
  public boolean addOne(String token) {
    Integer current = quantities.get(Token.valueOf(token));
    if (current == null) {
      return false;
    }
    quantities.put(Token.valueOf(token), current + 1);
    return true;
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
    Integer current = quantities.get(Token.valueOf(token));
    if (current == null || current == 0) {
      return false;
    }
    quantities.put(Token.valueOf(token), current - 1);
    return true;
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
      if (quantities.get(t) < cost.get(t)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Check whether the tokens of given colours are available in this bank.
   *
   * @param tokens the colours of the tokens to check for in the bank
   * @return whether the tokens are available
   */
  public boolean checkAvailable(String[] tokens) {
    for (Token t : Token.values()) {
      String tokenName = t.toString();
      int quantity = quantities.get(t);
      for (String s : tokens) {
        if (tokenName.equals(s)) {
          quantity--;
        }
      }
      if (quantity < 0) {
        return false;
      }
    }
    return true;
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
  public JSONObject toJson() {
    JSONObject json = new JSONObject();
    for (Token colour : quantities.keySet()) {
      json.put(colour.toString().toLowerCase(), quantities.get(colour));
    }
    return json;
  }
}