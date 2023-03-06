package ca.mcgill.splendorserver.models;

import ca.mcgill.splendorserver.models.board.TokenBank;
import ca.mcgill.splendorserver.models.cards.Card;
import ca.mcgill.splendorserver.models.expansion.City;
import ca.mcgill.splendorserver.models.expansion.TradingPost;
import ca.mcgill.splendorserver.models.expansion.Unlockable;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Model class for a Splendor player's inventory i.e. everything they've acquired.
 */
public class Inventory {

  private int points;
  private TokenBank tokens;
  private TokenBank bonuses;
  private ArrayList<Card> cards;
  private ArrayList<Noble> nobles;
  private ArrayList<Card> reservedCards;
  private ArrayList<Noble> reservedNobles;
  private ArrayList<Unlockable> unlockables;
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
  public boolean addTokens(Token[] acquiredTokens) {
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
   * @param goldUsed the gold the player wishes to use
   */
  public Token[] payForCard(Card card, int goldUsed) {
    ArrayList<Token> tokensPaid = new ArrayList<>();

    tokens.removeRepeated(Token.GOLD, goldUsed);
    for (int i = 0; i < goldUsed; i++) {
      tokensPaid.add(Token.GOLD);
    }

    TokenBank bonuses = getBonuses();
    for (Token token : Token.values()) {
      if (token.equals(Token.GOLD)) {
        continue;
      }
      int toRemove = Math.max(0, card.getCost().get(token) - bonuses.checkAmount(token));
      tokens.removeRepeated(token, toRemove);
      for (int i = 0; i < toRemove; i++) {
        tokensPaid.add(token);
      }
    }

    return tokensPaid.toArray(new Token[0]);
  }

  public void acquireCard(Card card) {
    points += card.getPoints();
    bonuses.addRepeated(card.getBonus().getType(), card.getBonus().getAmount());
  }

  /**
   * Checks whether the player can afford the cost.
   *
   * @param cost the amount to check for in the player's token bank
   * @return whether the player can afford the cost
   */
  public int isCostAffordable(HashMap<Token, Integer> cost) {
    int goldUsed = 0;
    for (Token token : Token.values()) {
      if (token.equals(Token.GOLD)) {
        continue;
      }
      TokenBank bonuses = getBonuses();
      int tokenAmount = tokens.checkAmount(token);
      int tokenCost = Math.max(0, cost.get(token) - bonuses.checkAmount(token));
      if (tokenAmount < tokenCost) {
        int goldAvailable = tokens.checkAmount(Token.GOLD) - goldUsed;
        if (tokenCost - tokenAmount > goldAvailable) {
          return -1;
        }
        goldUsed += tokenCost - tokenAmount;
      }
    }
    return goldUsed;
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
   * Getter for the bonuses acquired by the player (includes satchels).
   *
   * @return TokenBank containing the player's current discount or bonus count for each token.
   */
  public TokenBank getBonuses() {
    TokenBank bonuses = new TokenBank();
    for (Card card : cards) {
      if (card.getBonus().getType() != null) {
        bonuses.addRepeated(card.getBonus().getType(), 
            card.getBonus().getAmount() + card.getSatchelCount());
      }
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

  /**
   * Getter for the trading posts of the player.
   *
   * @return trading post array
   */
  public TradingPost[] getTradingPosts() {
    ArrayList<TradingPost> tradingPostsList = new ArrayList<>();
    for (Unlockable unlockable : unlockables) {
      if (unlockable != null & unlockable.getClass() == TradingPost.class) {
        tradingPostsList.add((TradingPost) unlockable);
      }
    }
    return tradingPostsList.toArray(new TradingPost[tradingPostsList.size()]);
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
    for (Unlockable unlockable : unlockables) {
      if (unlockable != null && unlockable.getClass() == TradingPost.class) {
        switch (unlockable.getId()) {
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
      } else if (unlockable != null && unlockable.getClass() == City.class) {
        json.put("acquiredCity", unlockable.getId());
      }
    }
    if (!tradingPostsJson.isEmpty()) {
      json.put("acquiredTradingPosts", tradingPostsJson);
    }

    return json;
  }

  public ArrayList<Noble> getReservedNobles() {
    return (ArrayList<Noble>) reservedNobles.clone();
  }
}