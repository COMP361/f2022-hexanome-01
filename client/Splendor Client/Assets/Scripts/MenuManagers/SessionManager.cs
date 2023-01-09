using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Networking;

public class SessionManager : MonoBehaviour
{
    /**
     * Allows GET request to get the list of all sessions available to join in the LobbyService.
     */
    public void getAvailableSessionsStart() {
        StartCoroutine(getAvailableSessions());
    }

    /**
     * Gets a list of all sessions currently stored in the LobbyService.
     */
    public IEnumerator getAvailableSessions() {
        string url = "http://127.0.0.1:4242/api/sessions"; //url for GET request
        UnityWebRequest request = UnityWebRequest.Get(url);
        yield return request.SendWebRequest();

        if (request.result == UnityWebRequest.Result.Success)
        {
            determineAvailable(FileManager.DecodeSessionListData(request.downloadHandler.text, false));
        }
    }

    /**
     * Determines if a session from a given list of sessions is available to join and displays it if so.
     */
    public void determineAvailable(SessionListData sessions) {
        List<SessionData> availableSessions = new List<SessionData>();

        if (sessions != null)
        {
            for (int i = 0; i < sessions.sessionList.Length; i++)
            {
                SessionData session = sessions.sessionList[i];
                if (session.launched == true) continue; //a launched session is not available
                else if (session.players.Length == session.maxSessionPlayers) continue; //a full session is not available
                else availableSessions.Add(session);
            }

            MainMenuManager mmm = new MainMenuManager();
            mmm.MakeSessions(availableSessions.ToArray()); //displays the available sessions
        }
    }
}
