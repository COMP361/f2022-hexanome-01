package ca.mcgill.splendorserver.models.cards;

import ca.mcgill.splendorserver.models.Token;
import java.util.HashMap;
import org.json.simple.JSONObject;

/**
 * Model class for a Splendor development card.
 */
public class Card {

  private int id; //uniquely identify a card
  private int pts;
  private CardBonus bonus = new CardBonus();
  private CardType type; //special abilities of the card
  private CardLevel level; //base game or extension and level
  private int satchelCount; //number of associations with a satchel card
  private HashMap<Token, Integer> cost = new HashMap<Token, Integer>();

  /**
   * Constructor.
   *
   * @param id          card's identification number as assigned in GameController's CardRegistry
   * @param pts         the points awarded upon card acquisition
   * @param bonusType   colour of bonus awarded upon card acquisition
   * @param bonusAmount the amount of bonuses awarded upon card acquisition
   * @param blue        blue tokens cost
   * @param green       green tokens cost
   * @param red         red tokens cost
   * @param white       white tokens cost
   * @param black       black tokens cost
   * @param type        special abilities of card
   * @param level       base game or expansion plus the level of the card
   *                    (relevant to the card's deck)
   */
  public Card(int id, int pts, String bonusType, int bonusAmount, int blue, int green, int red,
              int white, int black,
              String type, String level) {
    this.id = id;
    this.pts = pts;
    bonus = new CardBonus(Token.valueOf(bonusType), bonusAmount);
    cost.put(Token.BLUE, blue);
    cost.put(Token.GREEN, green);
    cost.put(Token.RED, red);
    cost.put(Token.WHITE, white);
    cost.put(Token.BLACK, black);
    this.type = CardType.valueOf(type);
    this.level = CardLevel.valueOf(level);
  }

  /**
   * Constructor from a JSONObject.
   *
   * @param obj   the JSONObject with the card data
   * @param level the level of the card
   */
  public Card(JSONObject obj, CardLevel level) {
    id = Integer.parseInt((String) obj.get("id"));
    pts = Integer.parseInt((String) obj.get("points"));
    cost.put(Token.BLUE, Integer.parseInt((String) obj.get("blue")));
    cost.put(Token.GREEN, Integer.parseInt((String) obj.get("green")));
    cost.put(Token.RED, Integer.parseInt((String) obj.get("red")));
    cost.put(Token.WHITE, Integer.parseInt((String) obj.get("white")));
    cost.put(Token.BLACK, Integer.parseInt((String) obj.get("brown")));
    satchelCount = 0;
    bonus.amount = Integer.parseInt((String) obj.get("bonusAmount"));
    String typeChar = (String) obj.get("bonus");

    //bonus type
    switch (typeChar) {
      case "J":
        bonus.type = Token.GOLD;
        break;
      case "R":
        bonus.type = Token.RED;
        break;
      case "W":
        bonus.type = Token.WHITE;
        break;
      case "G":
        bonus.type = Token.GREEN;
        break;
      case "B":
        bonus.type = Token.BLUE;
        break;
      case "K":
        bonus.type = Token.BLACK;
        break;
      default:
        bonus.type = null;
        break;
    }

    type = CardType.valueOf((String) obj.get("action"));
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
