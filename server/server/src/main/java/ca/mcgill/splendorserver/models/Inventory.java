package ca.mcgill.splendorserver.models;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import ca.mcgill.splendorserver.models.board.TokenBank;
import ca.mcgill.splendorserver.models.cards.Card;
import ca.mcgill.splendorserver.models.expansion.City;
import ca.mcgill.splendorserver.models.expansion.TradingPost;
import ca.mcgill.splendorserver.models.expansion.Unlockable;

/**
 * Model class for a Splendor player's inventory i.e. everything they've acquired.
 */
public class Inventory implements JsonStringafiable {

  private int points;
  private TokenBank tokens;
  private TokenBank bonuses;
  private ArrayList<Card> cards;
  private ArrayList<Noble> nobles;
  private ArrayList<Card> reservedCards;
  private ArrayList<Noble> reservedNobles;
  private City acquiredCity;
  private TradingPost[] tradingPosts;
  private ArrayList<Unlockable> unlockables; //please keep cities and trading posts separate
  private int activatedPosts;

  /**
   * Constructor.
   */
  public Inventory() {
    cards = new ArrayList<>();
    nobles = new ArrayList<>();
    reservedCards = new ArrayList<>();
    reservedNobles = new ArrayList<>();
    tokens = new TokenBank();
    bonuses = new TokenBank();
    tradingPosts = new TradingPost[5];
    unlockables = new ArrayList<>();
  }

  /**
   * Getter for points gained.
   *
   * @return points achieved.
   */
  public int getPoints() {
    return points;
  }

  /**
   * Getter for activated trading posts.
   *
   * @return number of activated trading posts.
   */
  public int getPosts() {
    return activatedPosts;
  }

  /**
   * incrementer for activated trading posts.
   */
  public void incrementPosts() {
    activatedPosts++;
  }

  /**
   * decrementer for activated trading posts.
   */
  public void decrementPosts() {
    activatedPosts--;
  }

  /**
   * Getter for list of cards.
   *
   * @return list of acquired cards.
   */
  public ArrayList<Card> getCards() {
    return cards;
  }

  /**
   * Getter for list of unlockables unlocked by this player.
   *
   * @return list of unlocked unlockables
   */
  public ArrayList<Unlockable> getUnlockables() {
    return unlockables;
  }

  /**
   * Getter for list of nobles.
   *
   * @return list of acquired nobles.
   */
  public ArrayList<Noble> getNobles() {
    return nobles;
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
   * Change points by indicated value.
   *
   * @param change to change points by.
   */
  public void changePoints(int change) {
    points += change;
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
    //TO DO: add the correct bonuses too?
    //TO DO: add points too
    //do not remove cost of card tho, it will mess up stuff
  }

  /**
   * Pay for a card using player's tokens/discounts.
   *
   * @param card the card to pay for.
   */
  public void payForCard(Card card) {
    for (Token token : Token.values()) {
      if (token.equals(Token.GOLD)) {
        continue;
      }
      tokens.removeRepeated(token.toString(), card.getCost().get(token));
    }
  }

  public boolean isCostAffordable(HashMap<Token, Integer> cost) {
    for (Token token : Token.values()) {
      if (token.equals(Token.GOLD)) {
        continue;
      }
      if (tokens.checkAmount(token) < cost.get(token)) {
        return false;
      }
    }
    return true;
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
    TokenBank bonuses = new TokenBank();
    for (Card card : cards) {
      bonuses.addRepeated(card.getBonus().getType().toString(), card.getBonus().getAmount());
    }
    return bonuses;
  }

  /**
   * Getter for the reserved cards of the player.
   *
   * @return reservedCards
   */
  public ArrayList<Card> getReservedCards() {
    return (ArrayList<Card>) reservedCards.clone();
  }

  @Override
  @SuppressWarnings("unchecked")
  public String toJsonString() {
    JSONObject data = new JSONObject();
    data.put("acquiredCards", JSONArray.toJSONString(cards));
    data.put("acquiredNobles", JSONArray.toJSONString(nobles));
    data.put("reservedCards", JSONArray.toJSONString(reservedCards));
    data.put("reservedNobles", JSONArray.toJSONString(reservedNobles));
    data.put("tokens", tokens.toJsonString());
    if (acquiredCity != null) {
      data.put("acquiredCity", acquiredCity.getId());
    }

    return data.toJSONString();
  }

  /**
   * Getter for the inventory as a JSONObject.
   *
   * @return the inventory as a JSONObject
   */
  @SuppressWarnings("unchecked")
  public JSONObject toJson() {
    JSONObject json = new JSONObject();
    //points
    json.put("points", points);
    //cards
    JSONArray cardsJson = new JSONArray();
    for (Card card : cards) {
      cardsJson.add(card.getId());
    }
    json.put("acquiredCards", cardsJson);
    //nobles
    JSONArray noblesJson = new JSONArray();
    for (Noble noble : nobles) {
      noblesJson.add(noble.getId());
    }
    json.put("acquiredNobles", noblesJson);
    //reserved cards
    JSONArray reservedCardsJson = new JSONArray();
    for (Card card : reservedCards) {
      cardsJson.add(card.getId());
    }
    json.put("reservedCards", reservedCardsJson);
    //reserved nobles
    JSONArray reservedNoblesJson = new JSONArray();
    for (Noble noble : reservedNobles) {
      noblesJson.add(noble.getId());
    }
    json.put("reservedNobles", reservedNoblesJson);
    //tokens
    json.put("tokens", tokens.toJson());
    //bonuses
    json.put("bonuses", bonuses.toJson());
    //trading posts
    JSONArray tradingPostsJson = new JSONArray();
    for (TradingPost tradingPost : tradingPosts) {
      if (tradingPost != null) {
        switch (tradingPost.getId()) {
          case 15:
            tradingPostsJson.add("A");
            break;
          case 16:
            tradingPostsJson.add("B");
            break;
          case 17:
            tradingPostsJson.add("C");
            break;
          case 18:
            tradingPostsJson.add("D");
            break;
          case 19:
            tradingPostsJson.add("E");
            break;
          default:
            break;
        }
      }
    }
    if (!tradingPostsJson.isEmpty()) {
      json.put("acquiredTradingPosts", tradingPostsJson);
    }

    return json;
  }
}
