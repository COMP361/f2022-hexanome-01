using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using UnityEngine;
using UnityEngine.Networking;

public class ActionManager : MonoBehaviour
{
    private static string HOST = Environment.GetEnvironmentVariable("SPLENDOR_HOST_IP");

    // Performs Generic action
    public static IEnumerator PerformAction(string id, string type, Action<JSONObject> result) {
        string url = "http://" + HOST + ":4244/splendor/api/action/" + id + "/" + type; //url for GET request
        UnityWebRequest request = UnityWebRequest.Get(url);
        yield return request.SendWebRequest();

        if (request.result == UnityWebRequest.Result.Success)
        {
            result((JSONObject) JSONHandler.DecodeJsonRequest(request.downloadHandler.text));
        }
    }
}
