package ca.mcgill.splendorserver.models.cards;

import ca.mcgill.splendorserver.models.Token;

/**
 * Model class for bonuses from Splendor development cards.
 */
public class CardBonus {
  Token type;
  int amount;

  public CardBonus(Token type, int amount) {
    this.type = type;
    this.amount = amount;
  }

  public CardBonus() {
    type = null;
    amount = 0;
  }

  public Token getType() {
    return type;
  }

  /**
   * Getter for quantity of bonus.
   */
  public int getAmount() {
    if (type == null) {
      return 0;
    }
    return amount;
  }
}
