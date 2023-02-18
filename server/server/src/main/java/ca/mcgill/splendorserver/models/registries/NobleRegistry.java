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
import ca.mcgill.splendorserver.models.Noble;

public class NobleRegistry implements Registry<Noble> {
	
	@SuppressWarnings("serial")
	static HashMap<Integer, Noble> data = new HashMap<Integer, Noble>();
	
	static {
	    try {
	      InputStream is = GameController.class.getClassLoader().getResourceAsStream("Nobles.txt");
	      JSONArray nobles = (JSONArray)JsonHandler.decodeJsonRequest(new String(is.readAllBytes(), StandardCharsets.UTF_8));
	    	
	      for(Object obj : nobles) {
	    	  Noble noble = new Noble((JSONObject)(JsonHandler.decodeJsonRequest((String)obj)));
	    	  data.put(noble.getId(), noble);
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
	public Noble of(int id) {
		return data.get(id);
	}
	
}
