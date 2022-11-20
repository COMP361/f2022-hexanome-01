using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;
using UnityEngine.Events;

public enum LastMenuVisited {
    MAIN,
    LOAD,
    JOIN
}
public class MainMenuManager : MonoBehaviour {
    public GameObject blankSessionSlot, sessionContent, blankSaveSlot, saveContent, blankPlayerSlot, playerContent;
    public Session createdSession;
    public SaveList saveList;
    public SessionList sessionList;
    public LobbyPlayerList playerList;
    public UnityEvent leaveSession, promptEndSession, joinSession, loadSave, createSession, exitToMain, exitToSession, exitToSave;
    public Text playerText, sessionNameText;
    public GameObject playerField, nameField;
    [SerializeField] private Save currentSave;
    [SerializeField] private Session currentSession;
    private bool sessionCreated;
    private LastMenuVisited previousMenu = LastMenuVisited.MAIN;
    public Save DEFAULTSAVE; //temp var until saves work properly.
    //TODO
    //      
    //      player colours in lobby?
    public void LoadLastMenu() {
        if (previousMenu == LastMenuVisited.MAIN)
            exitToMain.Invoke();
        else if (previousMenu == LastMenuVisited.LOAD)
            exitToSave.Invoke();
        else if (previousMenu == LastMenuVisited.JOIN)
            exitToSession.Invoke();
    }
    public void CreateSession() {
        if (playerField.GetComponent<InputField>().text != "" && nameField.GetComponent<InputField>().text != "") {
            previousMenu = LastMenuVisited.MAIN;
            createSession.Invoke(); //location of this event may change in the future
            createdSession.sessionName = sessionNameText.text;
            createdSession.maxPlayers = int.Parse(playerText.text[playerText.text.Length - 1].ToString());
            sessionCreated = true;
            currentSession = createdSession;
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
        }
    }

    public void JoinSession() {
        sessionCreated = false;
        if (currentSession) {
            previousMenu = LastMenuVisited.JOIN;
            joinSession.Invoke();
        }
    }

    public void SetupLobby() {
        playerText.text = "PLAYERS " + currentSession.playerList.Count() + "/" + currentSession.maxPlayers;
        sessionNameText.text = currentSession.sessionName;
    }

    public void LobbyPlayerLimits(InputField inputField) {
        if (inputField.text == "-")
            inputField.text = "";
        else if (inputField.text != "" && int.Parse(inputField.text) < 2)
            inputField.text = "2";
        else if (inputField.text != "" && int.Parse(inputField.text) > 4)
            inputField.text = "4";
    }

    public void SetSession(Session newSession) { //set currently selected session
        currentSession = newSession;
    }

    public void SetSave(Save newSave) { //set currently selected save
        currentSave = newSave;
    }

    public void MakeSessions() { //displays sessions in menu
        currentSession = null;
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
        foreach (LobbyPlayer player in playerList.lobbyPlayers) {
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
        SceneManager.LoadScene(2);
    }
}
