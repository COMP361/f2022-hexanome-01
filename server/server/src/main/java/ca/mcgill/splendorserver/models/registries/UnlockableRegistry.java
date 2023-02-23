package ca.mcgill.splendorserver.models.registries;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import ca.mcgill.splendorserver.apis.GameController;
import ca.mcgill.splendorserver.apis.JsonHandler;
import ca.mcgill.splendorserver.models.expansion.City;
import ca.mcgill.splendorserver.models.expansion.TradingPost;
import ca.mcgill.splendorserver.models.expansion.Unlockable;

public class UnlockableRegistry implements Registry<Unlockable> {
	
	@SuppressWarnings("serial")
	static HashMap<Integer, Unlockable> data = new HashMap<Integer, Unlockable>();
	
	static {
	    try {
	      InputStream is = GameController.class.getClassLoader().getResourceAsStream("Unlockables.txt");
	      JSONArray unlockables = (JSONArray)JsonHandler.decodeJsonRequest(new String(is.readAllBytes(), StandardCharsets.UTF_8));
	    	
	      for(Object obj : unlockables) {
	    	  JSONObject json = (JSONObject)(JsonHandler.decodeJsonRequest((String)obj));
	    	  Unlockable unlockable;
	    	  if (json.get("effectType").equals("City")) {
	    		  unlockable = new City(json);
	    	  }
	    	  else {
	    		  unlockable = new TradingPost(json);
	    	  }
	    	  data.put(unlockable.getId(), unlockable);
	      }
	      
	    } catch (IOException e) {
	      e.printStackTrace();
		} 
	  }
	
	@Override
	public Set<Integer> listIds() {
		return data.keySet();
	}

	@Override
	public Unlockable of(int id) {
		return data.get(id);
	}
	
}
