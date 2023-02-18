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
import ca.mcgill.splendorserver.models.cards.Card;
import ca.mcgill.splendorserver.models.cards.CardLevel;

public class CardRegistry implements Registry<Card> {
	
	@SuppressWarnings("serial")
	static HashMap<Integer, Card> data = new HashMap<Integer, Card>();
	
	static {
	    try {
	      InputStream is = GameController.class.getClassLoader().getResourceAsStream("Deck1.txt");
	      JSONArray deck1 = (JSONArray)JsonHandler.decodeJsonRequest(new String(is.readAllBytes(), StandardCharsets.UTF_8));
	    	
	      is = GameController.class.getClassLoader().getResourceAsStream("Deck2.txt");
	      JSONArray deck2 = (JSONArray)JsonHandler.decodeJsonRequest(new String(is.readAllBytes(), StandardCharsets.UTF_8));
	    	
	      is = GameController.class.getClassLoader().getResourceAsStream("Deck3.txt");
	      JSONArray deck3 = (JSONArray)JsonHandler.decodeJsonRequest(new String(is.readAllBytes(), StandardCharsets.UTF_8));
	    	
	      is = GameController.class.getClassLoader().getResourceAsStream("exDeck1.txt");
	      JSONArray exDeck1 = (JSONArray)JsonHandler.decodeJsonRequest(new String(is.readAllBytes(), StandardCharsets.UTF_8));
	    	
	      is = GameController.class.getClassLoader().getResourceAsStream("exDeck2.txt");
	      JSONArray exDeck2 = (JSONArray)JsonHandler.decodeJsonRequest(new String(is.readAllBytes(), StandardCharsets.UTF_8));
	    	
	      is = GameController.class.getClassLoader().getResourceAsStream("exDeck3.txt");
	      JSONArray exDeck3 = (JSONArray)JsonHandler.decodeJsonRequest(new String(is.readAllBytes(), StandardCharsets.UTF_8));

	      for(Object obj : deck1) {
	    	  Card card = new Card((JSONObject)(JsonHandler.decodeJsonRequest((String)obj)), CardLevel.LEVEL1);
	    	  data.put(card.getId(), card);
	      }
	      
	      for(Object obj : deck2) {
	    	  Card card = new Card((JSONObject)(JsonHandler.decodeJsonRequest((String)obj)), CardLevel.LEVEL2);
	    	  data.put(card.getId(), card);
	      }
	      
	      for(Object obj : deck3) {
	    	  Card card = new Card((JSONObject)(JsonHandler.decodeJsonRequest((String)obj)), CardLevel.LEVEL3);
	    	  data.put(card.getId(), card);
	      }
	      
	      for(Object obj : exDeck1) {
	    	  Card card = new Card((JSONObject)(JsonHandler.decodeJsonRequest((String)obj)), CardLevel.ORIENT_LEVEL1);
	    	  data.put(card.getId(), card);
	      }
	      
	      for(Object obj : exDeck2) {
	    	  Card card = new Card((JSONObject)(JsonHandler.decodeJsonRequest((String)obj)), CardLevel.ORIENT_LEVEL2);
	    	  data.put(card.getId(), card);
	      }
	      
	      for(Object obj : exDeck3) {
	    	  Card card = new Card((JSONObject)(JsonHandler.decodeJsonRequest((String)obj)), CardLevel.ORIENT_LEVEL3);
	    	  data.put(card.getId(), card);
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
	public Card of(int id) {
		return data.get(id);
	}
	
}
