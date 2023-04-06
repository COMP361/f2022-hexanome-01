using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using System.Security.Cryptography;
using UnityEngine;
using UnityEngine.Networking;

public class GameRequestManager : MonoBehaviour
{
    private static string HOST = Environment.GetEnvironmentVariable("SPLENDOR_HOST_IP");

    public static IEnumerator GetBoard(string id, string hash, Action<string, JSONObject> result) {
        string url = "http://" + HOST + ":4244/splendor/api/games/" + id + "/board"; //url for GET request
        if (hash != null) url += ("?hash=" + hash); //url for GET request
        UnityWebRequest request = UnityWebRequest.Get(url);
        yield return request.SendWebRequest();

        if (request.responseCode == 200)
        {
            //get hash of result
            using (MD5 hasher = MD5.Create())
            {
                //Debug.Log(request.downloadHandler.text);
                if(request.downloadHandler.text.Equals("End Session")){
                    result("End Session", null);
                }
                byte[] newHashBytes = hasher.ComputeHash(request.downloadHandler.data);
                var sBuilder = new StringBuilder();
                foreach (byte b in newHashBytes) sBuilder.Append(b.ToString("x2"));
                string newHash = sBuilder.ToString();

                result(newHash, (JSONObject)JSONHandler.DecodeJsonRequest(request.downloadHandler.text));
            }
        } 
        else if (request.responseCode == 408)
        {
            result(null, null);
        }
    }

    public static IEnumerator SaveGameServer(string gameid, FadeOut successText, FadeOut failText)
    {
        string url = "http://" + HOST + ":4244/splendor/api/action/" + gameid + "/save"; //url for POST request
        UnityWebRequest request = UnityWebRequest.Post(url, "body"); //body of POST cannot be empty
        yield return request.SendWebRequest();
        
        //TO BE WARNED IF THE REQUEST WAS NOT SUCCESSFUL, UNCOMMENT THE FOLLOWING LINES
        //UnityEngine.Debug.Log("Server save fail");
        if (request.responseCode == 200) {
            successText.ResetFade(false);
        }
        else {
            failText.ResetFade(false);
        }
    }

     public static IEnumerator DeleteGameServer(string gameid)
    {
        string url = "http://" + HOST + ":4244/splendor/api/action/" + gameid + "/delete"; //url for POST request
        UnityWebRequest request = UnityWebRequest.Delete(url); //body of POST cannot be empty
        yield return request.SendWebRequest();
        
        //TO BE WARNED IF THE REQUEST WAS NOT SUCCESSFUL, UNCOMMENT THE FOLLOWING LINES
        //UnityEngine.Debug.Log("Server save fail");
    }
}
