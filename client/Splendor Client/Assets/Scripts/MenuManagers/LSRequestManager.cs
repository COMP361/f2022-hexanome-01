using System;
using System.Text;
using System.Collections;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Diagnostics;
using System.Security.Cryptography;
using UnityEditor;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.UI;

/// <summary>
/// Sends web requests to the LobbyService to allow LobbyService requests in the main menu.
/// </summary>
public class LSRequestManager : MonoBehaviour
{
    private static string HOST = Environment.GetEnvironmentVariable("SPLENDOR_HOST_IP");
    private static Authentication mainPlayer;

    //******************************** LOG IN ********************************
    public static IEnumerator Login(Authentication authentication, string username, string password, Action<bool> result)
    {
        mainPlayer = authentication;

        string url = "http://" + HOST + ":4242/oauth/token"; //url for POST request

        WWWForm form = new WWWForm();
        form.AddField("grant_type", "password");
        form.AddField("username", username);
        form.AddField("password", password);

        UnityWebRequest auth = UnityWebRequest.Post(url, form);
        auth.SetRequestHeader("Authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc="); //authenticate the POST request

        yield return auth.SendWebRequest();

        if (auth.result == UnityWebRequest.Result.Success)
        {
            JSONObject json = (JSONObject)JSONHandler.DecodeJsonRequest(auth.downloadHandler.text);
            CallRefresh((long)json["expires_in"] - 5); //refresh automatically when token is about to expire

            //set authentication fields
            mainPlayer.SetUsername(username);
            mainPlayer.SetAccessToken((string)json["access_token"]);
            mainPlayer.SetRefreshToken((string)json["refresh_token"]);

            result(true);
        }
        else
            result(false);

    }

    private static IEnumerator CallRefresh(long seconds) {
        yield return new WaitForSeconds(seconds);
        Refresh();
    }

    public static IEnumerator Refresh()
    {
        string url = "http://" + HOST + ":4242/oauth/token"; //url for POST request

        WWWForm form = new WWWForm();
        form.AddField("grant_type", "refresh_token");
        form.AddField("refresh_token", mainPlayer.GetRefreshToken());

        UnityWebRequest auth = UnityWebRequest.Post(url, form);
        auth.SetRequestHeader("Authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc="); //authenticate the POST request

        yield return auth.SendWebRequest();

        if (auth.result == UnityWebRequest.Result.Success)
        {
            JSONObject json = (JSONObject)JSONHandler.DecodeJsonRequest(auth.downloadHandler.text);
            CallRefresh((long)json["expires_in"] - 5); //refresh automatically when token is about to expire

            //set authentication fields
            mainPlayer.SetAccessToken((string)json["access_token"]);
            mainPlayer.SetRefreshToken((string)json["refresh_token"]);
        }
    }


    //******************************** JOIN SESSION ********************************

    /// <summary>
    /// Gets a list of all sessions currently stored in the LobbyService.
    ///
    /// Explanation of JSONObject representing sessions:
    /// object is a Dictionary<string, IDictionary1> mapping "sessions" => IDictionary1<string, IDictionary2>
    /// Idictionary1 maps "idNumber" => Idictionary2<string, string>
    /// IDictionary2 maps "parameters" => "value" (i.e. "creator" => "maex")
    /// some values in Idictionary2 (players and gameParameters specifically) are themselves
    /// a string representing a JSONArray or JSONObject and must be decoded again
    /// 
    /// </summary>
    /// <param name="HOST">IP address to send the request to</param>
    /// <param name="result">method that will receive the list of sessions as a parameter</param>
    /// <returns>Allows GET request</returns>
    public static IEnumerator GetSessions(string hash, Action<string, List<Session>> result)
    {
        string url = "http://" + HOST + ":4242/api/sessions";
        if (hash != null) url += ("?hash=" + hash); //url for GET request
        UnityWebRequest request = UnityWebRequest.Get(url);
        yield return request.SendWebRequest();

        if (request.responseCode == 200)
        {
            //get hash of result
            byte[] newHashBytes = MD5.Create().ComputeHash(request.downloadHandler.data);
            var sBuilder = new StringBuilder();
            foreach (byte b in newHashBytes) sBuilder.Append(b.ToString("x2"));
            string newHash = sBuilder.ToString();

            JSONObject json = (JSONObject)JSONHandler.DecodeJsonRequest(request.downloadHandler.text);
            List<Session> sessions = new List<Session>();
            if (!json.Equals("{\"sessions\":{}}"))
            {
                foreach (DictionaryEntry de in (IDictionary)json["sessions"])
                    sessions.Add(new Session(de.Key.ToString(), (IDictionary)de.Value));
            }
            result(newHash, sessions); //return session list
        }
        else if (request.responseCode == 408) result(null, null);
    }

