package ca.mcgill.splendorserver.controllers;

import ca.mcgill.splendorserver.models.cards.Card;
import ca.mcgill.splendorserver.models.cards.CardLevel;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Class containing the methods for handling orient functionality.
 */
public class OrientManager {

  /**
   * Handles orient card types.
   *
   * @param card that has been acquired, whose type has to be dealt with.
   */
  public void handleCard(Card card) {
    switch (card.getType()) {
      case DOMINO1: break;
      case DOMINO2: break;
      case RESERVE: break;
      case SATCHEL: break;
      default: break;
    }
  }
  
  /**
   * Handles orient domino cards.
   *
   * @param card that has been acquired, and level of desired domino effect.
   */
  public void domino(Card card, CardLevel level) {
    //prompts player to take a card of level for free
    //if level is lowest, then it was a domino1 card to trigger this. do satchela action first
    
  }
  
  /**
   * Handles orient satchel cards.
   *
   * @param card that has been acquired.
   */
  public void satchel(Card card) {
    //asks player to select a card in their invenroty.
    //increments the satchel value on selected card
    //updates player bonuses
  } 
  
  /**
   * Handles orient noble reservation cards.
   *
   * @param card that has been acquired.
   */
  public void reserve(Card card) {
    //asks player to select a noble to reserve
    //adds noble to player's noble reserve inventory list
  } 
  
  /**
   * Handles orient sacrifice cards.
   *
   * @param card that has been acquired.
   */
  public void sacrifice(Card card) {
    //asks player to select which cards they desire to sacrifice
    //prioritizes sacrificing cards who satchel value != 0
    //if valid cards selected (2 cards, or 1 with bonus of 2), remove them from ivnentory and add this card
  } 
}
