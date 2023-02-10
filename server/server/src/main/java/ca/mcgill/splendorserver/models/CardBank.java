package ca.mcgill.splendorserver.models;

import java.util.Collections;
import java.util.Stack;

public class CardBank {
	
	private Card[][] cards = new Card[6][3];

	private Stack<Card> deck1; // TODO: Manually add all card data to these decks
	private Stack<Card> deck2;
	private Stack<Card> deck3;
	
	private Stack<Card> orientDeck1;
	private Stack<Card> orientDeck2;
	private Stack<Card> orientDeck3;
	
	public CardBank() {
		
		Collections.shuffle(deck1);
		Collections.shuffle(deck2);
		Collections.shuffle(deck3);
		Collections.shuffle(orientDeck1);
		Collections.shuffle(orientDeck2);
		Collections.shuffle(orientDeck3);
		
		for (int i=0; i<3; i++) {
			cards[0][i] = deck1.pop();
			cards[1][i] = deck2.pop();
			cards[2][i] = deck3.pop();
		}
		for (int i=0; i<2; i++) {
			cards[3][i] = orientDeck1.pop();
			cards[4][i] = orientDeck2.pop();
			cards[5][i] = orientDeck3.pop();
		}
	}
	
	public void add(Card card) {
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++)
				if (cards[j][i] == null) cards[j][i] = card;
		}
		for (int i=0; i<2; i++) {
			for (int j=3; j<6; j++)
				if (cards[j][i] == null) cards[j][i] = card;
		}
	}
	
	public boolean remove(Card card) {
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				if (cards[j][i].equals(card)) cards[j][i] = null;
				return false;
			}
		}
		for (int i=0; i<2; i++) {
			for (int j=3; j<6; j++) {
				if (cards[j][i].equals(card)) cards[j][i] = null;
				return false;
			}
		}
		return true;
	}
	
	public Stack<Card> getDeckOf(Card card) {
		switch (card.getLevel()) {
		case LEVEL1:
			return deck1;
		case LEVEL2:
			return deck2;
		case LEVEL3:
			return deck3;
		case ORIENT_LEVEL1:
			return orientDeck1;
		case ORIENT_LEVEL2:
			return orientDeck2;
		case ORIENT_LEVEL3:
			return orientDeck3;
		}
		return null;
	}
	
}