    /// <summary>
    /// Adds a player to the session in the LobbyService.
    /// </summary>
    /// <param name="HOST">IP address to send the request to</param>
    /// <returns>Allows PUT request</returns>
    public static IEnumerator Join(ActiveSession session)
    {
        string url = "http://" + HOST + ":4242/api/sessions/" + session.id + "/players/" + mainPlayer.GetUsername(); //url for PUT request
        UnityWebRequest add = UnityWebRequest.Put(url, "body"); //body of PUT cannot be empty
        add.SetRequestHeader("Authorization", "Bearer " + mainPlayer.GetAccessToken());

        yield return add.SendWebRequest();

        //TO BE WARNED IF THE REQUEST WAS NOT SUCCESSFUL, UNCOMMENT THE FOLLOWING LINES
        //if (add.result != UnityWebRequest.Result.Success)
        //{
        //    UnityEngine.Debug.Log("ERROR: PLAYER NOT ADDED TO SESSION");
        //}
    }

    //******************************** CREATE SESSION ********************************

    /// <summary>
    /// Creates a session in the LobbyService and gets back its id,
    /// to give to getSessionStart method.
    /// </summary>
    /// <param name="HOST">IP address to send the request to</param>
    /// <param name="result">method that will receive the created session id as a parameter</param>
    /// <returns>Allows POST request</returns>
    public static IEnumerator CreateSession(string variant, Action<string> result)
    {

        string url = "http://" + HOST + ":4242/api/sessions"; //url for POST request
        //LobbyService requires UTF8 encoded json NOT UnityWebRequest's default of URL encoded
        UnityWebRequest create = new UnityWebRequest(url);
        create.method = "POST";
        create.SetRequestHeader("Authorization", "Bearer " + mainPlayer.GetAccessToken());
        create.SetRequestHeader("Content-Type", "application/json");
        string body = "{\"game\":\"" + variant + "\", \"creator\":\"" + mainPlayer.GetUsername() + "\", \"savegame\":\"\"}";
        create.uploadHandler = new UploadHandlerRaw(Encoding.UTF8.GetBytes(body));
        create.downloadHandler = new DownloadHandlerBuffer();

        yield return create.SendWebRequest();

        if (create.result == UnityWebRequest.Result.Success)
            result(create.downloadHandler.text);
    }

    /// <summary>
    /// Gets one session's information in the LobbyService,
    /// to give to MainMenuManager's CreateSession method along with the session's id.
    /// </summary>
    /// <param name="id">String representing the id of the session whose information is being retrieved</param>
    /// <param name="variant">String representing the variant of the session whose information is being retrieved</param>
    /// <returns>Allows GET request</returns>
    public static IEnumerator GetSession(string id, string hash, Action<string, Session> result)
    {
        string url = "http://" + HOST + ":4242/api/sessions/" + id; //url for GET request
        if (hash != null) url += ("?hash=" + hash); //url for GET request
        UnityWebRequest request = UnityWebRequest.Get(url);
        yield return request.SendWebRequest();

        if (request.responseCode == 200) {
            //get hash of result
            using (MD5 hasher = MD5.Create())
            {
                byte[] newHashBytes = hasher.ComputeHash(request.downloadHandler.data);
                var sBuilder = new StringBuilder();
                foreach (byte b in newHashBytes) sBuilder.Append(b.ToString("x2"));
                string newHash = sBuilder.ToString();

                JSONObject json = (JSONObject)JSONHandler.DecodeJsonRequest(request.downloadHandler.text);
                Session session = new Session(id, json.dictionary);

                result(newHash, session);
            }
        } 
        else if (request.responseCode == 408) result(hash, null);
        else result(null, null);
    }

    /// <summary>
    /// Deletes a session from the LobbyService.
    /// </summary>
    /// <param name="HOST">IP of LobbyService</param>
    /// <param name="id">session id</param>
    /// <param name="mainPlayer">must be an admin or the creator of the session</param>
    /// <returns>allows Delete request</returns>
    public static IEnumerator DeleteSession(string id)
    {
        string url = "http://" + HOST + ":4242/api/sessions/" + id; //url for DELETE request
        UnityWebRequest delete = UnityWebRequest.Delete(url);
        delete.SetRequestHeader("Authorization", "Bearer " + mainPlayer.GetAccessToken());

        yield return delete.SendWebRequest();

        //TO BE WARNED IF THE REQUEST WAS NOT SUCCESSFUL, UNCOMMENT THE FOLLOWING LINES
        //if (delete.result != UnityWebRequest.Result.Success)
        //{
        //    UnityEngine.Debug.Log("ERROR (" + delete.result + "): SESSION NOT DELETED");
        //}
    }

    //******************************** LOAD SAVE ********************************


