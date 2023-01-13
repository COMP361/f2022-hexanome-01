using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using UnityEngine;

public static class JSONHandler {
    private static JSONParser jsonParser = new JSONParser();

    //JSONObject is essentially a hashmap. for encoding, you pass a hashmap 
    //'parameters' that encodes a parameter name to a value, e.g parameter "points" => "5"
    //the hashmap stored in the JSONObject is what actually contains the 
    //parameters/info of the object, which must be constructed semi-manually
    //parameters names (keys) can be anything, as long as your implementation is aware
    //of what its looking for (i.e. can make it ints for indexes of an array)
    //and for ease of use, can pass 'type' string so that, if desired, 
    //can check to see if the encoded/decoded object is of a certain type (e.g. Card) 
    //(or can pass the method that made the call, or any other custom string, up to you)
    //obviously dont need to use it if you use design by contract/dont wanna.
    
    //pass type and hashmap to encodeJSONRequest, pass a JSON string to decodeJSONRequest
    
    //in JSONObject, JSONArray, JSONValue, .toString() == .toJSONString() == string representation of JSON object
    
    //ps: Dictionary is c# version of a hashmap. TMYK

    public static JSONObject EncodeJsonRequest(string type, Dictionary<string, string> parameters) {
        JSONObject obj = new JSONObject(new Dictionary<string, object>());

        obj.Add("type", type);

        if (parameters != null) {
            obj.Add("parameters", parameters);
        }
        return obj; //NOTE: obj.get("type") is type, obj.get("parameters") is the hasmap of info
    }

    //NOTE, ONLY PASS AN ARRAYLIST OF STRINGS
    public static JSONArray EncodeJsonRequest(string type, ArrayList parameters) {
        JSONArray arr = new JSONArray();

        arr.Add(type);

        if (parameters != null) {
            arr.Add(parameters);
        }
        return arr; //NOTE: arr.get(0) is type, arr.get(1) is the array of info
    }

    public static object DecodeJsonRequest(string message) {
        try {
            return jsonParser.Parse(message);
        }
        catch (ParseException e) {
            Debug.Log(e.Message);
            Debug.Log(e.StackTrace);
            Debug.Log(e.GetPosition());
            Debug.Log(e.GetUnexpectedObject().ToString());
            return null;
        }
    }

    public static object ReadJsonFile(string filename) {
        try {
            return jsonParser.Parse(new StringReader(filename));
        }
        catch (Exception e) when (e is IOException || e is ParseException) {
            Debug.Log(e.StackTrace);
        }
        return null;
        }
    }
