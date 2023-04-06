package ca.mcgill.splendorserver.models.cards;

import ca.mcgill.splendorserver.models.Token;
import java.io.Serializable;
import java.util.HashMap;
import org.json.simple.JSONObject;

/**
 * Model class for a Splendor development card.
 */
public class Card implements Serializable {

  private static final long serialVersionUID = -4044194965826569425L;
  private int id; //uniquely identify a card
  private int pts;
  private CardBonus bonus = new CardBonus();
  private CardType type; //special abilities of the card
  private CardLevel level; //base game or extension and level
  private int satchelCount; //number of associations with a satchel card
  private HashMap<Token, Integer> cost = new HashMap<Token, Integer>();

  /**
   * Constructor from a JSONObject.
   *
   * @param obj   the JSONObject with the card data
   * @param level the level of the card
   */
  public Card(JSONObject obj, CardLevel level) {
    this.id = Integer.parseInt((String) obj.get("id"));
    this.pts = Integer.parseInt((String) obj.get("points"));
    this.bonus = new CardBonus(Token.valueOfIgnoreCase((String) obj.get("bonus")), 
        Integer.parseInt((String) obj.get("bonusAmount")));
    cost.put(Token.BLUE, Integer.parseInt((String) obj.get("blue")));
    cost.put(Token.GREEN, Integer.parseInt((String) obj.get("green")));
    cost.put(Token.RED, Integer.parseInt((String) obj.get("red")));
    cost.put(Token.WHITE, Integer.parseInt((String) obj.get("white")));
    cost.put(Token.BLACK, Integer.parseInt((String) obj.get("brown")));
    satchelCount = 0;

    type = CardType.valueOf((String) obj.get("action"));
    this.level = level;
  }

  /**
   * Constructor from an id (for tests).
   *
   * @param id of the card
   * @param level of the card
   */
  public Card(int id, CardLevel level) {
    this.id = id;
    this.level = level;
  }

  /**
   * Getter for the card's special abilities.
   *
   * @return refer to CardType enum
   */
  public CardType getType() {
    return type;
  }

  /**
   * Getter for the base game vs expansion and the level of the card.
   *
   * @return refer to CardLevel enum
   */
  public CardLevel getLevel() {
    return level;
  }

  public CardBonus getBonus() {
    return bonus;
  }

  public int getPoints() {
    return pts;
  }

  /**
   * Mark this card as associated to a satchel card.
   */
  public void addSatchel() {
    if (this.type == CardType.SATCHEL) {
      return;
    }
    satchelCount++;
  }

  /**
   * Getter for whether the card is associated with a satchel card.
   *
   * @return -1 if it is a satchel card, otherwise the number of associations with a satchel card
   */
  public int getSatchelCount() {
    if (this.type == CardType.SATCHEL) {
      return -1;
    }
    return satchelCount;
  }

  /**
   * Getter for the cost of the card.
   *
   * @return a hashmap linking each token colour to its cost
   */
  public HashMap<Token, Integer> getCost() {
    return cost;
  }

  /**
   * Compares two card.
   *
   * @param other the card to compare to this one
   * @return whether they have the same id given that ids uniquely identify a card
   */
  public boolean equals(Card other) {
    return id == other.id;
  }

  /**
   * Getter for the id of a card.
   *
   * @return the unique id of a card
   */
  public Integer getId() {
    return id;
  }
}
