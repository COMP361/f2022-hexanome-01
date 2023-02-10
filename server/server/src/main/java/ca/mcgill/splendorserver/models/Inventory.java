package ca.mcgill.splendorserver.models;

import java.util.ArrayList;

public class Inventory {
	
	private TokenBank tokens;
	private TokenBank bonus;
	private ArrayList<Card> cards;
	private ArrayList<Noble> nobles;
	private ArrayList<Card> reservedCards;
	private ArrayList<Noble> reservedNobles;
	
	public Inventory() {
		tokens = new TokenBank();
		bonus = new TokenBank();
	}
	
	public boolean addTokens(String[] acquiredTokens) {
		return tokens.addAll(acquiredTokens);
	}

	public void reserve(Card card){
		reservedCards.add(card);
	}
	public void reserveNoble(Noble noble){
		reservedNobles.add(noble);
	}

	public void addNobleToInventory(Noble noble){
		nobles.add(noble);
	}
	public void addCard(Card card){
		cards.add(card);
	}
	public boolean removeCard(Card card){
		return cards.remove(card);
	}
	public TokenBank getTokens() {
		return tokens;
	}

	public TokenBank getBonus() {
		return bonus;
	}
}
