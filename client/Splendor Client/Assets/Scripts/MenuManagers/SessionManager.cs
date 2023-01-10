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
    public InputField sessionNameField;
    public Authentication mainPlayer;
    public Toggle splendorToggle, citiesToggle, tradingPostsToggle;
    public GameObject createFail;

    //******************************** JOIN SESSION ********************************

    /// <summary>
    /// Allows GET request to get the list of all sessions available to join in the LobbyService.
    /// </summary>
    public void getAvailableSessionsStart() {
        StartCoroutine(getAvailableSessions());
    }

    /// <summary>
    /// Gets a list of all sessions currently stored in the LobbyService,
    /// to give to MainMenuManager's determineAvailable method.
    /// </summary>
    /// <returns>Allows GET request</returns>
    public IEnumerator getAvailableSessions() {
        string url = "http://127.0.0.1:4242/api/sessions"; //url for GET request
        UnityWebRequest request = UnityWebRequest.Get(url);
        yield return request.SendWebRequest();

        if (request.result == UnityWebRequest.Result.Success)
        {
            MainMenuManager mmm = GetComponent<MainMenuManager>();
            mmm.determineAvailable(FileManager.DecodeSessionListData(request.downloadHandler.text, false));
        }
    }

    //******************************** CREATE SESSION ********************************

    /// <summary>
    /// Allows POST request to create a session in the LobbyService and get its id.
    /// </summary>
    public void createSessionStart()
    {
        StartCoroutine(createSession());
    }

    /// <summary>
    /// Creates a session in the LobbyService and gets back its id,
    /// to give to getSessionStart method.
    /// </summary>
    /// <returns>Allows POST request</returns>
    public IEnumerator createSession() {
        string sessionName = sessionNameField.text; //keep the submitted session name even if it is changed after create button clicked
        string game = ""; //determine game version based on selected toggle
        if (splendorToggle.isOn) game = "splendor";
        else if (citiesToggle.isOn) game = "cities";
        else if (tradingPostsToggle.isOn) game = "tradingposts";

        if (sessionName == "") createFail.SetActive(true);
        else
        {
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
                getSessionStart(create.downloadHandler.text);
            }
        }
    }

    /// <summary>
    /// Allows GET request to get one session's information in the LobbyService.
    /// </summary>
    public void getSessionStart(string id)
    {
        StartCoroutine(getSession(id));
    }

    /// <summary>
    /// Gets one session's information in the LobbyService,
    /// to give to MainMenuManager's CreateSession method along with the session's id.
    /// </summary>
    /// <param name="id">String representing the id of the session whose information is being retrieved</param>
    /// <returns>Allows GET request</returns>
    public IEnumerator getSession(string id)
    {
        string url = "http://127.0.0.1:4242/api/sessions/" + id; //url for GET request
        UnityWebRequest request = UnityWebRequest.Get(url);
        yield return request.SendWebRequest();

        if (request.result == UnityWebRequest.Result.Success)
        {
            Session session = FileManager.DecodeSession(request.downloadHandler.text, false);
            session.id = id;
            MainMenuManager mmm = GetComponent<MainMenuManager>();
            mmm.CreateSession(session);
        }
    }
}
