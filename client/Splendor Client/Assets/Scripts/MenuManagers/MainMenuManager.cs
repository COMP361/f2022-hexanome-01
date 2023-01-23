using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;
using UnityEngine.Events;
using UnityEngine.Networking;
using System.Linq;

public enum LastMenuVisited {
    MAIN,
    LOAD,
    JOIN
}
public class MainMenuManager : MonoBehaviour {

    public GameObject blankSessionSlot, sessionContent, blankSaveSlot, saveContent, blankPlayerSlot, playerContent, joinButton, startButton, startSessionButton;
    public Toggle splendorToggle, citiesToggle, tradingPostsToggle;
    public UnityEvent promptEndSession, promptDeleteSession, joinSession, loadSave, createSession, exitToMain, exitToSession, exitToSave;
    public Text playerText, sessionNameText;
    public Save currentSave;
    public Session currentSession;
    private LastMenuVisited previousMenu = LastMenuVisited.MAIN;
    public NetworkManager networkManager;
    public Authentication authentication;

    public AllCards allCards;
    public NobleRow allNobles;

    public GlobalGameClient globalGameClient;
    public GameData game;

    private string HOST = "127.0.0.1";

    public void LoadLastMenu() {
        if (previousMenu == LastMenuVisited.MAIN)
            exitToMain.Invoke();
        else if (previousMenu == LastMenuVisited.LOAD)
            exitToSave.Invoke();
        else if (previousMenu == LastMenuVisited.JOIN)
            exitToSession.Invoke();
    }

    /// <summary>
    /// Sends the GET request to the LobbyService for data on all sessions
    /// and sends the data to "BaseJoin".
    /// </summary>
    public void OnBaseJoinClick() {
        game = null;
        StartCoroutine(SessionManager.GetSessions(HOST, (List<Session> sessions) => { 
            if (sessions != null && sessions.Count > 0) {
                List<Session> available = determineAvailable(sessions); //determine which sessions are available

                if (available != null && available.Count > 0) MakeSessions(available); //display available sessions
                else ClearChildren(sessionContent); //clear sessions display
            } else ClearChildren(sessionContent); //clear sessions display
        }));
    }

    /// <summary>
    /// Sends the GET request to the LobbyService for data on all saved games
    /// and sends the data to "BaseLoad".
    /// </summary>
    public void OnBaseLoadClick() {
        game = null;
        StartCoroutine(SessionManager.GetSaves(HOST, authentication, (List<Save> saves) => {
            if (saves != null && saves.Count > 0){
                List<Save> relevant = determineRelevant(saves);

                if (relevant != null && relevant.Count > 0) MakeSaves(relevant); //displays relevant saved games
                else ClearChildren(saveContent); //clear saved games display
            } else ClearChildren(saveContent); //clear saved games display
        }));
    }

    /// <summary>
    /// Sends the POST request to the LobbyService that creates a new session
    /// and sends the id to "SessionCreate".
    /// </summary>
    public void OnSessionCreateClick() {

        game = null;

        string variant = ""; //determine game version based on selected toggle
        if (splendorToggle.isOn) variant = "splendor";
        else if (citiesToggle.isOn) variant = "cities";
        else if (tradingPostsToggle.isOn) variant = "tradingposts";

        StartCoroutine(SessionManager.CreateSession(HOST, variant, authentication, (string id) => {
            StartCoroutine(SessionManager.GetSession(HOST, id, (Session session) => {
                previousMenu = LastMenuVisited.MAIN;
                currentSession = session;
                createSession.Invoke(); // location of this event may change in the future
                MakePlayers(); // displays the players in the current session

                //BROKEN POLLING
                //while (currentSession.players.Count < currentSession.minSessionPlayers) { // check whether the session can be launched
                //    Invoke("PollSession", 3); // calls poll session after 3 seconds
                //    MakePlayers();
                //}

                startSessionButton.SetActive(true); // allow the host to start the session
            }));
        }));
    }

    /// <summary>
    /// GET request for session data.
    /// </summary>
    private void PollSession() {
        StartCoroutine(SessionManager.GetSession(HOST, currentSession.id, (Session result) => currentSession = result));
    }

    /// <summary>
    /// Sends the PUT request to the LobbyService that adds a player to a session.
    /// Once the player is added to the session in the LobbyService, bring player to Lobby
    /// and poll for session updates.
    /// </summary>
    public void OnSessionJoinClick() {
        if (currentSession != null){
            StartCoroutine(SessionManager.Join(HOST, authentication, currentSession));

            previousMenu = LastMenuVisited.JOIN;
            globalGameClient.id = currentSession.id;
            networkManager.joinPolling(globalGameClient.id, this);

            StartCoroutine(SessionManager.GetSession(HOST, currentSession.id, (Session session) => {
                currentSession = session;
                joinSession.Invoke();

                //BROKEN POLLING
                //while (game == null) { // check whether the session has been launched
                //    Invoke("PollSession", 3); // calls poll session after 3 seconds
                //    MakePlayers();
                //}
                //UNCOMMENT WHEN POLLING WORKS
                //SceneManager.LoadScene(2);
            }));
        }
    }

    /// <summary>
    /// Sends the POST request to the LobbyService that creates a new session from a savegameid
    /// and sends the id of the new session to "SaveStart".
    /// </summary>
    public void OnSaveStartClick() {
        StartCoroutine(SessionManager.CreateSavedSession(HOST, currentSave, authentication, (string id) => {
            StartCoroutine(SessionManager.GetSession(HOST, id, (Session session) => {
                previousMenu = LastMenuVisited.LOAD;
                currentSession = session;

                //UPDATE WITH POLLING

                loadSave.Invoke(); // location of this event may change in the future
                MakePlayers(); // displays the players in the current session
            }));
        }));
    }

