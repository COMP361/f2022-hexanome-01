using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.Events;

public class MainMenuManager : MonoBehaviour {
    public GameObject blankSessionSlot, sessionContent, blankSaveSlot, saveContent, blankPlayerSlot, playerContent;
    public SaveList saveList;
    public SessionList sessionList;
    public LobbyPlayerList playerList;
    public UnityEvent leaveSession, promptEndSession;
    [SerializeField] private Save currentSave;
    [SerializeField] private Session currentSession;
    //TODO
    //      
    //      player colours in lobby?
    
    public void SetSession(Session newSession) { //set currently selected session
        currentSession = newSession;
    }

    public void SetSave(Save newSave) { //set currently selected save
        currentSave = newSave;
    }

    public void MakeSessions() { //displays sessions in menu
        currentSession = null;
        ClearChildren(sessionContent);
        foreach(Session session in sessionList.sessions) {
            GameObject temp = Instantiate(blankSessionSlot, sessionContent.transform.position, Quaternion.identity);
            temp.transform.parent = sessionContent.transform;
            temp.GetComponent<SessionSlot>().Setup(this, session);
        }
    }
    public void MakeSaves() { //displays saves in menu
        currentSave = null;
        ClearChildren(saveContent);
        foreach (Save save in saveList.saves) {
            GameObject temp = Instantiate(blankSaveSlot, saveContent.transform.position, Quaternion.identity);
            temp.transform.parent = saveContent.transform;
            temp.GetComponent<SaveSlot>().Setup(this, save);
        }
    }

    public void MakePlayers() { //displays players in lobby
        ClearChildren(playerContent);
        //foreach loop
        foreach(LobbyPlayer player in playerList.lobbyplayers){
            GameObject temp = Instantiate(blankPlayerSlot, playerContent.transform.position, Quaternion.identity);
            temp.transform.parent = playerContent.transform;
            temp.GetComponent<PlayerSlot>().Setup(this, player);
        }

    }

    void ClearChildren(GameObject content) { //clears players in lobby
        foreach (Transform child in content.transform)
            Destroy(child.gameObject);
    }

    public void ExitSession() { 
        if (false)  //if host, show prompt
            promptEndSession.Invoke();
        else  //else, leave session
            leaveSession.Invoke();
    }
    public void ContinueGame() {
        //final implementation will load last-used save (i.e. autosave) into a new session, currently just starts a regular session through the button
    }

    public void StartGame() { //available to host in game lobby
        SceneManager.LoadScene(2);
    }
    //create session method? particularly for starting session with dedicated save file
}
