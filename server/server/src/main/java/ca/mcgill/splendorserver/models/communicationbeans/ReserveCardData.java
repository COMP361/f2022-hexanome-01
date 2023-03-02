package ca.mcgill.splendorserver.models.communicationbeans;

/**
 * Communication bean to receive data from when player
 * wants to reserve a card.
 * POST "/api/action/{gameId}/reserveCard"
 */
public class ReserveCardData {

  private String player;
  private int card;
  private String deck;

  /**
   * Constructor for the data received when a player wants
   * to reserver a card.
   *
   * @param player username of plauyer that wants to reserve card
   * @param card   card that he wants to reserve (Unique ID)
   * @param deck   if reserve from deck it will be the name of the deck
   */
  public ReserveCardData(String player, int card, String deck) {
    this.player = player;
    this.card = card;
    this.deck = deck;
  }

  /**
   * Returns the player that wants to reserve a card.
   *
   * @return username of player
   */
  public String getPlayer() {
    return player;
  }

  /**
   * sets the player that wants to reserve a card.
   *
   * @param player username of player
   */
  public void setPlayer(String player) {
    this.player = player;
  }

  /**
   * Gets the card that the player wants to reserve.
   *
   * @return the unique Id of the card
   */
  public int getCard() {
    return card;
  }

  /**
   * Sets the card that the player wants to reserve.
   *
   * @param card card to be reserved
   */
  public void setCard(int card) {
    this.card = card;
  }

  /**
   * Returns the name of the deck.
   *
   * @return name of the deck
   */
  public String getDeck() {
    return deck;
  }

  /**
   * Sets the deck that we want to reserve a card from.
   *
   * @param deck name of deck
   */

  public void setDeck(String deck) {
    this.deck = deck;
  }
}
