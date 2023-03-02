package ca.mcgill.splendorserver.controllers;

import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.Inventory;
import ca.mcgill.splendorserver.models.Noble;
import ca.mcgill.splendorserver.models.SessionData;
import ca.mcgill.splendorserver.models.Token;
import ca.mcgill.splendorserver.models.board.Board;
import ca.mcgill.splendorserver.models.board.CardBank;
import ca.mcgill.splendorserver.models.board.TokenBank;
import ca.mcgill.splendorserver.models.cards.Card;
import ca.mcgill.splendorserver.models.cards.CardType;
import ca.mcgill.splendorserver.models.expansion.TradingPost;
import ca.mcgill.splendorserver.models.registries.CardRegistry;
import ca.mcgill.splendorserver.models.registries.NobleRegistry;
import ca.mcgill.splendorserver.models.registries.UnlockableRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

/**
 * This is the controller for all game managing functionality.
 * This classed is used to separate the REST endpoints with the
 * different controller functionality.
 */
@Component
public class GameManager {

  private static HashMap<String, Game> gameRegistry = new HashMap<String, Game>();

  private static HashMap<String, Game> saves = new HashMap<>();

  private CardRegistry cardRegistry = new CardRegistry();
  private NobleRegistry nobleRegistry = new NobleRegistry();
  private UnlockableRegistry unlockRegistry = new UnlockableRegistry();

  /**
   * Provides functionality of launching a game.
   * This function is called by the launchGame endpoint(PUT /api/splendor/{gameId})
   *
   * @param gameId  the unique id of the game
   * @param session the data about the session
   */
  public static void launchGame(String gameId, SessionData session) {
    String saveId = session.getSavegame();

    if (!saveId.equals("")) {
      Game save = saves.get(saveId);
      gameRegistry.put(gameId, save);
      save.setLaunched();
    } else {
      //players
      String[] players = session.getPlayers();
      //variant
      String variant = session.getVariant();
      //creator
      String creator = session.getCreator();
      //create new game
      gameRegistry.put(gameId, new Game(gameId, creator, players, variant));
    }
  }

  /**
   * Returns the game saved in the active game list.
   *
   * @param gameId the id of the game we want to find.
   * @return the game object saved
   */
  public static Game getGame(String gameId) {
    if (gameRegistry.containsKey(gameId)) {
      return gameRegistry.get(gameId);
    } else {
      return null;
    }
  }
  
  /**
   * Adds a card to a players inventory. does pay for the card.
   *
   * @param game the game where the player and card reside.
   * @param playerId the player in question.
   * @param cardId the card the player wishes to acquire.
   * @return the JSONObject response containing the action being done, and choices for user.
   */
  @SuppressWarnings("unchecked")
public static JSONObject purchaseCard(Game game, String playerId, int cardId) {
    Board board = game.getBoard();
    Card card = CardRegistry.of(cardId);
    Inventory inventory = board.getInventory(playerId);

    if (!inventory.isCostAffordable(card.getCost()) || !acquireCard(card, board, inventory)) {
      return null;
    }
    inventory.payForCard(card);
    
    JSONObject purchaseResults = determineBody(card, board, inventory);

    return purchaseResults;
  }
  
  /**
   * Creates the body for http responses.
   *
   * @param card that was purchased/acquired.
   * @param board of the game in question.
   * @param inventory of the player acquiring the card.
   * @return the JSONObject response containing the action being done, and choices for user.
   */
  @SuppressWarnings("unchecked")
public static JSONObject determineBody(Card card, Board board, Inventory inventory) {
    JSONObject response = new JSONObject();
    response.put("action", "none");
    response.put("choices", JSONArray.toJSONString(new ArrayList<Noble>()));
    if (card.getType() != CardType.NONE) {
      JSONObject result = OrientManager.handleCard(card, board, inventory);
      String furtherAction = (String) result.get("type");
      String actionOptions = (String) result.get("choices");
      response.replace("action", furtherAction);
      response.replace("choices", actionOptions);
      response.put("noblesVisiting", JSONArray.toJSONString(new ArrayList<Noble>()));
    } else {
      ArrayList<Noble> noblesVisiting = board.getNobles().attemptImpress(inventory);
      response.put("noblesVisiting", JSONArray.toJSONString(noblesVisiting));
    }

    return response;
  }
  
  /**
   * Adds a card to a players inventory. does not pay for the card.
   *
   * @param card the card the player wishes to acquire.
   * @param board the board on which the card resides.
   * @param inventory the inventory we wish to add the card to.
   * @return whether the acquisition was successful.
   */
  public static boolean acquireCard(Card card, Board board, Inventory inventory) {
    if (card == null) {
      return false;
    }
    CardBank cards = board.getCards();
    int pickedUp = cards.draw(card.getId());
    if (pickedUp != card.getId())  {
      return false;
    }
    inventory.addCard(card);
    return true;
  }

