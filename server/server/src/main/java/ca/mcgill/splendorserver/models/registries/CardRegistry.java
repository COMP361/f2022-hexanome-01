package ca.mcgill.splendorserver.models.registries;

import java.util.HashMap;
import java.util.Set;

import ca.mcgill.splendorserver.models.cards.Card;

public class CardRegistry implements Registry<Card> {
	
	@SuppressWarnings("serial")
	HashMap<Integer, Card> data = new HashMap<Integer, Card>() {{
		put(0, new Card(0, null, 0, 0, 0, 0, 0, 0, null, null));
	}};

	@Override
	public Set<Integer> listIds() {
		return data.keySet();
	}

	@Override
	public Card of(int id) {
		return data.get(id);
	}
	
}
