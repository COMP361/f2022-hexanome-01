package ca.mcgill.splendorserver.models;

import java.util.ArrayList;

public class Player {
	
	private Inventory inventory;
	private ArrayList<IUnlockable> unlockables;
	private Card lastAquired;

	public void aquireCard(Card card){
	    inventory.addCard(card);
    }
    public boolean removeCard(Card card){
	    return inventory.removeCard(card);
    }

    public ArrayList<IUnlockable> getUnlockables(){
        return unlockables;
    }
    public Card getLastAcquired(){
        return lastAquired;
    }




}
