package ca.mcgill.splendorserver.models;

public class Game {
	
  Deck[] decks = new Deck[6];
	
  public Game() {
    for (Deck deck : decks)
      deck.shuffle();
    for (int i=0; i<3; i++)
      decks[i] = new Deck(4);
    for (int i=3; i<6; i++)
        decks[i] = new Deck(2);
  }
	
	

}
