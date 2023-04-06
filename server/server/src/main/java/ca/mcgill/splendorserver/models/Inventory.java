package ca.mcgill.splendorserver.models;

import ca.mcgill.splendorserver.models.board.TokenBank;
import ca.mcgill.splendorserver.models.cards.Card;
import ca.mcgill.splendorserver.models.expansion.City;
import ca.mcgill.splendorserver.models.expansion.DoubleGold;
import ca.mcgill.splendorserver.models.expansion.TradingPost;
import ca.mcgill.splendorserver.models.expansion.Unlockable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Model class for a Splendor player's inventory i.e. everything they've acquired.
 */
public class Inventory implements Serializable {

  private static final long serialVersionUID = -8218270563314325614L;
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
   * Remove an array of tokens from the player's inventory.
   *
   * @param selectedTokens an array of String representing the colors of the tokens to remove
   * @return whether the tokens were successfully removed
   */
  public boolean removeTokens(Token[] selectedTokens) {
    return tokens.removeAll(selectedTokens);
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
    points += noble.getPts();
  }

  /**
   * Add a card to the player's acquired cards.
   *
   * @param card the card to add
   */
  public void addCard(Card card) {
    cards.add(card);
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
  public Token[] isCostAffordable(HashMap<Token, Integer> cost) {
    HashMap<Token, Integer> payment = new HashMap<Token, Integer>();
    payment.put(Token.GOLD, 0);
    payment.put(Token.RED, 0);
    payment.put(Token.BLUE, 0);
    payment.put(Token.BLACK, 0);
    payment.put(Token.GREEN, 0);
    payment.put(Token.WHITE, 0);
    
    int goldUsed = 0; //gold tokens being used
    int leftOver = 0; //rollover from gold orient cards
    int cardsUsed = 0; //gold orient cards being used
    for (Token token : Token.values()) { //for each token type
      if (token.equals(Token.GOLD)) {
        continue;
      }
      TokenBank bonuses = getBonuses();
      int tokenAmount = tokens.checkAmount(token);
      int tokenCost = Math.max(0, cost.get(token) - bonuses.checkAmount(token));
      payment.replace(token, tokenCost);

      if (tokenAmount < tokenCost) { //if insufficient funds
        payment.replace(token, tokenAmount);
        int goldAvailable = tokens.checkAmount(Token.GOLD) - goldUsed;
        boolean doubleGold = false;
        for (Unlockable u : unlockables) { //check for trading post
          if (u instanceof TradingPost
              && ((TradingPost) u).getAction() instanceof DoubleGold) {
            doubleGold = true;
            break;
          }
        }

        int goldNeeded = doubleGold ? (tokenCost - tokenAmount) / 2 : tokenCost - tokenAmount;
        if (goldNeeded > goldAvailable
            + getBonuses().checkAmount(Token.GOLD) + leftOver - cardsUsed * 2) {
          return null;
        } else if (goldNeeded > goldAvailable + leftOver) {
          int remaining = goldNeeded - goldAvailable - leftOver;
          cardsUsed += (int) ((((double) remaining) / 2) + 0.5);
          leftOver = remaining % 2;
          goldNeeded = goldNeeded - remaining; //adjust needed goldTokens by removing goldCards
        }
        goldUsed += goldNeeded;
        payment.put(Token.GOLD, payment.get(Token.GOLD) + goldNeeded);
      }
    }

    while (cardsUsed > 0) {
      for (int i = 0; i < cards.size(); i++) {
        if (cards.get(i).getBonus().getType() == Token.GOLD) {
          cards.remove(i); //if this is gold card, remove it decrement cards used
          cardsUsed--;
        }
      }
    }
    
    ArrayList<Token> tokensPaid = new ArrayList<Token>();
    for (Token token : Token.values()) {
      for (int i = 0; i < payment.get(token); i++) {
        tokensPaid.add(token);
      }
      tokens.removeRepeated(token, payment.get(token));
    }

    return tokensPaid.toArray(new Token[0]); //return gold tokens used
  }

  /**
   * Remove a card from the player's acquired cards.
   *
   * @param card the card to remove
   * @return whether the card was successfully removed
   */
  public boolean removeCard(Card card) {
    if (cards.contains(card)) {
      cards.remove(card);
      points -= card.getPoints();
      return true;
    }
    return false;
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
  @SuppressWarnings("unchecked")
  public ArrayList<Card> getReservedCards() {
    return reservedCards;
  }

  /**
   * Checker for the reserved cards of the player.
   *
   * @param card card to check
   * @return status
   */
  public boolean containsReservedCard(Card card) {
    return reservedCards.contains(card);
  }

  /**
   * Remove from reserved cards.
   *
   * @param card card to remove
   * @return status
   */
  public boolean removeFromReservedCards(Card card) {
    return reservedCards.remove(card);
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
    
    //satcheled cards. i = cardid, i+1 = satchel count
    JSONArray satchelCardsJson = new JSONArray();
    for (Card card : cards) {
      satchelCardsJson.add(card.getId());
      satchelCardsJson.add(card.getSatchelCount());
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
      reservedCardsJson.add(card.getId());
    }
    json.put("reservedCards", reservedCardsJson);
    //reserved nobles
    JSONArray reservedNoblesJson = new JSONArray();
    for (Noble noble : reservedNobles) {
      reservedNoblesJson.add(noble.getId());
    }
    json.put("reservedNobles", reservedNoblesJson);
    //tokens
    json.put("tokens", tokens.toJson());
    //bonuses
    json.put("bonuses", getBonuses().toJson());
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

  /**
   * Getter for reserved nobles.
   *
   * @return list of claimed nobles
   */
  public ArrayList<Noble> getReservedNobles() {
    return reservedNobles;
  }

  /**
   * Checker for city containment.
   *
   * @return whether this inventory contains a city
   */
  public boolean containsCity() {
    for (Unlockable u : unlockables) {
      if (u instanceof City) {
        return true;
      }
    }
    return false;
  }

  /**
   * getter for city.
   *
   * @return this inventory's city card
   */
  public City getCity() {
    for (Unlockable u : unlockables) {
      if (u instanceof City) {
        return (City) u;
      }
    }
    return null;
  }
}