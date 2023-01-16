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

    public GameObject blankSessionSlot, sessionContent, blankSaveSlot, saveContent, blankPlayerSlot, playerContent;
    private Session createdSession = new Session();
    public UnityEvent leaveSession, promptEndSession, joinSession, loadSave, createSession, exitToMain, exitToSession, exitToSave;
    public Text playerText, sessionNameText;
    [SerializeField] private Save currentSave;
    public Session currentSession;
    private LastMenuVisited previousMenu = LastMenuVisited.MAIN;
    public NetworkManager networkManager;
    public Authentication authentication;

    public AllCards allCards;
    public NobleRow allNobles;

    public GlobalGameClient globalGameClient;
   
    public void LoadLastMenu() {
        if (previousMenu == LastMenuVisited.MAIN)
            exitToMain.Invoke();
        else if (previousMenu == LastMenuVisited.LOAD)
            exitToSave.Invoke();
        else if (previousMenu == LastMenuVisited.JOIN)
            exitToSession.Invoke();
    }

    /// <summary>
    /// On create button click from "create" menu, sets the current session to the created session and opens the lobby.
    /// </summary>
    /// <param name="session">the session that was created</param>
    public void CreateSession(Session session) {
        previousMenu = LastMenuVisited.MAIN;
        currentSession = session;

        createSession.Invoke(); // location of this event may change in the future
        MakePlayers(); // displays the players in the current session
    }

    /// <summary>
    /// Determines if a session from a given list of sessions is available to join and displays it if so.
    /// </summary>
    /// <param name="allSessions">Session List of all sessions currently stored in the LobbyService</param>
    public void determineAvailable(List<Session> allSessions)
    {
        List<Session> availableSessions = new List<Session>();

        if (allSessions != null)
        {
            foreach (Session session in allSessions)
            {
                if (session.launched == true) continue; //a launched session is not available
                else if (session.players.Count == session.maxSessionPlayers) continue; //a full session is not available
                else availableSessions.Add(session);
            }

            MakeSessions(availableSessions); //displays the available sessions
        }
        else { //if there are no available sessions 
            ClearChildren(sessionContent); 
        }
    }

    /// <summary>
    /// Determines if a save from a given list of saves is relevant to the main player and displays it if so.
    /// </summary>
    /// <param name="allSaves">Save List of all saves having a savegame id in the LobbyService</param>
    public void determineRelevant(List<Save> allSaves)
    {
        List<Save> relevantSaves = new List<Save>();

        if (allSaves != null)
        {
            foreach (Save save in allSaves)
            {
                if (save.players.Contains(authentication.username)) relevantSaves.Add(save);
            }

            MakeSaves(relevantSaves); //displays the relevant saves
        }
        else { //if there are no relevant saves
            ClearChildren(saveContent);
        }
    }

    public void OnStartSaveClick()
    {
        previousMenu = LastMenuVisited.LOAD;
        currentSession = createdSession;
        loadSave.Invoke();
        MakePlayers();
    }

    public void OnJoinSessionClick()
    {
        if (currentSession != null)
        {
            previousMenu = LastMenuVisited.JOIN;
            globalGameClient.id = currentSession.players[0] + "-" + currentSession.name;
            currentSession.players.Add(authentication.username);
            networkManager.joinPolling(globalGameClient.id, this);
            joinSession.Invoke();
        }
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
        //currentSession = null;

        ClearChildren(sessionContent);
        foreach (Session session in sessions) {
            GameObject temp = Instantiate(blankSessionSlot, sessionContent.transform.position, Quaternion.identity);
            temp.transform.SetParent(sessionContent.transform);
            temp.transform.localScale = new Vector3(1, 1, 1);
            temp.GetComponent<SessionSlot>().Setup(this, session);
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
            temp.GetComponent<SaveSlot>().Setup(this, save);
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
