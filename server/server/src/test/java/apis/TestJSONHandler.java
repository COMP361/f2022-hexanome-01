package apis;

import static org.junit.Assert.assertEquals;

import ca.mcgill.splendorserver.apis.JSONHandler;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;

public class TestJSONHandler {
  
  @Test
  public void testObjectEncoding() {
    HashMap<String, String> objMapping = new HashMap<String, String>();
    objMapping.put("points", "5");
    objMapping.put("price", "6$");
    JSONObject json = JSONHandler.encodeJsonRequest("item", objMapping);
    JSONObject decodedJson = (JSONObject) JSONHandler.decodeJsonRequest(json.toString());
    JSONObject objDecoded = (JSONObject) 
        JSONHandler.decodeJsonRequest(decodedJson.get("params").toString());
    assertEquals("item", decodedJson.get("type"));
    assertEquals("5", objDecoded.get("points"));
    assertEquals("6$", objDecoded.get("price"));
  }
  
  @Test
  public void testArrayEncoding() {
    ArrayList<String> arrMapping = new ArrayList<String>();
    arrMapping.add("5");
    arrMapping.add("6");
    JSONArray json = JSONHandler.encodeJsonRequest("item", arrMapping);
    JSONArray decodedJson = (JSONArray) JSONHandler.decodeJsonRequest(json.toString());
    JSONArray arrDecoded = (JSONArray) JSONHandler.decodeJsonRequest(decodedJson.get(1).toString());
    assertEquals("item", decodedJson.get(0));
    assertEquals("5", arrDecoded.get(0));
    assertEquals("6", arrDecoded.get(1));
  }
  
  @Test
  public void testNestedEncoding() {
    HashMap<String, String> objMapping1 = new HashMap<String, String>();
    HashMap<String, String> objMapping2 = new HashMap<String, String>();
    ArrayList<String> arrMapping = new ArrayList<String>();
    
    objMapping1.put("points", "5");
    objMapping1.put("price", "6$");
    objMapping2.put("points", "8");
    objMapping2.put("price", "12$");
    JSONObject jsonObj1 = JSONHandler.encodeJsonRequest("item", objMapping1);
    JSONObject jsonObj2 = JSONHandler.encodeJsonRequest("item", objMapping2);
    
    arrMapping.add(jsonObj1.toString());
    arrMapping.add(jsonObj2.toString());
    JSONArray jsonArr = JSONHandler.encodeJsonRequest("inventory", arrMapping);
    
    JSONArray decodedArr = (JSONArray) JSONHandler.decodeJsonRequest(jsonArr.toString());
    JSONArray arrElements = (JSONArray) JSONHandler.decodeJsonRequest(decodedArr.get(1).toString());
    JSONObject obj1Decoded = (JSONObject) 
        JSONHandler.decodeJsonRequest(arrElements.get(0).toString());
    JSONObject obj2Decoded = (JSONObject) 
        JSONHandler.decodeJsonRequest(arrElements.get(1).toString());
    JSONObject obj1Params = (JSONObject) 
        JSONHandler.decodeJsonRequest(obj1Decoded.get("params").toString());
    JSONObject obj2Params = (JSONObject) 
        JSONHandler.decodeJsonRequest(obj2Decoded.get("params").toString());
    
    assertEquals("inventory", decodedArr.get(0));
    assertEquals("item", obj1Decoded.get("type"));
    assertEquals("item", obj2Decoded.get("type"));
    assertEquals("5", obj1Params.get("points"));
    assertEquals("12$", obj2Params.get("price"));
  }
}
