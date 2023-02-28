using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using UnityEngine;
using UnityEngine.Networking;

public class GameRequestManager : MonoBehaviour
{
    private static string HOST = Environment.GetEnvironmentVariable("SPLENDOR_HOST_IP");

    //TO DO: Long-polling with server -> see LSRequestManager.GetSession(...)
    public static IEnumerator GetBoard(string id, Action<JSONObject> result) {
        string url = "http://" + HOST + ":4244/splendor/api/games/" + id + "/board"; //url for GET request
        UnityWebRequest request = UnityWebRequest.Get(url);
        yield return request.SendWebRequest();

        if (request.result == UnityWebRequest.Result.Success)
        {
            result((JSONObject) JSONHandler.DecodeJsonRequest(request.downloadHandler.text));
        }
    }
}
