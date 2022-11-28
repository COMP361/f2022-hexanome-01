package ca.mcgill.splendorserver.models;

public class PlayerData {
	private String username, access_token, refresh_token, expires_in; //should password be added?
	private CardData[] inventory, reserved;
	private NobleData[] nobles;
	private int[] discounts = new int[5]; //order: red, green, blue, brown, white
	
    public String getUsername() {
		return username;
	}
	
    public void setUsername(String username) {
		this.username = username;
	}
	
    public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public String getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
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
