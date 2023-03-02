package ca.mcgill.splendorserver.models.communicationbeans;

/**
 * Communication bean to receive data from when player
 * wants to reserve a card.
 * POST "/api/action/{gameId}/reserveCard"
 */
public class ReserveCardData {

  private String player;
  private int card;

  /**
   * Constructor for the data received when a player wants
   * to reserver a card.
   *
   * @param player username of plauyer that wants to reserve card
   * @param card   card that he wants to reserve (Unique ID)
   */
  public ReserveCardData(String player, int card) {
    this.player = player;
    this.card = card;
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
}
