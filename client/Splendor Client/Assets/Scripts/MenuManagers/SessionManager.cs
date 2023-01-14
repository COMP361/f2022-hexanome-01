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
    private string HOST = "127.0.0.1";
    //for create session
    public Authentication mainPlayer;
    public Toggle splendorToggle, citiesToggle, tradingPostsToggle;

    //******************************** JOIN SESSION ********************************

    /// <summary>
    /// Allows GET request to get the list of all sessions available to join in the LobbyService.
    /// </summary>
    public void GetAvailableSessionsStart() {
        StartCoroutine(GetAvailableSessions());
    }

    /// <summary>
    /// Gets a list of all sessions currently stored in the LobbyService,
    /// to give to MainMenuManager's determineAvailable method.
    ///
    /// Explaination of JSONObject representing sessions:
    /// object is a Dictionary<string, IDictionary1> mapping "sessions" => IDictionary1<string, IDictionary2>
    /// Idictionary1 maps "idNumber" => Idictionary2<string, string>
    /// IDictionary2 maps "parameters" => "value" (i.e. "creator" => "maex")
    /// some values in Idictionary2 (players and gameParameters specifically) are themselves
    /// a string representing a JSONArray or JSONObject and must be decoded again
    /// 
    /// </summary>
    /// <returns>Allows GET request</returns>
    public IEnumerator GetAvailableSessions() {
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
                
                MainMenuManager mmm = GetComponent<MainMenuManager>();
                mmm.determineAvailable(sessions);
            }
        }
    }

    //******************************** CREATE SESSION ********************************

    /// <summary>
    /// Allows POST request to create a session in the LobbyService and get its id.
    /// </summary>
    public void CreateSessionStart()
    {
        StartCoroutine(CreateSession());
    }

    /// <summary>
    /// Creates a session in the LobbyService and gets back its id,
    /// to give to getSessionStart method.
    /// </summary>
    /// <returns>Allows POST request</returns>
    public IEnumerator CreateSession() {
        string variant = ""; //determine game version based on selected toggle
        if (splendorToggle.isOn) variant = "splendor";
        else if (citiesToggle.isOn) variant = "cities";
        else if (tradingPostsToggle.isOn) variant = "tradingposts";

        string url = "http://" + HOST + ":4242/api/sessions"; //url for POST request
        WWWForm form = new WWWForm();
        form.AddField("creator", mainPlayer.username);
        form.AddField("game", "splendor");
        form.AddField("savegame", "");
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
        {
            GetSessionStart(create.downloadHandler.text, variant);
        }
    
    }

    /// <summary>
    /// Allows GET request to get one session's information in the LobbyService.
    /// </summary>
    public void GetSessionStart(string id, string variant)
    {
        StartCoroutine(GetSession(id, variant));
    }

    /// <summary>
    /// Gets one session's information in the LobbyService,
    /// to give to MainMenuManager's CreateSession method along with the session's id.
    /// </summary>
    /// <param name="id">String representing the id of the session whose information is being retrieved</param>
    /// <param name="variant">String representing the variant of the session whose information is being retrieved</param>
    /// <returns>Allows GET request</returns>
    public IEnumerator GetSession(string id, string variant)
    {
        string url = "http://" + HOST + ":4242/api/sessions/" + id; //url for GET request
        UnityWebRequest request = UnityWebRequest.Get(url);
        yield return request.SendWebRequest();

        if (request.result == UnityWebRequest.Result.Success)
        {
            JSONObject json = (JSONObject) JSONHandler.DecodeJsonRequest(request.downloadHandler.text);
            Session session = new Session(id, json.dictionary);
            session.SetVariant(variant);

            MainMenuManager mmm = GetComponent<MainMenuManager>();
            mmm.CreateSession(session);
        }
    }
}
