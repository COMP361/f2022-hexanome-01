package ca.mcgill.splendorserver.models;

public class PlayerData {
	private String username, token; //should password be added?
	private CardData[] inventory, reserved;
	private NobleData[] nobles;
	private int[] discounts = new int[5]; //order: red, green, blue, brown, white
	
    public String getUsername() {
		return username;
	}
	
    public void setUsername(String username) {
		this.username = username;
	}
	
    public String getToken() {
		return token;
	}
	
    public void setToken(String token) {
		this.token = token;
	}
	
    public CardData[] getInventory() {
		return inventory;
	}
	
    public void setInventory(CardData[] inventory) {
		this.inventory = inventory;
	}
	
    public CardData[] getReserved() {
		return reserved;
	}
	
    public void setReserved(CardData[] reserved) {
		this.reserved = reserved;
	}
	
    public NobleData[] getNobles() {
		return nobles;
	}
	
    public void setNobles(NobleData[] nobles) {
		this.nobles = nobles;
	}
	
    public int[] getDiscounts() {
		return discounts;
	}
	
    public void setDiscounts(int[] discounts) {
		this.discounts = discounts;
	}

}
