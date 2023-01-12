package ca.mcgill.splendorserver.apis;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONHandler {
  private static JSONParser jsonParser = new JSONParser();

  //JSONObject is essentially a hashmap. for encoding, you pass a hashmap 
  //'params' that encodes a parameter to a value, e.g parameter "points" => "5"
  //the hashmap stored in the JSONObject is what actually contains the 
  //parameters/info of the object, which must be constructed semi-manually
  //param name can be anything, as long as your implementation is aware
  //of what its looking for (i.e. can make it ints for indexes of an array)
  //and for ease of use, can pass 'type' string so that, if desired, 
  //can check to see if the encoded/decoded object is of a certain type (e.g. Card) 
  //(or can pass the method that made the call, or any other custom string)
  //obviously dont need to use it if you use design by contract
  //pass type and hashmap to encodeJSONRequest, pass a JSON string to decodeJSONRequest
  //in JSON classes, .toString() == .toJSONString() == string representation of JSON object

  //MAYBE MAKE IT <Object, Object> hashmap instead? to allow mapping something like string => obj

  @SuppressWarnings("unchecked")
  public static JSONObject encodeJsonRequest(String type, HashMap<String, String> params) {
    JSONObject obj = new JSONObject();

    obj.put("type", type);
          
    if (params != null) {
      obj.put("params", params);
    }      
    return obj; //NOTE: obj.get("type") is type, obj.get("params") is the hasmap of info
  }
  
  @SuppressWarnings("unchecked")
  public static JSONArray encodeJsonRequest(String type, ArrayList<String> params) {
    JSONArray arr = new JSONArray();

    arr.add(type);
          
    if (params != null) {
      arr.add(params);
    }      
    return arr; //NOTE: arr.get(0) is type, arr.get(1) is the array of info
  }
	
  public static Object decodeJsonRequest(String message) {
    try {
      return jsonParser.parse(message);
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    }
  }
  
  public static Object readJsonFile(String filename) {
    try {
      return jsonParser.parse(new FileReader(filename));
    } catch (IOException | ParseException e) {
      e.printStackTrace();
    }
    return null;
  }
}
