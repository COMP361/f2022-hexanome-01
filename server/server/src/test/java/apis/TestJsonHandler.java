package apis;

import static org.junit.Assert.assertEquals;

import ca.mcgill.splendorserver.apis.JsonHandler;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;

/**
 * Tester for JSON Handling class for the server.
 */
public class TestJsonHandler {
  
  @Test
  public void testObjectEncoding() {
    HashMap<String, String> objMapping = new HashMap<String, String>();
    objMapping.put("points", "5");
    objMapping.put("price", "6$");
    JSONObject json = JsonHandler.encodeJsonRequest("item", objMapping);
    JSONObject decodedJson = (JSONObject) JsonHandler.decodeJsonRequest(json.toString());
    JSONObject objDecoded = (JSONObject) 
        JsonHandler.decodeJsonRequest(decodedJson.get("parameters").toString());
    assertEquals("item", decodedJson.get("type"));
    assertEquals("5", objDecoded.get("points"));
    assertEquals("6$", objDecoded.get("price"));
  }
  
  @Test
  public void testArrayEncoding() {
    ArrayList<String> arrMapping = new ArrayList<String>();
    arrMapping.add("5");
    arrMapping.add("6");
    JSONArray json = JsonHandler.encodeJsonRequest("item", arrMapping);
    JSONArray decodedJson = (JSONArray) JsonHandler.decodeJsonRequest(json.toString());
    JSONArray arrDecoded = (JSONArray) JsonHandler.decodeJsonRequest(decodedJson.get(1).toString());
    assertEquals("item", decodedJson.get(0));
    assertEquals("5", arrDecoded.get(0));
    assertEquals("6", arrDecoded.get(1));
  }
  
  @Test
  public void testNestedEncoding() {
    HashMap<String, String> objMapping1 = new HashMap<String, String>();
    HashMap<String, String> objMapping2 = new HashMap<String, String>();
    
    objMapping1.put("points", "5");
    objMapping1.put("price", "6$");
    objMapping2.put("points", "8");
    objMapping2.put("price", "12$");
    JSONObject jsonObj1 = JsonHandler.encodeJsonRequest("item", objMapping1);
    JSONObject jsonObj2 = JsonHandler.encodeJsonRequest("item", objMapping2);
    
    ArrayList<String> arrMapping = new ArrayList<String>();
    arrMapping.add(jsonObj1.toString());
    arrMapping.add(jsonObj2.toString());
    JSONArray jsonArr = JsonHandler.encodeJsonRequest("inventory", arrMapping);
    
    JSONArray decodedArr = (JSONArray) JsonHandler.decodeJsonRequest(jsonArr.toString());
    JSONArray arrElements = (JSONArray) JsonHandler.decodeJsonRequest(decodedArr.get(1).toString());
    JSONObject obj1Decoded = (JSONObject) 
        JsonHandler.decodeJsonRequest(arrElements.get(0).toString());
    JSONObject obj2Decoded = (JSONObject) 
        JsonHandler.decodeJsonRequest(arrElements.get(1).toString());
    JSONObject obj1Params = (JSONObject) 
        JsonHandler.decodeJsonRequest(obj1Decoded.get("parameters").toString());
    assertEquals("5", obj1Params.get("points"));
    JSONObject obj2Params = (JSONObject) 
        JsonHandler.decodeJsonRequest(obj2Decoded.get("parameters").toString()); 
    assertEquals("12$", obj2Params.get("price"));
    assertEquals("inventory", decodedArr.get(0));
    assertEquals("item", obj1Decoded.get("type"));
    assertEquals("item", obj2Decoded.get("type"));
  }
}
