package ca.mcgill.splendorserver.models.registries;

import java.util.HashMap;
import java.util.Set;

import ca.mcgill.splendorserver.models.Noble;

public class NobleRegistry implements Registry<Noble> {
	
	@SuppressWarnings("serial")
	HashMap<Integer, Noble> data = new HashMap<Integer, Noble>() {{
		put(0, new Noble(0, 0, 0, 0, 0, 0, 0));
	}};

	@Override
	public Set<Integer> listIds() {
		return data.keySet();
	}

	@Override
	public Noble of(int id) {
		return data.get(id);
	}
	
}
