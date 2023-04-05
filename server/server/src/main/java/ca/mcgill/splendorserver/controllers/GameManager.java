package ca.mcgill.splendorserver.controllers;

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
import ca.mcgill.splendorserver.models.expansion.City;
import ca.mcgill.splendorserver.models.expansion.ExtraToken;
import ca.mcgill.splendorserver.models.expansion.FreeToken;
import ca.mcgill.splendorserver.models.expansion.TradingPost;
import ca.mcgill.splendorserver.models.expansion.Unlockable;
import ca.mcgill.splendorserver.models.registries.CardRegistry;
import ca.mcgill.splendorserver.models.registries.UnlockableRegistry;
import ca.mcgill.splendorserver.models.saves.SaveSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  private SaveManager saveManager;

  /**
   * Provides functionality of launching a game.
   * This function is called by the launchGame endpoint(PUT /api/splendor/{gameId})
   *
   * @param gameId  the unique id of the game
   * @param session the data about the session
   * @return whether the game was successfully launched
   */
  public boolean launchGame(String gameId, SessionData session) {
    String saveId = session.getSavegame();

    if (!saveId.equals("")) {
      SaveSession save = saveManager.loadGame(gameId, session.getCreator());
      if (save != null && save.isValidLaunch(session.getVariant(), session.getPlayers().length)) {
        save.reassignPlayers(session.getPlayers());
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
  public Game getGame(String gameId) {
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
  public JSONObject purchaseCard(Game game, String playerId, int cardId) {
    Board board = game.getBoard();
    Card card = CardRegistry.of(cardId);
    Inventory inventory = board.getInventory(playerId);

    if (card.getType() == CardType.SACRIFICE) {
      return purchaseCardBody(card, board, inventory);
    }
    
    if (card.getType() == CardType.SATCHEL || card.getType() == CardType.DOMINO1) {
      if (!checkValidPairing(inventory)) {
        return null;
      }
    }

    Token[] toAddToBank = inventory.isCostAffordable(card.getCost());

    if (toAddToBank == null || !acquireCard(card, board, inventory)) {
      return null;
    }
    board.getTokens().addAll(toAddToBank);
    inventory.acquireCard(card);

    JSONObject purchaseResults = purchaseCardBody(card, board, inventory);

    return purchaseResults;
  }

  /**
   * Creates the body for http responses.
   *
   * @param card      that was purchased/acquired.
   * @param board     of the game in question.
   * @param inventory of the player acquiring the card.
   * @return the JSONObject response containing the action being done, and choices for user.
   */
  @SuppressWarnings("unchecked")
  public JSONObject purchaseCardBody(Card card, Board board, Inventory inventory) {
    JSONObject response = new JSONObject();
    response.put("action", "none");
    response.put("options", new JSONArray());

    if (card.getType() != CardType.NONE) {
      if (card.getType() == CardType.SATCHEL || card.getType() == CardType.DOMINO1) {
        if (!checkValidPairing(inventory)) {
          return null;
        }
      }
      JSONObject result = OrientManager.handleCard(card, board, inventory);
      if (result == null) {
        return null;
      }
      String furtherAction = (String) result.get("type");
      String actionOptions = (String) result.get("options");
      response.replace("action", furtherAction);
      response.replace("options", actionOptions);
      response.put("noblesVisiting", new JSONArray());
    } else {

      JSONArray noblesVisiting = checkImpressedNobles(inventory, board);

      response.put("noblesVisiting", noblesVisiting);
      if (checkFreeToken(inventory)) {
        response.replace("action", "token");
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
  public boolean acquireCard(Card card, Board board, Inventory inventory) {
    if (card == null) {
      return false;
    }
    CardBank cards = board.getCards();
    int pickedUp = cards.draw(card);
    
    if (pickedUp == card.getId()) {
      inventory.addCard(card);
      return true;
    }

    if (inventory.containsReservedCard(card)) {
      inventory.removeFromReservedCards(card);    
      inventory.addCard(card);
      return true;
    }
    
    return false;
  }

  /**
   * Action of taking tokens.
   *
   * @param game     the game in which the action takes place
   * @param playerId the player taking the tokens
   * @param tokens   the tokens to take
   * @return a JSONObject of the player's token overflow following taking tokens (max 10)
   */
  @SuppressWarnings("unchecked")
  public JSONObject takeTokens(Game game, String playerId, Token[] tokens) {
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

  /**
   * Action of return tokens.
   *
   * @param game     the game in which the action takes place
   * @param playerId the player returning the tokens
   * @param tokens   the tokens to return
   * @return whether the tokens are returned successfully
   */
  public boolean returnTokens(Game game, String playerId, Token[] tokens) {
    Board board = game.getBoard();
    Inventory inventory = board.getInventory(playerId);

    //try removing tokens from inventory
    if (inventory.removeTokens(tokens)) {
      //return overflow
      board.getTokens().addAll(tokens);
      return true;
    } else { //if returning the tokens didn't go through
      return false;
    }
  }

  private boolean checkValidityTokens(Game game, String playerId, Token[] tokensArray) {
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
  public Optional<Board> getGameBoard(String gameId) {
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
  public void deleteGame(String gameId) {
    gameRegistry.remove(gameId);
  }

  /**
   * Functionality for a player to reserve a card.
   * It makes sure that the player is part of the game
   * they requested from.
   *
   * @param game     the game that its being played
   * @param playerId id of the player taking the action
   * @param source   the data receive from the request
   * @param cardId   the card being reserved
   * @param deckId   id of deck the card came from (if any)
   * @return true or false depending if the player can or cannot reserve card
   */
  @SuppressWarnings("unchecked")
  public JSONObject reserveCard(Game game, String playerId,
                                    String source, int cardId, String deckId) {

    Board board = game.getBoard();
    Inventory inventory = board.getInventory(playerId);
    
    if (inventory.getReservedCards().size() >= 3) {
      return null;
    }
    
    CardBank cards = board.getCards();
    
    int pickedUp = -1;
    JSONObject reserveCardResult = new JSONObject();

    if (source.equals("board")) {
      Card card = CardRegistry.of(cardId);

      pickedUp = cards.draw(card);
      if (pickedUp != card.getId()) {
        return null;
      }
    } else if (source.equals("deck")) {
      CardLevel level = CardLevel.valueOfIgnoreCase(deckId);

      pickedUp = cards.drawCardFromDeck(level);
      if (pickedUp == -1) {
        return null;
      }
    }
    
    if (inventory.reserve(CardRegistry.of(pickedUp))) {
      addGoldWithReserve(game, playerId);
      int overflow = inventory.getTokens().checkOverflow();
      reserveCardResult.put("tokenOverflow", overflow <= 40 ? overflow : 0);
      //boolean gold = addGoldWithReserve(game, playerId);
      //reserveCardResult.put("tokenOverflow", 0);
      //if (gold) {
      //  int overflow = inventory.getTokens().checkOverflow();
      //  reserveCardResult.put("tokenOverflow", overflow <= 40 ? overflow : 0);
      //}
    }

    return reserveCardResult;
  }

  /**
   * Adds a gold token to a player's inventory when reserving a card.
   *
   * @param game     the game where this is occurring
   * @param playerId the player reserving a card
   * @return whether gold was added
   */
  private boolean addGoldWithReserve(Game game, String playerId) {
    Board board = game.getBoard();
    Inventory inventory = board.getInventory(playerId);
    TokenBank tokens = board.getTokens();

    if (tokens.checkQuantity(Token.GOLD) > 0) {
      tokens.removeOne(Token.GOLD);
      inventory.addTokens(new Token[] {Token.GOLD});
      return true;
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
   * @param noble     the noble the player wishes to acquire.
   * @param board     the board on which the card resides.
   * @param inventory the inventory we wish to add the card to.
   * @return whether the acquisition was successful.
   */
  public boolean acquireNoble(Noble noble, Board board, Inventory inventory) {
    if (noble == null) {
      return false;
    }
    NobleBank nobles = board.getNobles();
    if (!nobles.contains(noble.getId()) && !inventory.getReservedNobles().contains(noble)) {
      return false;
    }
    if (inventory.getReservedNobles().contains(noble)) {
      inventory.getNobles().add(noble);
      inventory.getReservedNobles().remove(noble);
      return true;
    }

    nobles.removeId(noble.getId());
    inventory.addNobleToInventory(noble);
    return true;
  }

  /**
   * Ends a game turn by updating the current player of a game.
   * Checks for City and TradingPost unlocks.
   *
   * @param game the game where the turn is ending
   * @return JSONObject of relevant info
   */
  public JSONObject endTurn(Game game) {
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
      
      //check if multiple city unlocks
      JSONArray acquiredCities = new JSONArray();
      for (Unlockable u : currentPlayer.getInventory().getUnlockables()) {
        if (u instanceof City) {
          acquiredCities.add(u.getId());
        }
      }
      
      //if multiple cities unlocked, give player choice
      if (acquiredCities.size() > 1) {
        //remove all cities from player inventory
        ArrayList<Unlockable> unlockables = currentPlayer.getInventory().getUnlockables();
        for (int i = 0; i < unlockables.size(); i++) {
          if (unlockables.get(i) instanceof City) {
            unlockables.remove(i);
            i--;
          }
        }
            
        JSONObject response = new JSONObject();
        response.put("action", "city");
        response.put("options", acquiredCities.toJSONString());
        return response;
      }
    }

    //for citites, if it becomes host's turn and someone has a city, end game
    //player with city wins
    //if multiple city owners, one with highest point worth city wins
    //if still tie (both vanilla and with expansions), player with fewest cards wins

    //go to next players turn, check winner
    changeTurn(game);
    
    return new JSONObject();
  }
  
  /**
   * Updates current player, checks for win.
   *
   * @param game the game where the turn is ending
   */
  public void changeTurn(Game game) {
    game.nextPlayer(); //changes the current player to the next player

    //game.getBoard().setWinner("winner test");
    //at the endturn, check if an entire round is finished(goes back to host), 
    //then check if there's winner(s)
    if (game.getCurrentPlayer().getUsername().equals(game.getCreatorId())) {
      //game.getBoard().setWinner("winner test");
      String temp = game.checkWinState();
      if (temp != null) {
        game.setWinner(temp);
        game.getBoard().setWinner(game.getWinner());
      }
    }
  }


  /**
   * Checks to see if player gets free token after a purchase.
   *
   * @param inventory of the player
   * @return boolean of if they deserve a token
   */
  public boolean checkFreeToken(Inventory inventory) {
    for (Unlockable u : inventory.getUnlockables()) {
      if (u instanceof TradingPost && ((TradingPost) u).getAction() instanceof FreeToken) {
        return true;
      }
    }
    return false;
  }


  /**
   * Checks to see which nobles the player has impressed.
   *
   * @param inventory of the player
   * @param board     of the current game
   * @return JSONArray of noble ids
   */
  public JSONArray checkImpressedNobles(Inventory inventory, Board board) {
    JSONArray noblesVisiting = new JSONArray();
    for (int nobleId : board.getNobles().attemptImpress(inventory)) {
      noblesVisiting.add(nobleId);
    }
    for (Noble noble : inventory.getReservedNobles()) {
      if (noble.impressed(inventory.getBonuses())) {
        noblesVisiting.add(noble.getId());
      }
    }
    return noblesVisiting;
  }

  /**
   * Checks to see if player can correctly pair a satchel card.
   *
   * @param inventory of the player
   * @return boolean of if they can pair it
   */
  public boolean checkValidPairing(Inventory inventory) {
    boolean valid = false;
    for (int i = 0; i < inventory.getCards().size(); i++) {
      if (inventory.getCards().get(i).getBonus().getType() != null
          && inventory.getCards().get(i).getBonus().getType() != Token.GOLD) {
        valid = true;
        break;
      }
    }
    return valid;
  }
}
