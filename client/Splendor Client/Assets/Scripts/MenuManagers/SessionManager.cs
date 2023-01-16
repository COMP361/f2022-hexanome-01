using System;
using System.Text;
using System.Collections;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Diagnostics;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.UI;

/// <summary>
/// Interacts with LobbyService to allow session management in the main menu.
/// </summary>
public class SessionManager : MonoBehaviour
{
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
    public static IEnumerator GetSessions(string HOST, Action<List<Session>> result) {
        string url = "http://" + HOST + ":4242/api/sessions"; //url for GET request
        UnityWebRequest request = UnityWebRequest.Get(url);
        yield return request.SendWebRequest();

        if (request.result == UnityWebRequest.Result.Success)
        {
            //string json = request.downloadHandler.text; // formatting to match a list of SessionData
            JSONObject json = (JSONObject) JSONHandler.DecodeJsonRequest(request.downloadHandler.text);
            if (!json.Equals("{\"sessions\":{}}")) {
                List<Session> sessions = new List<Session>();
                foreach (DictionaryEntry de in (IDictionary)json["sessions"]) 
                    sessions.Add(new Session(de.Key.ToString(), (IDictionary)de.Value));

                result(sessions); //to imitate returning the session list
            }
        }
    }

    /// <summary>
    /// Adds a player to the session in the LobbyService.
    /// </summary>
    /// <param name="HOST">IP address to send the request to</param>
    /// <returns>Allows PUT request</returns>
    public static IEnumerator Join(string HOST, Authentication mainPlayer, Session session)
    {
        string url = "http://" + HOST + ":4242/api/sessions/" + session.id + "/players/" + mainPlayer.username; //url for PUT request
        UnityWebRequest add = UnityWebRequest.Put(url, "body");
        add.SetRequestHeader("Authorization", "Bearer " + mainPlayer.access_token);

        yield return add.SendWebRequest();

        if (add.result != UnityWebRequest.Result.Success)
        {
            UnityEngine.Debug.Log(add.result);
            UnityEngine.Debug.Log("ERROR: PLAYER NOT ADDED TO SESSION");
        }
    }

    /*
    /// <summary>
    /// Allows DELETE request to remove a player from a session in the LobbyService.
    /// </summary>
    public void LeaveStart()
    {
        StartCoroutine(Leave());
    }

    /// <summary>
    /// Removes a player from a session in the LobbyService.
    /// </summary>
    /// <returns>Allows DELETE request</returns>
    public IEnumerator Leave()
    {
        Session session = mmm.currentSession;
        string url = "http://" + HOST + ":4242/api/sessions/" + session.id + "/players/" + mainPlayer.username; //url for DELETE request
        UnityWebRequest remove = UnityWebRequest.Delete(url);
        remove.SetRequestHeader("Authorization", "Bearer " + mainPlayer.access_token);

        yield return remove.SendWebRequest();

        if (remove.result != UnityWebRequest.Result.Success)
        {
            UnityEngine.Debug.Log("ERROR: PLAYER NOT REMOVED FROM SESSION");
        }
    }
    */

    //******************************** CREATE SESSION ********************************

    /// <summary>
    /// Creates a session in the LobbyService and gets back its id,
    /// to give to getSessionStart method.
    /// </summary>
    /// <param name="HOST">IP address to send the request to</param>
    /// <param name="result">method that will receive the created session id as a parameter</param>
    /// <returns>Allows POST request</returns>
    public static IEnumerator CreateSession(string HOST, Authentication mainPlayer, Action<string> result) {

        string url = "http://" + HOST + ":4242/api/sessions"; //url for POST request
        //LobbyService requires UTF8 encoded json NOT UnityWebRequest's default of URL encoded
        UnityWebRequest create = new UnityWebRequest(url);
        create.method = "POST";
        create.SetRequestHeader("Authorization", "Bearer " + mainPlayer.access_token);
        create.SetRequestHeader("Content-Type", "application/json");
        string body = "{\"game\":\"splendor\", \"creator\":\"" + mainPlayer.username + "\", \"savegame\":\"\"}";
        create.uploadHandler = new UploadHandlerRaw(Encoding.UTF8.GetBytes(body));
        create.downloadHandler = new DownloadHandlerBuffer();

        yield return create.SendWebRequest();

        if (create.result == UnityWebRequest.Result.Success)
            result(create.downloadHandler.text);
            //GetSessionStart(create.downloadHandler.text, variant, false);
    }

    /// <summary>
    /// Gets one session's information in the LobbyService,
    /// to give to MainMenuManager's CreateSession method along with the session's id.
    /// </summary>
    /// <param name="id">String representing the id of the session whose information is being retrieved</param>
    /// <param name="variant">String representing the variant of the session whose information is being retrieved</param>
    /// <returns>Allows GET request</returns>
    public static IEnumerator GetSession(string HOST, string id, string variant, Action<Session> result)
    {
        string url = "http://" + HOST + ":4242/api/sessions/" + id; //url for GET request
        UnityWebRequest request = UnityWebRequest.Get(url);
        yield return request.SendWebRequest();

        if (request.result == UnityWebRequest.Result.Success)
        {

            JSONObject json = (JSONObject) JSONHandler.DecodeJsonRequest(request.downloadHandler.text);
            Session session = new Session(id, json.dictionary);
            session.SetVariant(variant);

            result(session);
        }
    }

