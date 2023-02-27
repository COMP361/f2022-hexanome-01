using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using UnityEngine;
using UnityEngine.Networking;

public class GameRequestManager : MonoBehaviour
{
    private static string HOST = Environment.GetEnvironmentVariable("SPLENDOR_HOST_IP");

    public static GameRequestManager instance;

    void Start() {
        instance = this;
    }

    //TO DO: Long-polling with server -> see LSRequestManager.GetSession(...)
    public static IEnumerator GetBoard(Board board, string id) {
        string url = "http://" + HOST + ":4244/splendor/api/games/" + id + "/board"; //url for GET request
        UnityWebRequest request = UnityWebRequest.Get(url);
        yield return request.SendWebRequest();

        if (request.result == UnityWebRequest.Result.Success)
        {
            board.SetBoard((JSONObject) JSONHandler.DecodeJsonRequest(request.downloadHandler.text));
        }
    }
}
