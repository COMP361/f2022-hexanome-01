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
    public Text playerText, sessionNameText;
    public GameObject nameField;
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
        sessionList.sessions.Add(new Session("Yang's Game", 4, temp1));
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
    public void CreateSession() {
        if (nameField.GetComponent<InputField>().text != "") {
            previousMenu = LastMenuVisited.MAIN;
            createSession.Invoke(); //location of this event may change in the future
            string sessionName = sessionNameText.text;
            int maxPlayers = 4;
            createdSession.sessionName = sessionNameText.text;
            //determine player count based on selected toggle
            Toggle[] toggles = GetComponents<Toggle>();
            foreach (Toggle toggle in toggles) {
                if (toggle.isOn) {
                    switch (toggle.name) {
                        // case "TwoPlayersToggle": maxPlayers = 2; break;
                        // case "ThreePlayersToggle": maxPlayers = 3; break;
                        // case "FourPlayersToggle": maxPlayers = 4; break;
                        case "TwoPlayersToggle": createdSession.maxPlayers = 2; break;
                        case "ThreePlayersToggle": createdSession.maxPlayers = 3; break;
                        case "FourPlayersToggle": createdSession.maxPlayers = 4; break;
                    }
                    break;
                } 
            }
            createdSession.maxPlayers = maxPlayers;
            createdSession.playerList.Add(new LobbyPlayer(authentication));

            // authentication = Instantiate(authentication);

            // string username = authentication.username;
            // string access_token = authentication.access_token;
            // string refresh_token = authentication.refresh_token;
            // string expires_in = authentication.expires_in;
            // LobbyPlayer host = new LobbyPlayer(username, access_token, refresh_token, expires_in);
            // StartCoroutine(networkManager.postSession(sessionName, maxPlayers, host));
            sessionCreated = true;
            currentSession = createdSession;
            //add host/this player to playerlist of created session here
            MakePlayers();
        }
    }

    public void LoadSave(bool mostRecent) {
        if (mostRecent)
            currentSave = DEFAULTSAVE;
        if (currentSave) {
            previousMenu = mostRecent ? LastMenuVisited.MAIN : LastMenuVisited.LOAD;
            createdSession.sessionName = currentSave.saveName;
            createdSession.maxPlayers = currentSave.maxPlayers;
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
            globalGameClient.id = currentSession.playerList[0].username + "-" + currentSession.sessionName;
            currentSession.playerList.Add(new LobbyPlayer(authentication));
            networkManager.joinPolling(globalGameClient.id, this);
            joinSession.Invoke();
        }
    }

    public void SetupLobby() {
        playerText.text = "PLAYERS " + currentSession.playerList.Count() + "/" + currentSession.maxPlayers;
        sessionNameText.text = currentSession.sessionName;
    }

    public void SetSession(Session newSession) { //set currently selected session
        currentSession = newSession;
    }

    public void SetSave(Save newSave) { //set currently selected save
        currentSave = newSave;
    }

    public void MakeSessions() { //displays sessions in menu
        currentSession = null;
        // networkManager.getSessions(sessions);
        // foreach (SessionData s in sessions) {
        //     sessionList.sessions.Add(new Session(s));
        // }

        // HARDCODE FOR DEMO
        Session demo = new Session();
        demo.sessionName = "test";
        demo.maxPlayers = 2;
        LobbyPlayer demoHost = new LobbyPlayer();
        demoHost.username = "maex";
        demo.playerList.Add(demoHost);
        LobbyPlayer demoPlayer = new LobbyPlayer();
        demoPlayer.username = "linus";
        sessionList.sessions.Add(demo);
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
        foreach (LobbyPlayer player in currentSession.playerList) {
            GameObject temp = Instantiate(blankPlayerSlot, playerContent.transform.position, Quaternion.identity);
            temp.transform.SetParent(playerContent.transform);
            temp.transform.localScale = new Vector3(1, 1, 1);
            temp.GetComponent<PlayerSlot>().Setup(this, player);
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
        networkManager.registerGame(new GameConfigData(authentication, new SessionData(currentSession), allCards, allNobles));
        globalGameClient.id = authentication.username + "-" + currentSession.sessionName;
        SceneManager.LoadScene(2);
    }

    public void StartJoinedGame() {
        SceneManager.LoadScene(2);
    }
}