    //******************************** LOAD SAVE ********************************

    
    /// <summary>
    /// Gets a list of all saved games stored in the LobbyService.
    /// </summary>
    /// <param name="HOST">IP address to send the request to</param>
    /// <param name="result">method that will receive the list of saved games as a parameter</param>
    /// <returns></returns>
    public static IEnumerator GetSaves(string HOST, Authentication mainPlayer, Action<List<Save>> result)
    {
        string url = "http://" + HOST + ":4242/api/gameservices/splendor/savegames"; //url for GET request
        UnityWebRequest request = UnityWebRequest.Get(url);
        request.SetRequestHeader("Authorization", "Bearer " + mainPlayer.access_token);
        yield return request.SendWebRequest();

        if (request.result == UnityWebRequest.Result.Success)
        {
            JSONArray json = (JSONArray)JSONHandler.DecodeJsonRequest(request.downloadHandler.text);
            IEnumerator enumerator = json.GetEnumerator();
            List<Save> allSaves = new List<Save>();
            while (enumerator.MoveNext())
            {
                allSaves.Add(new Save((JSONObject)enumerator.Current));
            }

            result(allSaves);
        }
    }

    /*
    /// <summary>
    /// Allows PUT request to create a savegame in the LobbyService.
    /// </summary>
    public void CreateSaveStart()
    {
        StartCoroutine(CreateSave());
    }

    /// <summary>
    /// Creates a savegame in the LobbyService.
    /// </summary>
    /// <returns>Allows PUT request</returns>
    public IEnumerator CreateSave()
    {
        Session session = mmm.currentSession;
        string savegameid = session.creator + "0" + session.variant; //must be changed eventually when we have a better system for creating savegameids
        string url = "http://" + HOST + ":4242/api/gameservices/splendor/savegames/" + savegameid ; //url for PUT request
        //LobbyService requires UTF8 encoded json NOT UnityWebRequest's default of URL encoded
        UnityWebRequest create = new UnityWebRequest(url);
        create.method = "PUT";
        create.SetRequestHeader("Authorization", "Bearer " + mainPlayer.access_token);
        create.SetRequestHeader("Content-Type", "application/json");
        string body = "{\"gamename\":\"splendor\", \"players\":" + session.PlayersToJSONString() + ", \"savegameid\":\"" + savegameid + "\"}";
        create.uploadHandler = new UploadHandlerRaw(Encoding.UTF8.GetBytes(body));
        create.downloadHandler = new DownloadHandlerBuffer();

        yield return create.SendWebRequest();

        if (create.result != UnityWebRequest.Result.Success)
        {
            UnityEngine.Debug.Log("ERROR: SAVE NOT CREATED");
        }
    }

    /// <summary>
    /// Allows POST request to create a session in the LobbyService from a saved game and get its id.
    /// </summary>
    public void CreateSavedSessionStart()
    {
        StartCoroutine(CreateSavedSession());
    }

    /// <summary>
    /// Creates a session in the LobbyService from a saved game and gets back its id,
    /// to give to getSessionStart method.
    /// </summary>
    /// <returns>Allows POST request</returns>
    public IEnumerator CreateSavedSession()
    {
        Save save = mmm.currentSave;

        if (save != null)
        {
            string variant = ""; //determine game version based on selected toggle
            if (splendorToggle.isOn) variant = "splendor";
            else if (citiesToggle.isOn) variant = "cities";
            else if (tradingPostsToggle.isOn) variant = "tradingposts";

            string url = "http://" + HOST + ":4242/api/sessions"; //url for POST request
                                                                  //LobbyService requires UTF8 encoded json NOT UnityWebRequest's default of URL encoded
            UnityWebRequest create = new UnityWebRequest(url);
            create.method = "POST";
            create.SetRequestHeader("Authorization", "Bearer " + mainPlayer.access_token);
            create.SetRequestHeader("Content-Type", "application/json");
            string body = "{\"game\":\"splendor\", \"creator\":\"" + mainPlayer.username + "\", \"savegame\":\"" + save.savegameid + "\"}";
            create.uploadHandler = new UploadHandlerRaw(Encoding.UTF8.GetBytes(body));
            create.downloadHandler = new DownloadHandlerBuffer();

            yield return create.SendWebRequest();

            if (create.result == UnityWebRequest.Result.Success)
            {
                GetSessionStart(create.downloadHandler.text, variant, true);
            }
        }
        else {
            UnityEngine.Debug.Log("No selected saved game");
        }
    }
    */
}
