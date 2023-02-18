package ca.mcgill.splendorserver.models;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import ca.mcgill.splendorserver.models.board.TokenBank;
import ca.mcgill.splendorserver.models.cards.Card;

/**
 * Model class for a Splendor player's inventory i.e. everything they've acquired.
 */
public class Inventory implements JSONStringafiable {

  private TokenBank tokens;
  private TokenBank bonus;
  private ArrayList<Card> cards;
  private ArrayList<Noble> nobles;
  private ArrayList<Card> reservedCards;
  private ArrayList<Noble> reservedNobles;

  /**
   * Constructor.
   */
  public Inventory() {
    tokens = new TokenBank();
    bonus = new TokenBank();
  }

  /**
   * Add an array of tokens to the player's inventory.
   *
   * @param acquiredTokens an array of Strings representing the colors of the tokens to add
   * @return whether the tokens were successfully added
   */
  public boolean addTokens(String[] acquiredTokens) {
    return tokens.addAll(acquiredTokens);
  }

  /**
   * Add a card to the player's reserved cards,
   * assuming they haven't reached the limit of 3 reserved cards yet.
   *
   * @param card the card to reserve if possible
   * @return whether the card was successfully reserved
   */
  public boolean reserve(Card card) {
    if (reservedCards.size() == 3) {
      return false;
    }
    reservedCards.add(card);
    return true;
  }

  /**
   * Add a noble to the player's reserved nobles.
   *
   * @param noble the noble to reserve
   */
  public void reserveNoble(Noble noble) {
    reservedNobles.add(noble);
  }

  /**
   * Add a noble to the player's acquired nobles.
   *
   * @param noble the noble to add
   */
  public void addNobleToInventory(Noble noble) {
    nobles.add(noble);
  }

  /**
   * Add a card to the player's acquired cards.
   *
   * @param card the card to add
   */
  public void addCard(Card card) {
    cards.add(card);
    //need to add the correct bonuses too?
  }

  /**
   * Remove a card from the player's acquired cards.
   *
   * @param card the card to remove
   * @return whether the card was successfully removed
   */
  public boolean removeCard(Card card) {
    return cards.remove(card);
  }

  /**
   * Getter for the player's current tokens.
   *
   * @return TokenBank containing the player's current token counts
   */
  public TokenBank getTokens() {
    return tokens;
  }

  /**
   * Getter for the bonuses acquired by the player.
   *
   * @return TokenBank containing the player's current discount or bonus count for each token.
   */
  public TokenBank getBonuses() {
    return bonus;
  }

  @Override
  @SuppressWarnings("unchecked")
  public String toJSONString() {
	  JSONObject data = new JSONObject();
	  data.put("acquiredCards", JSONArray.toJSONString(cards));
	  data.put("acquiredNobles", JSONArray.toJSONString(nobles));
	  data.put("reservedCards", JSONArray.toJSONString(reservedCards));
	  data.put("reservedNobles", JSONArray.toJSONString(reservedNobles));
	  data.put("tokens", tokens.toJSONString());
	  
	  return data.toJSONString();
  }
}
