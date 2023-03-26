package ca.mcgill.splendorserver.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.Inventory;
import ca.mcgill.splendorserver.models.Noble;
import ca.mcgill.splendorserver.models.Player;
import ca.mcgill.splendorserver.models.Token;
import ca.mcgill.splendorserver.models.board.Board;
import ca.mcgill.splendorserver.models.board.CardBank;
import ca.mcgill.splendorserver.models.board.CitiesBoard;
import ca.mcgill.splendorserver.models.board.NobleBank;
import ca.mcgill.splendorserver.models.board.TokenBank;
import ca.mcgill.splendorserver.models.cards.Card;
import ca.mcgill.splendorserver.models.cards.CardLevel;
import ca.mcgill.splendorserver.models.cards.CardType;
import ca.mcgill.splendorserver.models.communicationbeans.SessionData;
import ca.mcgill.splendorserver.models.expansion.ExtraToken;
import ca.mcgill.splendorserver.models.expansion.FreeToken;
import ca.mcgill.splendorserver.models.expansion.TradingPost;
import ca.mcgill.splendorserver.models.expansion.Unlockable;
import ca.mcgill.splendorserver.models.registries.CardRegistry;
import ca.mcgill.splendorserver.models.registries.UnlockableRegistry;
import ca.mcgill.splendorserver.models.saves.SaveSession;

/**
 * This is the controller for all game managing functionality.
 * This classed is used to separate the REST endpoints with the
 * different controller functionality.
 */
@Component
public class GameManager {

  private static HashMap<String, Game> gameRegistry = new HashMap<String, Game>();

  private static HashMap<String, Game> saves = new HashMap<>();

