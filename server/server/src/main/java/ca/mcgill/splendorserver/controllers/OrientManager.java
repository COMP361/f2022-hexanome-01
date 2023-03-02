package ca.mcgill.splendorserver.controllers;

import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.cards.Card;
import ca.mcgill.splendorserver.models.cards.CardLevel;

/**
 * Class containing the methods for handling orient functionality.
 */
public class OrientManager {

  /**
   * Handles orient card types.
   *
   * @param card that has been acquired, whose type has to be dealt with.
   */
  public static void handleCard(Card card) {
    switch (card.getType()) {
      case DOMINO1: 
        domino(card, 1);
        break;
      case DOMINO2: 
        domino(card, 2);
        break;
      case RESERVE: 
        reserve();
        break;
      case SATCHEL: 
        satchel(card);
        break;
      default: break;
    }
  }
  
  /**
   * Handles orient domino cards.
   *
   * @param card that has been acquired, and level of desired domino effect.
   * @param level int of level of desired domino effect.
   */
  @SuppressWarnings("unchecked")
public static void domino(Card card, int level) {
    //prompts player to take a card of level for free
    //if level is lowest, then it was a domino1 card to trigger this. do satchela action first
    JSONObject jsonresponse = new JSONObject();
    jsonresponse.put("type", "domino");
    jsonresponse.put("options", "list of options to display");
    ResponseEntity<String> re = new ResponseEntity<String>(jsonresponse.toJSONString(), HttpStatus.OK);
  }
  
  /**
   * Handles orient satchel cards.
   *
   * @param card that has been acquired.
   */
  public static void satchel(Game game, String playerId) {
    //asks player to select a card in their invenroty.
    //increments the satchel value on selected card
    //updates player bonuses
  } 
  
  /**
   * Handles orient noble reservation cards.
   *
   * @param card that has been acquired.
   */
  public static void reserve() {
    //asks player to select a noble to reserve
    //adds noble to player's noble reserve inventory list
  } 
  
  /**
   * Handles orient sacrifice cards.
   *
   * @param card that has been acquired.
   */
  public static void sacrifice(Game game, String playerId, int cardId) {
    //asks player to select which cards they desire to sacrifice
    //prioritizes sacrificing cards who satchel value != 0
    //if valid cards selected (2 cards, or 1 with bonus of 2), remove them from ivnentory and add this card
  } 
}
