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
    public string apiBaseUrl = $"http://{HOST}:4244/splendor/api";

    // The GameObject that will display the error popup
    public GameObject errorPopup;

    // The text to display in the error popup
    public string errorPopupText = "Invalid selection";

    // This enum represents the different types of actions that can be performed
    public enum ActionType
    {
        performCardPurchase,
        takeTokens,
        reserveCard,
        selectNoble,
    }

    public void MakeApiRequest(string gameId, JSONObject jsonPayload, ActionType actionType, Action<JSONObject> callback)
    {
        string apiEndpointUrl = GetApiEndpointUrl(gameId, actionType);

        // Create a new HTTP request object
        UnityWebRequest webRequest = UnityWebRequest.Post(apiEndpointUrl, "POST");

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
            if (errorPopup != null)
            {
                errorPopup.SetActive(true);
                // Set the text of the error popup
                errorPopup.GetComponentInChildren<Text>().text = errorPopupText;
            }
            callback(null);
        }
        else
        {
            string jsonResponseString = webRequest.downloadHandler.text;
            // Create a new JSONObject
            JSONObject jsonObject = new JSONObject();

            jsonObject.Parse(jsonObject);
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