    /// <summary>
    /// Gets a list of all saved games stored in the LobbyService.
    /// </summary>
    /// <param name="HOST">IP address to send the request to</param>
    /// <param name="result">method that will receive the list of saved games as a parameter</param>
    /// <returns></returns>
    public static IEnumerator GetSaves(string variant, Action<List<Save>> result)
    {
        string url = "http://" + HOST + ":4242/api/gameservices/" + variant + "/savegames"; //url for GET request
        UnityWebRequest request = UnityWebRequest.Get(url);
        request.SetRequestHeader("Authorization", "Bearer " + mainPlayer.GetAccessToken());
        yield return request.SendWebRequest();

        if (request.result == UnityWebRequest.Result.Success)
        {
            JSONArray json = (JSONArray)JSONHandler.DecodeJsonRequest(request.downloadHandler.text);
            List<Save> saves = new List<Save>();
            if (json.Count > 0)
            {
                foreach (JSONObject save in json)
                    saves.Add(new Save(save));
            }
            result(saves); //return save list
        } else result(null);
    }

    /// <summary>
    /// Creates a session in the LobbyService from a saved game and gets back its id,
    /// to give to getSessionStart method.
    /// </summary>
    /// <returns>Allows POST request</returns>
    public static IEnumerator CreateSavedSession(Save save, Action<string> result)
    {
        string url = "http://" + HOST + ":4242/api/sessions"; //url for POST request
        //LobbyService requires UTF8 encoded json NOT UnityWebRequest's default of URL encoded
        UnityWebRequest create = new UnityWebRequest(url);
        create.method = "POST";
        create.SetRequestHeader("Authorization", "Bearer " + mainPlayer.GetAccessToken());
        create.SetRequestHeader("Content-Type", "application/json");
        string body = "{\"game\":\"" + save.gamename + "\", \"creator\":\"" + mainPlayer.GetUsername() + "\", \"savegame\":\"" + save.savegameid + "\"}";
        create.uploadHandler = new UploadHandlerRaw(Encoding.UTF8.GetBytes(body));
        create.downloadHandler = new DownloadHandlerBuffer();

        yield return create.SendWebRequest();

        if (create.result == UnityWebRequest.Result.Success) result(create.downloadHandler.text);
    }

    //******************************** LOBBY ********************************

    /// <summary>
    /// Removes a player from a session in the LobbyService.
    /// </summary>
    /// <returns>Allows DELETE request</returns>
    public static IEnumerator Leave(ActiveSession session)
    {
        string url = "http://" + HOST + ":4242/api/sessions/" + session.id + "/players/" + mainPlayer.GetUsername(); //url for DELETE request
        UnityWebRequest remove = UnityWebRequest.Delete(url);
        remove.SetRequestHeader("Authorization", "Bearer " + mainPlayer.GetAccessToken());

        yield return remove.SendWebRequest();

        

        //TO BE WARNED IF THE REQUEST WAS NOT SUCCESSFUL, UNCOMMENT THE FOLLOWING LINES
        //if (remove.result != UnityWebRequest.Result.Success)
        //{
        //    UnityEngine.Debug.Log(remove.result);
        //    UnityEngine.Debug.Log("ERROR: PLAYER NOT REMOVED FROM SESSION");
        //}
    }

    public static IEnumerator Launch(ActiveSession session, Action<bool> result)
    {
        string url = "http://" + HOST + ":4242/api/sessions/" + session.id; //url for POST request
        UnityWebRequest launch = UnityWebRequest.Post(url, "body"); //body of POST cannot be empty
        launch.SetRequestHeader("Authorization", "Bearer " + mainPlayer.GetAccessToken());

        yield return launch.SendWebRequest();

        result(launch.result == UnityWebRequest.Result.Success);
    }

	//******************************** ADMIN ********************************

	public static IEnumerator AddUser(Authentication mainPlayer, string username, string password, 
			string role, string colour, Action<bool> result) {
		string url = "http://" + HOST + ":4242/api/users/" + username;
		UnityWebRequest add = new UnityWebRequest(url);
		add.method = "PUT";
		add.SetRequestHeader("Authorization", "Bearer " + mainPlayer.GetAccessToken());
		add.SetRequestHeader("Content-Type", "application/json");
		string body = "{\"name\":\"" + username 
			+ "\", \"password\":\"" + password 
			+ "\", \"preferredColour\":\"" + colour 
			+ "\", \"role\":\"" + role + "\"}";
		add.uploadHandler = new UploadHandlerRaw(Encoding.UTF8.GetBytes(body));
		add.downloadHandler = new DownloadHandlerBuffer();

		yield return add.SendWebRequest();

        UnityEngine.Debug.Log(add.downloadHandler.text);

		result(add.result == UnityWebRequest.Result.Success);
 	}

	public static IEnumerator GetRole(Action<string> result) {
		string url = "http://" + HOST + ":4242/oauth/role";
		UnityWebRequest get = UnityWebRequest.Get(url);
		get.SetRequestHeader("Authorization", "Bearer " + mainPlayer.GetAccessToken());

		yield return get.SendWebRequest();

		if (get.result == UnityWebRequest.Result.Success) {
			result(get.downloadHandler.text);
		} else result(null);
	}
}