  /**
   * Provides functionality of launching a game.
   * This function is called by the launchGame endpoint(PUT /api/splendor/{gameId})
   *
   * @param gameId  the unique id of the game
   * @param session the data about the session
   */
  public static boolean launchGame(String gameId, SessionData session) {
    String saveId = session.getSavegame();

    if (!saveId.equals("")) {
      SaveSession save = SaveManager.loadGame(gameId, session.getCreator());
      if (save != null && save.isValidLaunch(session.getVariant(), session.getPlayers())) {
        gameRegistry.put(gameId, save.getGame());
        return true;
      }
      return false;
    } else {
      //players
      String[] playersUsernames = session.getPlayers();
      int numberOfPlayers = playersUsernames.length;
      Player[] players = new Player[numberOfPlayers];
      for (int i = 0; i < numberOfPlayers; i++) {
        players[i] = new Player(playersUsernames[i]);
      }
      //variant
      String variant = session.getVariant();
      //creator
      String creator = session.getCreator();
      //create new game
      gameRegistry.put(gameId, new Game(gameId, creator, players, variant));
      return true;
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
   * @param game     the game where the player and card reside.
   * @param playerId the player in question.
   * @param cardId   the card the player wishes to acquire.
   * @return the JSONObject response containing the action being done, and choices for user.
   */
  public static JSONObject purchaseCard(Game game, String playerId, int cardId) {
    Board board = game.getBoard();
    Card card = CardRegistry.of(cardId);
    Inventory inventory = board.getInventory(playerId);
    
    if (card.getType() == CardType.SACRIFICE) {
      return determineBody(card, board, inventory);
    }
    
    int goldUsed = inventory.isCostAffordable(card.getCost());

    if (goldUsed == -1 || !acquireCard(card, board, inventory)) {
      return null;
    }
    Token[] toAddToBank = inventory.payForCard(card, goldUsed);
    board.getTokens().addAll(toAddToBank);
    inventory.acquireCard(card);

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
    response.put("options", new JSONArray());
    
    if (card.getType() != CardType.NONE) {
      if (card.getType() == CardType.SATCHEL || card.getType() == CardType.DOMINO1) {
        boolean valid = false;
        for (int i = 0; i < inventory.getCards().size(); i++) {
          if (inventory.getCards().get(i).getBonus().getType() != null
              && inventory.getCards().get(i).getBonus().getType() != Token.GOLD) {
            valid = true;
            break;
          }
        }
        if (!valid) {
          return null;
        }
      }
      JSONObject result = OrientManager.handleCard(card, board, inventory);
      String furtherAction = (String) result.get("type");
      String actionOptions = (String) result.get("options");
      response.replace("action", furtherAction);
      response.replace("options", actionOptions);
      response.put("noblesVisiting", new JSONArray());
    } else {
      JSONArray noblesVisiting = new JSONArray();
      for (int nobleId : board.getNobles().attemptImpress(inventory)) {
        noblesVisiting.add(nobleId);
      }
      for (Noble noble : inventory.getReservedNobles()) {
        if (noble.impressed(inventory.getBonuses())) {
          noblesVisiting.add(noble.getId());
        }
      }
      response.put("noblesVisiting", noblesVisiting);
      ArrayList<Unlockable> unlockables = inventory.getUnlockables();
      for (Unlockable u : unlockables) {
        if (u instanceof TradingPost && ((TradingPost) u).getAction() instanceof FreeToken) {
          response.replace("action", "token");
          break;
        }
      }
    }

    return response;
  }

  /**
   * Adds a card to a players inventory. does not pay for the card.
   *
   * @param card      the card the player wishes to acquire.
   * @param board     the board on which the card resides.
   * @param inventory the inventory we wish to add the card to.
   * @return whether the acquisition was successful.
   */
  public static boolean acquireCard(Card card, Board board, Inventory inventory) {
    if (card == null) {
      return false;
    }
    CardBank cards = board.getCards();
    int pickedUp = cards.draw(card);
    if (pickedUp != card.getId() && !inventory.getReservedCards().contains(card)) {
      return false;
    } 
    
    if (inventory.getReservedCards().contains(card)) {
      inventory.getReservedCards().remove(card);
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
  @SuppressWarnings("unchecked")
public static JSONObject takeTokens(Game game, String playerId, Token[] tokens) {
    Board board = game.getBoard();
    Inventory inventory = board.getInventory(playerId);
    JSONObject takeTokensResult = new JSONObject();

    //check validity of the token taking
    if (!checkValidityTokens(game, playerId, tokens)) {
      return null;
    }

    //try adding tokens
    if (inventory.addTokens(tokens)) {
      //return overflow
      board.getTokens().removeAll(tokens);
      
      int overflow = inventory.getTokens().checkOverflow();
      takeTokensResult.put("tokenOverflow", overflow <= 40 ? overflow : 0);
      return takeTokensResult;
    } else { //if taking the tokens didn't go through
      return null;
    }
  }

  private static boolean checkValidityTokens(Game game, String playerId, Token[] tokensArray) {
    //check that all given strings are valid tokens
    ArrayList<Token> tokens = new ArrayList<Token>();
    Collections.addAll(tokens, tokensArray);

    //check if the tokens given correspond to a valid action pattern
    Board board = game.getBoard();
    TokenBank tokenBank = board.getTokens();
    //trading post variant adds possibilities for taking tokens
    boolean tradingPostA = false;
    boolean tradingPostB = false;
    if (game.getVariant().equals("tradingposts")) {
      ArrayList<Unlockable> unlockables = board.getInventory(playerId).getUnlockables();
      for (Unlockable u : unlockables) {
        if (u instanceof TradingPost && ((TradingPost) u).getAction() instanceof FreeToken) { 
          tradingPostA = true;
        } else if (u instanceof TradingPost
                && ((TradingPost) u).getAction() instanceof ExtraToken) { 
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
   * Functionality for a player to reserve a card.
   * It makes sure that the player is part of the game
   * they requested from.
   *
   * @param game the game that its being played
   * @param playerId id of the player taking the action
   * @param source the data receive from the request
   * @param cardId the card being reserved
   * @param deckId id of deck the card came from (if any)
   * @return true or false depending if the player can or cannot reserve card
   */
  public static boolean reserveCard(Game game, String playerId, 
      String source, int cardId, String deckId) {

    Board board = game.getBoard();
    Inventory inventory = board.getInventory(playerId);
    CardBank cards = board.getCards();

    if (source.equals("board")) {
      Card card = CardRegistry.of(cardId);

      int pickedUp = cards.draw(card);
      if (pickedUp != card.getId()) {
        return false;
      }
      if (inventory.reserve(card)) {
        return addGoldWithReserve(game, playerId);
      }
    } else if (source.equals("deck")) {
      CardLevel level = CardLevel.valueOfIgnoreCase(deckId);

      int pickedUp = cards.drawCardFromDeck(level);
      if (pickedUp == -1) {
        return false;
      }
      if (inventory.reserve(CardRegistry.of(pickedUp))) {
        return addGoldWithReserve(game, playerId);
      }
    }

    return false;
  }

  /**
   * Adds a gold token to a player's inventory when reserving a card.
   *
   * @param game the game where this is occurring
   * @param playerId the player reserving a card
   * @return whether it was successful
   */
  private static boolean addGoldWithReserve(Game game, String playerId) {
    Board board = game.getBoard();
    Inventory inventory = board.getInventory(playerId);
    TokenBank tokens = board.getTokens();

    if (tokens.removeOne(Token.GOLD)) {
      return inventory.addTokens(new Token[] {Token.GOLD});
    } else if (tokens.checkQuantity(Token.GOLD) == 0) {
      return true; //you can still reserve a card if the bank has no gold tokens
    }

    return false;
  }

  /**
   * Returns the game registry.
   *
   * @return gameRegistry
   */
  public HashMap<String, Game> getGameRegistry() {
    return gameRegistry;
  }

  /**
   * Adds a card to a players inventory. does not pay for the card.
   *
   * @param noble      the noble the player wishes to acquire.
   * @param board     the board on which the card resides.
   * @param inventory the inventory we wish to add the card to.
   * @return whether the acquisition was successful.
   */
  public static boolean acquireNoble(Noble noble, Board board, Inventory inventory) {
    if (noble == null) {
      return false;
    }
    NobleBank nobles = board.getNobles();
    if (!nobles.contains(noble.getId()) && !inventory.getReservedNobles().contains(noble)) {
      return false;
    }
    if (inventory.getReservedNobles().contains(noble))  {
      inventory.getNobles().add(noble);
      inventory.getReservedNobles().remove(noble);
      return true;
    }

    if (nobles.removeId(noble.getId())) {
      inventory.addNobleToInventory(noble);
      return true;
    }
    return false;
  }

  /**
   * Ends a game turn by updating the current player of a game.
   *
   * @param game the game where the turn is ending
   */
  public static void endTurn(Game game) {    
    Player currentPlayer = game.getCurrentPlayer();
    
    //if trading posts expansion enabled...
    if (game.getVariant().equals("tradingposts")) {
      for (Unlockable u : UnlockableRegistry.getTradingPosts()) {
        u.observe(currentPlayer);
      }
    }

    //doesnt check yet if multiple get unlocked,
    //should we have it unlock the highest point-values one?
    //or give the choice to the player?
    if (game.getBoard() instanceof CitiesBoard) {
      for (int c : ((CitiesBoard) game.getBoard()).getCities()) {
        UnlockableRegistry.of(c).observe(currentPlayer);
      }
    }
    
    //for citites, if it becomes host's turn and someone has a city, end game
    //player with city wins
    //if multiple city owners, one with highest point worth city wins
    //if still tie (both vanilla and with expansions), player with fewest cards wins
    
    game.nextPlayer(); //changes the current player to the next player
  }
}
