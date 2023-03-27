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

    [SerializeField] private GameObject blankSessionSlot, sessionContent, lobbyView, blankSaveSlot, saveContent, blankPlayerSlot, playerContent, joinButton, startButton, startSessionButton;
    [SerializeField] private Toggle splendorToggle, citiesToggle, tradingPostsToggle;
    [SerializeField] private UnityEvent promptEndSession, promptDeleteSession, joinSession, loadSave, createSession, exitToMain, exitToSession, exitToSave;
    [SerializeField] private Text playerText, sessionNameText;
    [SerializeField] private Authentication authentication;

    private LastMenuVisited previousMenu = LastMenuVisited.MAIN;

    public Save currentSave;
    [SerializeField] private ActiveSession currentSession;
    private string sessionsHash = null;
    private string sessionHash = null;
	private string savesHash = null;

    //******************************** MAIN MENU ********************************

    public void LoadLastMenu() {
        //reset all data
        currentSession.Reset();
        sessionsHash = null;
        sessionHash = null;

        //switch menus
        if (previousMenu == LastMenuVisited.MAIN)
            exitToMain.Invoke();
        else if (previousMenu == LastMenuVisited.LOAD)
            exitToSave.Invoke();
        else if (previousMenu == LastMenuVisited.JOIN)
            exitToSession.Invoke();
    }

    /// <summary>
    /// Implements long polling for the GET request to the LobbyService for data on all sessions.
    /// </summary>
    public void OnBaseJoinClick() {
        StartCoroutine(LSRequestManager.GetSessions(sessionsHash, (string hash, List<Session> sessions) => {
            if (hash != null) {
                if (sessions != null && sessions.Count > 0) {
                    List<Session> available = determineAvailable(sessions); //determine which sessions are available

                    if (available != null && available.Count > 0) MakeSessions(available); //display available sessions
                    else ClearChildren(sessionContent); //clear sessions display
                } else ClearChildren(sessionContent); //clear sessions display

                sessionsHash = hash;
                if (sessionContent.activeInHierarchy) OnBaseJoinClick();
            } else if (sessionContent.activeInHierarchy) OnBaseJoinClick();
        }));
    }

    /// <summary>
    /// Sends the GET request to the LobbyService for data on all saved games
    /// and sends the data to "BaseLoad".
    /// </summary>
    public void OnBaseLoadClick() {
        StartCoroutine(LSRequestManager.GetSaves(savesHash, (string hash, List<Save> saves) => {
			if (hash != null) {            
				if (saves != null && saves.Count > 0){
                	List<Save> relevant = determineRelevant(saves);

                	if (relevant != null && relevant.Count > 0) MakeSaves(relevant); //displays relevant saved games
                	else ClearChildren(saveContent); //clear saved games display
            	} else ClearChildren(saveContent); //clear saved games display
				
				savesHash = hash;
				if (saveContent.activeInHierarchy) OnBaseLoadClick();
			} else if (saveContent.activeInHierarchy) OnBaseLoadClick();
        }));
    }

    /// <summary>
    /// Sends the POST request to the LobbyService that creates a new session
    /// and sends the id to "SessionCreate".
    /// </summary>
    public void OnSessionCreateClick() {
        string variant = ""; //determine game version based on selected toggle
        if (splendorToggle.isOn) variant = "splendor";
        else if (citiesToggle.isOn) variant = "cities";
        else if (tradingPostsToggle.isOn) variant = "tradingposts";

        StartCoroutine(LSRequestManager.CreateSession(variant, LobbyPolling));

        previousMenu = LastMenuVisited.MAIN;
        createSession.Invoke();
    }

    //******************************** JOIN MENU ********************************

    /// <summary>
    /// Sends the PUT request to the LobbyService that adds a player to a session.
    /// Once the player is added to the session in the LobbyService, bring player to Lobby
    /// and poll for session updates.
    /// </summary>
    public void OnSessionJoinClick() {
        if (currentSession != null){
            StartCoroutine(LSRequestManager.Join(currentSession));

            previousMenu = LastMenuVisited.JOIN;

            LobbyPolling(currentSession.id);
            joinSession.Invoke();
        }
    }

    //******************************** SAVE MENU ********************************

    /// <summary>
    /// Sends the POST request to the LobbyService that creates a new session from a savegameid
    /// and sends the id of the new session to "SaveStart".
    /// </summary>
    public void OnSaveStartClick() {
        StartCoroutine(LSRequestManager.CreateSavedSession(currentSave, (string id) => {
            StartCoroutine(LSRequestManager.GetSession(id, sessionHash, (string hash, Session session) => {
                if (hash != null) {
                    if (session != null) {
                        previousMenu = LastMenuVisited.LOAD;
                        currentSession.SetSession(session);
                        loadSave.Invoke(); // location of this event may change in the future
                        MakePlayers(); // displays the players in the current session

                        if (currentSession.players.Count > 2 && currentSession.creator.Equals(authentication.GetUsername()))
                            startSessionButton.SetActive(true); // allow the host to start the session
                    }

                    sessionHash = hash;
                    if (lobbyView.activeInHierarchy) LobbyPolling(currentSession.id);
                } else if (lobbyView.activeInHierarchy) LobbyPolling(currentSession.id);
            }));
        }));
    }

    //******************************** LOBBY ********************************

    /// <summary>
    /// Prompts the player to confirm that they want to leave the lobby.
    /// </summary>
    public void OnLobbyBackClick() {
        if (authentication.GetUsername().Equals(currentSession.creator)) promptDeleteSession.Invoke();
        else promptEndSession.Invoke();
    }

    /// <summary>
    /// Removes the player from the (unlaunched) current session in the LobbyService.
    /// </summary>
    public void OnConfirmEndClick() {
        StartCoroutine(LSRequestManager.Leave(currentSession));
        LoadLastMenu();
    }

    /// <summary>
    /// Removes the creator from the (unlaunched) current session in the LobbyService, removing the session altogether.
    /// </summary>
    public void OnConfirmDeleteClick() {
        StartCoroutine(LSRequestManager.DeleteSession(currentSession.id));
        LoadLastMenu();
    }

    /// <summary>
    /// Launches the game with the LobbyService
    /// </summary>
    public void OnLobbyStartClick() {

        StartCoroutine(LSRequestManager.Launch(currentSession, (bool success) =>
            {
                if (success)
                    SceneManager.LoadScene(2);
            }));
    }

    //******************************** HELPERS ********************************

    /// <summary>
    /// 
    /// </summary>
    /// <param name="id"></param>
    public void LobbyPolling(string id)
    {
        StartCoroutine(LSRequestManager.GetSession(id, sessionHash, (string hash, Session session) =>
        {
            if (session != null && hash != null)
            {
                currentSession.SetSession(session);
                sessionHash = hash;
                if (session.launched)
                    SceneManager.LoadScene(2);
                else
                {
                    SetupLobby();
                    MakePlayers(); // displays the players in the current session

                    if (session.players.Count >= 2 && session.creator.Equals(authentication.GetUsername()))
                        startSessionButton.SetActive(true); // allow the host to start the session

                    if (lobbyView.activeInHierarchy) LobbyPolling(currentSession.id);
                }
            }
            else if (lobbyView.activeInHierarchy) LobbyPolling(currentSession.id);
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
            if (save.players.Contains(authentication.GetUsername())) relevantSaves.Add(save);
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
        if (newSession != null) currentSession.SetSession(newSession);
    }

    /// <summary>
    /// Setter for currently selected saved game.
    /// </summary>
    /// <param name="newSave">currently selected Save</param>
    public void SetSave(Save newSave) {
        if (newSave != null) currentSave = newSave;
    }

    /// <summary>
    /// Displays sessions in "join" menu.
    /// </summary>
    /// <param name="sessions">Session List of all available sessions which must be displayed</param>
    public void MakeSessions(List<Session> sessions) {
        if (sessionContent.activeInHierarchy)
        {
            currentSession.Reset();
            sessionHash = null;

            ClearChildren(sessionContent);
            foreach (Session session in sessions)
            {
                GameObject temp = Instantiate(blankSessionSlot, sessionContent.transform.position, Quaternion.identity);
                temp.transform.SetParent(sessionContent.transform);
                temp.transform.localScale = new Vector3(1, 1, 1);
                temp.GetComponent<SessionSlot>().Setup(this, session, joinButton);
            }
        }
    }

    /// <summary>
    /// Displays saves in "load save" menu.
    /// </summary>
    /// <param name="saves">Save List of all saves relevant to the main player which must be displayed</param>
    public void MakeSaves(List<Save> saves) { 
        //currentSave = null;

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
    public void Winning() {
        SceneManager.LoadScene("WinScene");
    }
}
