using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;
using UnityEngine.Events;
using System.Linq;

public enum LastMenuVisited {
    MAIN,
    LOAD,
    JOIN
}
public class MainMenuManager : MonoBehaviour {

    public GameObject blankSessionSlot, sessionContent, blankSaveSlot, saveContent, blankPlayerSlot, playerContent, joinButton, startButton;
    public Toggle splendorToggle, citiesToggle, tradingPostsToggle;
    public UnityEvent leaveSession, promptEndSession, joinSession, loadSave, createSession, exitToMain, exitToSession, exitToSave;
    public Text playerText, sessionNameText;
    public Save currentSave;
    public Session currentSession;
    private LastMenuVisited previousMenu = LastMenuVisited.MAIN;
    public NetworkManager networkManager;
    public Authentication authentication;

    public AllCards allCards;
    public NobleRow allNobles;

    public GlobalGameClient globalGameClient;

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

        StartCoroutine(SessionManager.GetSessions(HOST, BaseJoin));

    }

    /// <summary>
    /// Displays the available sessions if any.
    /// </summary>
    /// <param name="sessions">Session List of all sessions from the LobbyService</param>
    private void BaseJoin(List<Session> sessions) {

        if (sessions != null && sessions.Count > 0)
        {

            List<Session> available = determineAvailable(sessions); //determine which sessions are available

            if (available != null && available.Count > 0)
                MakeSessions(available); //display available sessions
            else
                ClearChildren(sessionContent); //clear sessions display

        }
        else ClearChildren(sessionContent); //clear sessions display

    }

    /// <summary>
    /// Sends the GET request to the LobbyService for data on all saved games
    /// and sends the data to "BaseLoad".
    /// </summary>
    public void OnBaseLoadClick() {

        StartCoroutine(SessionManager.GetSaves(HOST, authentication, BaseLoad));

    }

    /// <summary>
    /// Displays the relevant saved games if any.
    /// </summary>
    /// <param name="saves">Save List of all saved games from the LobbyService</param>
    private void BaseLoad(List<Save> saves) {

        if (saves != null && saves.Count > 0)
        {
            List<Save> relevant = determineRelevant(saves);

            if (relevant != null && relevant.Count > 0)
                MakeSaves(relevant); //displays relevant saved games
            else
                ClearChildren(saveContent); //clear saved games display
        }
        else ClearChildren(saveContent); //clear saved games display

    }

    /// <summary>
    /// Sends the POST request to the LobbyService that creates a new session
    /// and sends the id to "SessionCreate".
    /// </summary>
    public void OnSessionCreateClick() {

        StartCoroutine(SessionManager.CreateSession(HOST, authentication, SessionCreate));

    }

    /// <summary>
    /// Sends the GET request to the LobbyService for the created session's data
    /// and sends the session to "SessionCreate2".
    /// </summary>
    /// <param name="id">the id of the created session</param>
    private void SessionCreate(string id) {

        string variant = ""; //determine game version based on selected toggle
        if (splendorToggle.isOn) variant = "splendor";
        else if (citiesToggle.isOn) variant = "cities";
        else if (tradingPostsToggle.isOn) variant = "tradingposts";

        StartCoroutine(SessionManager.GetSession(HOST, id, variant, SessionCreate2));

    }

    /// <summary>
    /// Sets the current session to the created session and opens the lobby.
    /// </summary>
    /// <param name="session">the session that was created</param>
    private void SessionCreate2(Session session) {

        previousMenu = LastMenuVisited.MAIN;
        currentSession = session;

        createSession.Invoke(); // location of this event may change in the future
        MakePlayers(); // displays the players in the current session

    }

    /// <summary>
    /// Sends the PUT request to the LobbyService that adds a player to a session.
    /// Once the player is added to the session in the LobbyService, bring player to Lobby
    /// and poll for session updates.
    /// </summary>
    public void OnSessionJoinClick() {

        if (currentSession != null)
        {
            StartCoroutine(SessionManager.Join(HOST, authentication, currentSession));

            previousMenu = LastMenuVisited.JOIN;
            //globalGameClient.id = currentSession.players[0] + "-" + currentSession.name;
            globalGameClient.id = currentSession.id;
            networkManager.joinPolling(globalGameClient.id, this);

            StartCoroutine(SessionManager.GetSession(HOST, currentSession.id, currentSession.variant.ToString(), SessionJoin));
        }
    }

    /// <summary>
    /// Update the current session following the addition of the player.
    /// </summary>
    /// <param name="session">Session being updated</param>
    private void SessionJoin(Session session) {

        currentSession = session;
        joinSession.Invoke();
    }

    public void OnSaveStartClick() { }

    public void OnLobbyBackClick() { }

    public void OnLobbyStartClick() { }

    

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

    public void SetSession(Session newSession) { //set currently selected session
        currentSession = newSession;
    }

    public void SetSave(Save newSave) { //set currently selected save
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

    /// <summary>
    /// Prompts the player to confirm that they want to leave the lobby.
    /// </summary>
    public void ExitSession() {
        promptEndSession.Invoke();
    }

    public void StartGame() { //available to host in game lobby
        LobbyPlayer demoPlayer = new LobbyPlayer();
        demoPlayer.username = "linus";
        currentSession.players.Add(demoPlayer.username);
        networkManager.registerGame(new GameConfigData(authentication, currentSession, allCards, allNobles));
        globalGameClient.id = authentication.username + "-" + currentSession.name;
        SceneManager.LoadScene(2);
    }

    public void StartJoinedGame() {
        SceneManager.LoadScene(2);
    }
}