    /// <summary>
    /// Prompts the player to confirm that they want to leave the lobby.
    /// </summary>
    public void OnLobbyBackClick() {
        if (authentication.username.Equals(currentSession.creator)) {
            promptDeleteSession.Invoke();
        }
        promptEndSession.Invoke();
    }

    /// <summary>
    /// Removes the player from the (unlaunched) current session in the LobbyService.
    /// </summary>
    public void OnConfirmEndClick() {
        StartCoroutine(SessionManager.Leave(HOST, authentication, currentSession));
        LoadLastMenu();
    }

    /// <summary>
    /// Removes the creator from the (unlaunched) current session in the LobbyService, removing the session altogether.
    /// </summary>
    public void OnConfirmDeleteClick() {
        //DELETE request to /api/sessions/{session} ? see answer to Ed question
        LoadLastMenu();
    }

    /// <summary>
    /// Launches the game with the LobbyService
    /// </summary>
    public void OnLobbyStartClick() {

        StartCoroutine(SessionManager.Launch(HOST, authentication, currentSession, (bool successfulLaunch) => {
            if (successfulLaunch){
                StartCoroutine(GameNetworkManager.GetGame(HOST, currentSession.id, (GameData game) =>
                {
                    this.game = game;
                    SceneManager.LoadScene(2);
                }));//get game data from server
            }
        }));
    }

    /// <summary>
    /// Determines if a session from the list of sessions is available to join and displays it if so.
    /// <param name="sessions">Session List of all sessions to check the availability of</param>
    /// </summary>
    public List<Session> determineAvailable(List<Session> sessions)
    {
        List<Session> availableSessions = new List<Session>();

        foreach (Session session in sessions)
        {
            if (session.launched == true) continue; //a launched session is not available
            else if (session.players.Count == session.maxSessionPlayers) continue; //a full session is not available
            else availableSessions.Add(session);
        }

        return availableSessions; //to display the available sessions
    }

    /// <summary>
    /// Determines if a save from a given list of saves is relevant to the main player and displays it if so.
    /// </summary>
    /// <param name="allSaves">Save List of all saves having a savegame id in the LobbyService</param>
    public List<Save> determineRelevant(List<Save> saves)
    {
        List<Save> relevantSaves = new List<Save>();

        foreach (Save save in saves)
        {
            if (save.players.Contains(authentication.username)) relevantSaves.Add(save);
        }

        return relevantSaves; 
    }

    /// <summary>
    /// Displays the lobby according to the current session.
    /// </summary>
    public void SetupLobby() {
        int playerCount = currentSession.players.Count();
        if (playerCount == 1)
        {
            playerText.text = "1 player of " + currentSession.maxSessionPlayers + " total players";
        }
        else {
            playerText.text = playerCount + " players of " + currentSession.maxSessionPlayers + " total players";
        }
        sessionNameText.text = currentSession.GetVariant();
    }

    /// <summary>
    /// Setter for currently selected session.
    /// </summary>
    /// <param name="newSession">currently selected Session</param>
    public void SetSession(Session newSession) {
        currentSession = newSession;
    }

    /// <summary>
    /// Setter for currently selected saved game.
    /// </summary>
    /// <param name="newSave">currently selected Save</param>
    public void SetSave(Save newSave) {
        currentSave = newSave;
    }

    /// <summary>
    /// Displays sessions in "join" menu.
    /// </summary>
    /// <param name="sessions">Session List of all available sessions which must be displayed</param>
    public void MakeSessions(List<Session> sessions) { 
        currentSession = null;

        ClearChildren(sessionContent);
        foreach (Session session in sessions) {
            GameObject temp = Instantiate(blankSessionSlot, sessionContent.transform.position, Quaternion.identity);
            temp.transform.SetParent(sessionContent.transform);
            temp.transform.localScale = new Vector3(1, 1, 1);
            temp.GetComponent<SessionSlot>().Setup(this, session, joinButton);
        }
    }

    /// <summary>
    /// Displays saves in "load save" menu.
    /// </summary>
    /// <param name="saves">Save List of all saves relevant to the main player which must be displayed</param>
    public void MakeSaves(List<Save> saves) { 
        currentSave = null;

        ClearChildren(saveContent);
        foreach (Save save in saves) {
            GameObject temp = Instantiate(blankSaveSlot, saveContent.transform.position, Quaternion.identity);
            temp.transform.SetParent(saveContent.transform);
            temp.transform.localScale = new Vector3(1, 1, 1);
            temp.GetComponent<SaveSlot>().Setup(this, save, startButton);
        }
    }

    /// <summary>
    /// Displays players in the lobby.
    /// </summary>
    public void MakePlayers() {
        ClearChildren(playerContent);
        foreach (string player in currentSession.players) {
            GameObject temp = Instantiate(blankPlayerSlot, playerContent.transform.position, Quaternion.identity);
            temp.transform.SetParent(playerContent.transform);
            temp.transform.localScale = new Vector3(1, 1, 1);
            temp.GetComponent<PlayerSlot>().Setup(player);
        }
    }

    /// <summary>
    /// Clears content children in a parent GameObject.
    /// See MakePlayers, MakeSaves, MakeSessions for examples
    /// </summary>
    /// <param name="content">the parent game object that holds the content children</param>
    void ClearChildren(GameObject content) { 
        foreach (Transform child in content.transform)
            Destroy(child.gameObject);
    }

    public void StartJoinedGame() {
        SceneManager.LoadScene(2);
    }
}