  /**
   * Action of taking tokens.
   *
   * @param game the game in which the action takes place
   * @param playerId the player taking the tokens
   * @param tokens the tokens to take
   * @return a JSONObject of the player's token overflow following taking tokens (max 10)
   */
  public static JSONObject takeTokens(Game game, String playerId, String[] tokens) {
    Board board = game.getBoard();
    Inventory inventory = board.getInventory(playerId);
    JSONObject takeTokensResult = new JSONObject();

    //check validity of the token taking
    if (!checkValidityTokens(game, playerId, tokens)) {
      throw new RuntimeException("The requested take tokens action was not valid.");
    }

    //try adding tokens
    if (inventory.addTokens(tokens)) {
      //return overflow
      takeTokensResult.put("tokenOverflow", inventory.getTokens().checkOverflow());
      return takeTokensResult;
    } else { //if taking the tokens didn't go through
      throw new RuntimeException("The requested take tokens action could not be completed.");
    }
  }

  private static boolean checkValidityTokens(Game game, String playerId, String[] tokenStrings) {
    //check that all given strings are valid tokens
    ArrayList<Token> tokens = new ArrayList<>();
    for (String tokenString : tokenStrings) {
      try {
        Token token = Token.valueOf(tokenString.toUpperCase());
        tokens.add(token);
      } catch (Exception e) {
        return false;
      }
    }

    //check if the tokens given correspond to a valid action pattern
    Board board = game.getBoard();
    TokenBank tokenBank = board.getTokens();
    //trading post variant adds possibilities for taking tokens
    boolean tradingPostA = false;
    boolean tradingPostB = false;
    if (game.getVariant().equals("tradingposts")) {
      TradingPost[] tradingPosts = board.getInventory(playerId).getTradingPosts();
      for (TradingPost tradingPost : tradingPosts) {
        if (tradingPost.getId() == 15) { //trading post id 15 is trading post A
          tradingPostA = true;
        } else if (tradingPost.getId() == 16) { //trading post id 16 is trading post B
          tradingPostB = true;
        }
      }
    }

    if (tradingPostA && tokens.size() == 1) { //A -> purchasedCard? -> 1 token -> at least 1 left
      //there is only one possible valid action for taking exactly one token
      //therefore, if that token isn't available, we don't need to check any other options
      //before returning false
      return tokenBank.checkQuantity(tokens.get(0)) > 0;
    } else if (tokens.size() == 2) { //2 of same token -> at least 4 left
      //check same token
      if (tokens.get(0) == tokens.get(1)) {
        //check at least 4 left
        //this is the only action possible involving two tokens,
        //so if it isn't valid we can safely return false
        return tokenBank.checkQuantity(tokens.get(0)) > 3;
      }
    } else if (tokens.size() == 3) { //3 tokens, either 3 different or 2 same + 1 different
      if (tokens.get(0) != tokens.get(1)
          && tokens.get(1) != tokens.get(2)
          && tokens.get(0) != tokens.get(2)) { //3 of different tokens -> at least 1 left each
        //check at least 1 left of each
        //there can be another valid action with three tokens,
        //but it would have two of the same tokens and one different token,
        //so it would not pass the condition above,
        //and we can safely return false if the condition below is false
        return tokenBank.checkQuantity(tokens.get(0)) > 0
            && tokenBank.checkQuantity(tokens.get(1)) > 0
            && tokenBank.checkQuantity(tokens.get(2)) > 0;
      } else if (tradingPostB && tokens.get(0) == tokens.get(1) && tokens.get(0) != tokens.get(2)) {
        //B -> first 2 same + last 1 different -> 4 left of same + 1 left different
        return tokenBank.checkQuantity(tokens.get(0)) > 4
            && tokenBank.checkQuantity(tokens.get(2)) > 0;
      } else if (tradingPostB && tokens.get(1) == tokens.get(2) && tokens.get(0) != tokens.get(1)) {
        //B -> last 2 same + first 1 different -> 4 left of same + 1 left different
        return tokenBank.checkQuantity(tokens.get(1)) > 4
            && tokenBank.checkQuantity(tokens.get(0)) > 0;
      } else if (tradingPostB && tokens.get(0) == tokens.get(2) && tokens.get(0) != tokens.get(1)) {
        //B -> 2 (first 1 and last 1) same + middle 1 different -> 4 left of same + 1 left different
        return tokenBank.checkQuantity(tokens.get(0)) > 4
            && tokenBank.checkQuantity(tokens.get(1)) > 0;
      }
    }

    //all actions would have already returned true if they were valid
    //therefore, any action that reaches this point must be invalid
    return false;
  }

  /**
   * Gets the game board of the game with the provided ID.
   *
   * @param gameId id of game
   * @return Optional is used in this case as we might not have found the game
   */
  public static Optional<Board> getGameBoard(String gameId) {
    if (gameRegistry.containsKey(gameId)) {
      return Optional.of(gameRegistry.get(gameId).getBoard());
    } else {
      return Optional.empty();
    }
  }

  /**
   * Deletes the given game from the registry.
   *
   * @param gameId the id of the games
   */
  public static void deleteGame(String gameId) {
    gameRegistry.remove(gameId);
  }

  /**
   * Returns the game registry.
   *
   * @return gameRegistry
   */
  public static HashMap<String, Game> getGameRegistry() {
    return gameRegistry;
  }
}
