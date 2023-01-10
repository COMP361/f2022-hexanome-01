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
    public SaveList saveList;
    public SessionList sessionList;
    public UnityEvent leaveSession, promptEndSession, joinSession, loadSave, createSession, exitToMain, exitToSession, exitToSave;
    public Text playerText, sessionNameText, nameField;
    [SerializeField] private Save currentSave;
    private Session currentSession;
    private bool sessionCreated;
    private LastMenuVisited previousMenu = LastMenuVisited.MAIN;
    public Save DEFAULTSAVE; //temp var until saves work properly.
    public NetworkManager networkManager;
    public Authentication authentication;
    private SessionData[] sessions;

    public AllCards allCards;
    public NobleRow allNobles;

    public GlobalGameClient globalGameClient;
    //TODO
    //      
    //      player colours in lobby?

    //EMPTY START METHOD BEFORE DEMO, creates 2 hardcoded players
    private void Start() {
        List<LobbyPlayer> temp1 = new List<LobbyPlayer>();
        temp1.Add(new LobbyPlayer("Yang", "TEMP", "TEMP_REFRESH", DateTime.Now.ToString()));
        temp1.Add(new LobbyPlayer("Joshua", "TEMP", "TEMP_REFRESH", DateTime.Now.ToString()));
        sessionList.sessions.Add(new Session("splendor", 4, temp1, "Yang's Game"));
    }

    public void TempCreateSessionJson() {
        FileManager.EncodeSession(sessionList.sessions[0], true);
    }
    public void TempLoadSessionJson() {
        sessionList.sessions.Add(FileManager.DecodeSession("SessionData-Joshua", true));
    }

    public void LoadLastMenu() {
        if (previousMenu == LastMenuVisited.MAIN)
            exitToMain.Invoke();
        else if (previousMenu == LastMenuVisited.LOAD)
            exitToSave.Invoke();
        else if (previousMenu == LastMenuVisited.JOIN)
            exitToSession.Invoke();
    }

    public void CreateSession(Session session) {
        previousMenu = LastMenuVisited.MAIN;
        currentSession = session;
        sessionCreated = true;

        createSession.Invoke(); // location of this event may change in the future
        MakePlayers(); // displays the players in the current session
    }

    /// <summary>
    /// Determines if a session from a given list of sessions is available to join and displays it if so.
    /// </summary>
    /// <param name="sessions">SessionListData of all sessions currently stored in the LobbyService</param>
    public void determineAvailable(List<Session> allSessions)
    {
        List<SessionData> availableSessions = new List<SessionData>();

        if (allSessions != null)
        {
            foreach (Session session in allSessions)
            {
                if (session.launched == true) continue; //a launched session is not available
                else if (session.players.Count == session.maxSessionPlayers) continue; //a full session is not available
                else availableSessions.Add(new SessionData(session));
            }

            MakeSessions(availableSessions.ToArray()); //displays the available sessions
        }
        else { //if there are no sessions available
            ClearChildren(sessionContent); 
        }
    }

    public void LoadSave(bool mostRecent) {
        if (mostRecent)
            currentSave = DEFAULTSAVE;
        if (currentSave) {
            previousMenu = mostRecent ? LastMenuVisited.MAIN : LastMenuVisited.LOAD;
            createdSession.sessionName = currentSave.saveName;
            createdSession.maxSessionPlayers = currentSave.maxPlayers;
            sessionCreated = true;
            currentSession = createdSession;
            loadSave.Invoke();
            MakePlayers();
        }
    }

    public void JoinSession() {
        sessionCreated = false;
        if (currentSession != null) {
            previousMenu = LastMenuVisited.JOIN;
            globalGameClient.id = currentSession.players[0] + "-" + currentSession.name;
            currentSession.players.Add(authentication.username);
            networkManager.joinPolling(globalGameClient.id, this);
            joinSession.Invoke();
        }
    }

    public void SetupLobby() {
        int playerCount = currentSession.players.Count();
        if (playerCount == 1)
        {
            playerText.text = "1 player of " + currentSession.maxSessionPlayers + " total players";
        }
        else {
            playerText.text = playerCount + " players of " + currentSession.maxSessionPlayers + " total players";
        }
        sessionNameText.text = currentSession.getName();
    }

    public void SetSession(Session newSession) { //set currently selected session
        currentSession = newSession;
    }

    public void SetSave(Save newSave) { //set currently selected save
        currentSave = newSave;
    }

    /**
     * Displays sessions in "join" menu.
     */
    public void MakeSessions(SessionData[] sessions) { //displays sessions in menu
        currentSession = null;
        foreach (SessionData s in sessions)
        {
            sessionList.sessions.Add(new Session(s));
        }

        // HARDCODE FOR DEMO
        //Session demo = new Session();
        //demo.name = "test";
        //demo.maxSessionPlayers = 2;
        //LobbyPlayer demoHost = new LobbyPlayer();
        //demoHost.username = "maex";
        //demo.players.Add("maex");
        //LobbyPlayer demoPlayer = new LobbyPlayer();
        //demoPlayer.username = "linus";
        //demo.players.Add("linus");
        //sessionList.sessions.Add(demo);
        //

        ClearChildren(sessionContent);
        foreach (Session session in sessionList.sessions) {
            GameObject temp = Instantiate(blankSessionSlot, sessionContent.transform.position, Quaternion.identity);
            temp.transform.SetParent(sessionContent.transform);
            temp.transform.localScale = new Vector3(1, 1, 1);
            temp.GetComponent<SessionSlot>().Setup(this, session);
        }
    }

    public void MakeSaves() { //displays saves in menu
        currentSave = null;
        ClearChildren(saveContent);
        foreach (Save save in saveList.saves) {
            GameObject temp = Instantiate(blankSaveSlot, saveContent.transform.position, Quaternion.identity);
            temp.transform.SetParent(saveContent.transform);
            temp.transform.localScale = new Vector3(1, 1, 1);
            temp.GetComponent<SaveSlot>().Setup(this, save);
        }
    }

    public void MakePlayers() { //displays players in lobby
        ClearChildren(playerContent);
        foreach (string player in currentSession.players) {
            GameObject temp = Instantiate(blankPlayerSlot, playerContent.transform.position, Quaternion.identity);
            temp.transform.SetParent(playerContent.transform);
            temp.transform.localScale = new Vector3(1, 1, 1);
            temp.GetComponent<PlayerSlot>().Setup(player);
        }
    }

    void ClearChildren(GameObject content) { //clears players in lobby
        foreach (Transform child in content.transform)
            Destroy(child.gameObject);
    }

    public void ExitSession() {
        if (sessionCreated)  //if host, show prompt
            promptEndSession.Invoke();
        else  //else, leave session
            LoadLastMenu();
    }
    public void ContinueGame() {
        //final implementation will load last-used save (i.e. autosave) into a new session, currently just starts a regular session through the create session button
    }

    public void StartGame() { //available to host in game lobby
        LobbyPlayer demoPlayer = new LobbyPlayer();
        demoPlayer.username = "linus";
        currentSession.players.Add(demoPlayer.username);
        networkManager.registerGame(new GameConfigData(authentication, new SessionData(currentSession), allCards, allNobles));
        globalGameClient.id = authentication.username + "-" + currentSession.name;
        SceneManager.LoadScene(2);
    }

    public void StartJoinedGame() {
        SceneManager.LoadScene(2);
    }
}
