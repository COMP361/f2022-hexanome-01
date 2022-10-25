using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.Events;

public class MainMenuManager : MonoBehaviour {
    public GameObject blankSessionSlot, sessionContent, blankSaveSlot, saveContent, blankPlayerSlot, playerContent;
    public SaveList saveList;
    public SessionList sessionList;
    public UnityEvent leaveSession, promptEndSession;
    [SerializeField] private Save currentSave;
    [SerializeField] private Session currentSession;
    //Todo: player data
    //      select save/session to load/join
    //      player colours in lobby?
    
    public void MakeSessions() { //displays sessions in menu
        foreach(Session session in sessionList.sessions) {
            GameObject temp = Instantiate(blankSessionSlot, sessionContent.transform.position, Quaternion.identity);
            temp.transform.parent = sessionContent.transform;
            temp.GetComponent<SessionSlot>().Setup(this, session);
        }
    }

    public void ClearSessions() { //clears sessions in menu
        foreach (Transform child in sessionContent.transform)
            Destroy(child.gameObject);
    }

    public void MakeSaves() { //displays saves in menu
        foreach (Save save in saveList.saves) {
            GameObject temp = Instantiate(blankSaveSlot, saveContent.transform.position, Quaternion.identity);
            temp.transform.parent = saveContent.transform;
            temp.GetComponent<SaveSlot>().Setup(this, save);
        }
    }

    public void ClearSaves() { //clears saves in menu
        foreach (Transform child in saveContent.transform)
            Destroy(child.gameObject);
    }

    public void MakePlayers() { //displays players in lobby

    }

    public void ClearPlayers() { //clears players in lobby
        foreach (Transform child in playerContent.transform)
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
