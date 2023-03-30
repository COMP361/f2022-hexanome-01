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

    public static IEnumerator SaveGameLS(string gamename, String accessToken, List<string> players, string savegameid)
    {
        //set up PUT request body
        JSONObject body = new JSONObject(new Dictionary<string, object>());
        body.Add("gamename", gamename);
        
        JSONArray playersJson = new JSONArray();
        foreach (string player in players)
            playersJson.Add(player);
        body.Add("players", playersJson);

        body.Add("savegameid", savegameid);

        string url = "http://" + HOST + ":4242/api/gameservices/" + gamename + "/savegames/" + savegameid; //url for PUT request
        UnityWebRequest add = new UnityWebRequest(url);
        add.method = "PUT";
        add.SetRequestHeader("Authorization", "Bearer " + accessToken);
        add.SetRequestHeader("Content-Type", "application/json");
        add.uploadHandler = new UploadHandlerRaw(Encoding.UTF8.GetBytes(body.ToJSONString()));
        add.downloadHandler = new DownloadHandlerBuffer();
        yield return add.SendWebRequest();

        //TO BE WARNED IF THE REQUEST WAS NOT SUCCESSFUL, UNCOMMENT THE FOLLOWING LINES
        if (add.result != UnityWebRequest.Result.Success)
        {
            UnityEngine.Debug.Log("ERROR: GAME NOT SAVED");
            UnityEngine.Debug.Log(add.result);
        }
        else
        {
            UnityEngine.Debug.Log("LS save success");
        }
    }

    public static IEnumerator SaveGameServer(string gameid, Action<string> result)
    {
        string url = "http://" + HOST + ":4244/splendor/api/action/" + gameid + "/save"; //url for POST request
        UnityWebRequest request = UnityWebRequest.Post(url, "body"); //body of POST cannot be empty
        yield return request.SendWebRequest();

        if (request.result == UnityWebRequest.Result.Success)
        {
            JSONObject json = (JSONObject)JSONHandler.DecodeJsonRequest(request.downloadHandler.text);
            UnityEngine.Debug.Log("Server save: " + (string)json["status"]);
            if (((string)json["status"]).Equals("success"))
                result((string)json["savegameid"]);
            else
                result(null);
        }
        else
        {
            //TO BE WARNED IF THE REQUEST WAS NOT SUCCESSFUL, UNCOMMENT THE FOLLOWING LINES
            UnityEngine.Debug.Log("Server save fail");
            result(null);
        }
    }
}
