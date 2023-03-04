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

  public int getAmount() {
	if (type == Token.NONE) return 0;
    return amount;
  }
}
