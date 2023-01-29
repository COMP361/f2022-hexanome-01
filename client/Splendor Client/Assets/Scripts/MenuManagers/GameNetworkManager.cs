using System;
using System.Text;
using System.Collections;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Diagnostics;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.UI;

public class GameNetworkManager : MonoBehaviour
{
    //******************************** GAME DATA ********************************

    public static IEnumerator GetGame(string HOST, string id, Action<GameData> result) {
        string url = "http://" + HOST + ":4244/splendor/update/" + id; //url for GET request
        UnityWebRequest request = UnityWebRequest.Get(url);
        yield return request.SendWebRequest();

        if (request.result == UnityWebRequest.Result.Success)
            result(FileManager.DecodeGameState(request.downloadHandler.text, false));
    }
}
