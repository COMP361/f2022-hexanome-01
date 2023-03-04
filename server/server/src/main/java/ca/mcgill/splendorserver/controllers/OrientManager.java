package ca.mcgill.splendorserver.controllers;

import ca.mcgill.splendorserver.models.Inventory;
import ca.mcgill.splendorserver.models.Noble;
import ca.mcgill.splendorserver.models.Token;
import ca.mcgill.splendorserver.models.board.Board;
import ca.mcgill.splendorserver.models.cards.Card;
import ca.mcgill.splendorserver.models.cards.CardLevel;
import ca.mcgill.splendorserver.models.cards.CardType;
import ca.mcgill.splendorserver.models.registries.CardRegistry;
import ca.mcgill.splendorserver.models.registries.NobleRegistry;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Class containing the methods for handling orient functionality.
 */
public class OrientManager {

  /**
   * Handles orient noble selection.
   *
   * @param card with action.
   * @param board of current game.
   * @param inventory of player making the action.
   * @return the JSONObject response containing the action being done, and choices for user.
   */
  public static JSONObject handleCard(Card card, Board board, Inventory inventory) {
    switch (card.getType()) {
      case DOMINO1: return domino(card, board, inventory);
      case DOMINO2: return domino(card, board, inventory);
      case RESERVE: return reserve(card, board);
      case SATCHEL: return satchel(card, inventory);
      case SACRIFICE: return sacrifice(card, inventory);
      default: return null;
    }
  }
  
  /**
   * Handles orient domino cards.
   *
   * @param card causing domino.
   * @param board of current game.
   * @param inventory of player making the action.
   * @return the JSONObject response containing the action being done, and choices for user.
   */
  @SuppressWarnings("unchecked")
  public static JSONObject domino(Card card, Board board, Inventory inventory) {
    JSONObject response = new JSONObject();
    response.put("type", card.getType().toString());
    ArrayList<Integer> choices = new ArrayList<Integer>();
  
    if (card.getType() == CardType.DOMINO1) { //pass satchelable cards
      for (int i = 0; i < inventory.getCards().size(); i++) {
        if (inventory.getCards().get(i).getId() != card.getId()) {
          Card currentCard = inventory.getCards().get(i);
          if (currentCard.getBonus().getType() != null 
              && currentCard.getBonus().getType() != Token.GOLD) {
            choices.add(inventory.getCards().get(i).getId());
          }
        }
      }
    } else { //pass free cards choices (i.e. this is a domino2)
      choices = OrientManager.getDominoOptions(board, 2);
    }
  
    response.put("options", JSONArray.toJSONString(choices));
    
    return response;
  }
 
  /**
   * Handles orient domino cards.
   *
   * @param board of current game.
   * @param level of card desired.
   * @return the list of all the cards on the board of the given level.
   */
  public static ArrayList<Integer> getDominoOptions(Board board, int level) {
    int[] regularRow;
    int[] orientRow;

    switch (level) {
      case 1: regularRow = board.getCards().getRows().get(CardLevel.LEVEL1);
        orientRow = board.getCards().getRows().get(CardLevel.ORIENTLEVEL1);
        break;
      case 2: regularRow = board.getCards().getRows().get(CardLevel.LEVEL2);
        orientRow = board.getCards().getRows().get(CardLevel.ORIENTLEVEL2);
        break;
      case 3: regularRow = board.getCards().getRows().get(CardLevel.LEVEL3);
        orientRow = board.getCards().getRows().get(CardLevel.ORIENTLEVEL3);
        break;
      default: regularRow = new int[0]; 
        orientRow = new int[0];
        break;
    }

    ArrayList<Integer> choices = new ArrayList<Integer>();
    for (int i = 0; i < regularRow.length; i++) {
      choices.add(CardRegistry.of(regularRow[i]).getId());
    }
    for (int i = 0; i < orientRow.length; i++) {
      choices.add(CardRegistry.of(orientRow[i]).getId());
    }
    return choices;
  }
  
  /**
   * Handles orient satchel cards.
   *
   * @param card that has been acquired.
   * @param inventory of player making the action.
   * @return the JSONObject response containing the action being done, and choices for user.
   */
  @SuppressWarnings("unchecked")
public static JSONObject satchel(Card card, Inventory inventory) {
    JSONObject response = new JSONObject();
    response.put("type", card.getType().toString());
    ArrayList<Integer> choices = new ArrayList<Integer>();

    for (int i = 0; i < inventory.getCards().size(); i++) {
      if (inventory.getCards().get(i).getId() != card.getId()) {
        Card currentCard = inventory.getCards().get(i);
        if (currentCard.getBonus().getType() != null 
            && currentCard.getBonus().getType() != Token.GOLD) {
          choices.add(inventory.getCards().get(i).getId());
        }
      }
    }

    response.put("options", JSONArray.toJSONString(choices));

    return response;
  }
  
  /**
   * Handles orient noble reservation cards.
   *
   * @param card that has been acquired.
   * @param board of current game.
   * @return the JSONObject response containing the action being done, and choices for user.
   */
  @SuppressWarnings("unchecked")
public static JSONObject reserve(Card card, Board board) {
    JSONObject response = new JSONObject();
    response.put("type", card.getType().toString());
    ArrayList<Integer> choices = new ArrayList<Integer>();
    for (int i = 0; i < board.getNobles().getNobles().length; i++) {
      choices.add(board.getNobles().getNobles()[i]);
    }

    response.put("options", JSONArray.toJSONString(choices));

    return response;
  } 
  
  /**
   * Handles orient sacrifice cards.
   *
   * @param card that has been acquired.
   * @param inventory of player making the action.
   * @return the JSONObject response containing the action being done, and choices for user.
   */
  public static JSONObject sacrifice(Card card, Inventory inventory) {
    return null;
    //asks player to select which cards they desire to sacrifice
    //prioritizes sacrificing cards with satchel value != 0
    //if valid cards selected (2 cards, or 1 with bonus of 2),
    //remove them from inventory and add this card
  } 
  
  /**
   * Handles orient satchel selection.
   *
   * @param card to attach the satchel to.
   * @param inventory of player that the card resides in.
   * @return whether the action was valid.
   */
  public static boolean addSatchel(Card card, Inventory inventory) {
    if (card == null || !inventory.getCards().contains(card)) {
      return false;
    }
    card.addSatchel();
    return true;
  }
  
  /**
   * Handles orient noble selection.
   *
   * @param noble to reserve.
   * @param board of current game.
   * @param inventory of player making the action.
   * @return whether the action was valid.
   */
  public static boolean reserveNoble(Noble noble, Board board, Inventory inventory) {
    if (noble == null || !board.getNobles().contains(noble.getId())) {
      return false;
    }
    inventory.reserveNoble(noble);
    return true;
  }
  
  /**
   * Handles orient sacrifice selection.
   *
   * 
   */
  public static boolean makeSacrifice() {
    return true;
  }
}
