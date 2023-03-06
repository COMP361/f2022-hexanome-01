using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.UI;



public class ActionManager : MonoBehaviour
{
    private static string HOST = Environment.GetEnvironmentVariable("SPLENDOR_HOST_IP");

    // The base URL of the REST API
    private string apiBaseUrl = $"http://{HOST}:4244/splendor/api";

    // The GameObject that will display the error popup
    // public GameObject errorPopup;


    // public string errorPopupText = "Invalid selection";


    public enum ActionType
    {
        purchaseCard,
        takeTokens,
        reserveCard,
        selectNoble,
        endTurn,
    }

    public enum RequestType
    {
        PUT,
        POST,
        DELETE,
        GET,
    }

    public void MakeApiRequest(string gameId, JSONObject jsonPayload, ActionType actionType,RequestType requestType, Action<JSONObject> callback)
    {
        string apiEndpointUrl = GetApiEndpointUrl(gameId, actionType);

        Debug.Log(apiEndpointUrl);
        Debug.Log(jsonPayload.ToString());

        // Create a new HTTP request object
       UnityWebRequest webRequest;

        switch (requestType)
        {
            case RequestType.POST:
                webRequest = UnityWebRequest.Post(apiEndpointUrl, "POST");
                break;
            default:
                webRequest = UnityWebRequest.Post(apiEndpointUrl, "POST");
                break;
        }

        // Set the content type to "application/json"
        webRequest.SetRequestHeader("Content-Type", "application/json");

        // Encode the JSON payload using the JSONHandler class and set it in the request body
        byte[] payloadBytes = System.Text.Encoding.UTF8.GetBytes(jsonPayload.ToString());
        webRequest.uploadHandler = new UploadHandlerRaw(payloadBytes);

        // Start the HTTP request as a coroutine
        StartCoroutine(SendApiRequest(webRequest, callback));
    }

    private IEnumerator SendApiRequest(UnityWebRequest webRequest, Action<JSONObject> callback)
    {
        // Send the HTTP request and wait for the response
        yield return webRequest.SendWebRequest();

        // Handle the HTTP response
        if (webRequest.result != UnityWebRequest.Result.Success)
        {
            Debug.LogError("API request failed: " + webRequest.error);
            // if (errorPopup != null)
            // {
            //     errorPopup.SetActive(true);
            //     // Set the text of the error popup
            //     errorPopup.GetComponentInChildren<Text>().text = errorPopupText;
            // }
            callback(null);
        }
        else
        {
            
            Debug.Log(webRequest.downloadHandler.text);
            JSONObject jsonObject = (JSONObject)JSONHandler.DecodeJsonRequest(webRequest.downloadHandler.text);

            callback(jsonObject);

        }
    }

    private string GetApiEndpointUrl(string gameId, ActionType actionType)
    {
        // Construct the API endpoint URL based on the selected action type
        string actionPath = actionType.ToString();
        string apiEndpointUrl = $"{apiBaseUrl}/action/{gameId}/{actionPath}";
        return apiEndpointUrl;
    }

    // Custom JSON parser function
    
}


// public class ActionManager : MonoBehaviour
// {
//     private static string HOST = Environment.GetEnvironmentVariable("SPLENDOR_HOST_IP");

//     // Performs Generic action
//     public static IEnumerator PerformAction(string id, string type, Action<JSONObject> result) {
//         string url = "http://" + HOST + ":4244/splendor/api/action/" + id + "/" + type; //url for GET request
//         UnityWebRequest request = UnityWebRequest.Get(url);
//         yield return request.SendWebRequest();

//         if (request.result == UnityWebRequest.Result.Success)
//         {
//             result((JSONObject) JSONHandler.DecodeJsonRequest(request.downloadHandler.text));
//         }
//     }
// }