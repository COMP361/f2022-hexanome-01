using System;
using System.Text;
using System.Collections;
using System.Collections.Generic;
using System.Diagnostics;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.UI;

/// <summary>
/// Interacts with LobbyService to allow session management in the main menu.
/// </summary>
public class SessionManager : MonoBehaviour
{
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
    /// </summary>
    /// <returns>Allows GET request</returns>
    public IEnumerator GetAvailableSessions() {
        string url = "http://127.0.0.1:4242/api/sessions"; //url for GET request
        UnityWebRequest request = UnityWebRequest.Get(url);
        yield return request.SendWebRequest();

        if (request.result == UnityWebRequest.Result.Success)
        {
            string json = request.downloadHandler.text; // formatting to match a list of SessionData
            if (!json.Equals("{\"sessions\":{}}"))
            {
                json = json.Replace("{\"sessions\":{", "").Replace("}}}}", "}}").Replace(",\"playerLocations\":{}", "");
                json = json.Replace("{\"gameParameters\":", "").Replace("},\"creator", ",\"creator");
                json = "\"id\":" + json.Replace(":{", ",").Replace("},", "},\"id\":") + ",";
                string[] jsons = json.Replace("},", "}").Split('}');

                List<Session> sessions = new List<Session>();
                foreach (string session in jsons)
                {
                    sessions.Add(FileManager.DecodeSession("{" + session + "}", false));
                }

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
        string game = ""; //determine game version based on selected toggle
        if (splendorToggle.isOn) game = "splendor";
        else if (citiesToggle.isOn) game = "cities";
        else if (tradingPostsToggle.isOn) game = "tradingposts";

        string url = "http://127.0.0.1:4242/api/sessions"; //url for POST request
        WWWForm form = new WWWForm();
        form.AddField("creator", mainPlayer.username);
        form.AddField("game", game);
        form.AddField("savegame", "");
        //LobbyService requires UTF8 encoded json NOT UnityWebRequest's default of URL encoded
        UnityWebRequest create = new UnityWebRequest(url);
        create.method = "POST";
        create.SetRequestHeader("Authorization", "Bearer " + mainPlayer.access_token);
        create.SetRequestHeader("Content-Type", "application/json");
        string body = "{\"game\":\"" + game + "\", \"creator\":\"" + mainPlayer.username + "\", \"savegame\":\"\"}";
        create.uploadHandler = new UploadHandlerRaw(Encoding.UTF8.GetBytes(body));
        create.downloadHandler = new DownloadHandlerBuffer();

        yield return create.SendWebRequest();

        if (create.result == UnityWebRequest.Result.Success)
        {
            GetSessionStart(create.downloadHandler.text);
        }
    
    }

    /// <summary>
    /// Allows GET request to get one session's information in the LobbyService.
    /// </summary>
    public void GetSessionStart(string id)
    {
        StartCoroutine(GetSession(id));
    }

    /// <summary>
    /// Gets one session's information in the LobbyService,
    /// to give to MainMenuManager's CreateSession method along with the session's id.
    /// </summary>
    /// <param name="id">String representing the id of the session whose information is being retrieved</param>
    /// <returns>Allows GET request</returns>
    public IEnumerator GetSession(string id)
    {
        string url = "http://127.0.0.1:4242/api/sessions/" + id; //url for GET request
        UnityWebRequest request = UnityWebRequest.Get(url);
        yield return request.SendWebRequest();

        if (request.result == UnityWebRequest.Result.Success)
        {
            string json = request.downloadHandler.text;
            json = json.Replace("{\"gameParameters\":", "").Replace("},\"creator", ",\"creator");
            Session session = FileManager.DecodeSession(json, false);
            session.id = id;

            MainMenuManager mmm = GetComponent<MainMenuManager>();
            mmm.CreateSession(session);
        }
    }
}
