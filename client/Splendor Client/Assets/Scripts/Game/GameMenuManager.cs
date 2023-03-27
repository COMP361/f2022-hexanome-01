using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class GameMenuManager : MonoBehaviour
{
    [SerializeField] private ActiveSession currentSession;

    public void OnSaveClick()
    {
        //TO DO: replace savegameid with response from server
        //(send request to save to server and get back savegameid)
        string savegameid = "";
        StartCoroutine(GameRequestManager.SaveGame(currentSession.name, currentSession.players, savegameid));
    }

    public void OnExitClick()
    {
        StartCoroutine(LSRequestManager.DeleteSession(currentSession.id));
        SceneManager.LoadScene(1);
    }

    public void OnSaveAndExitClick(){
        StartCoroutine(saveAndExitRoutine());
    }

    IEnumerator saveAndExitRoutine(){
        string savegameid = "";
        yield return StartCoroutine(GameRequestManager.SaveGame(currentSession.name, currentSession.players, savegameid));
        Debug.Log("saved game");
        yield return StartCoroutine(LSRequestManager.DeleteSession(currentSession.id));
        SceneManager.LoadScene(1);
    }
}
