using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class GameMenuManager : MonoBehaviour
{
    [SerializeField] private ActiveSession currentSession;
    [SerializeField] private Authentication mainPlayer;

    public void OnSaveClick()
    {
        StartCoroutine(GameRequestManager.SaveGameServer(currentSession.id));
    }

    public void OnExitClick()
    {
        StartCoroutine(exitRoutine());
    }

    public void OnExitEndSessionClick(){
        SceneManager.LoadScene(1);
    }

    public void OnExitWin() {
        SceneManager.LoadScene(1);
    }

    public void OnSaveAndExitClick(){
        StartCoroutine(saveAndExitRoutine());
    }

    public void OnSaveAndExitEndSessionClick(){
        StartCoroutine(saveAndExitEndSessionRoutine());
    }

    IEnumerator saveAndExitRoutine(){
        yield return StartCoroutine(GameRequestManager.SaveGameServer(currentSession.id));
        Debug.Log("saved game");
        yield return StartCoroutine(GameRequestManager.DeleteGameServer(currentSession.id));
        SceneManager.LoadScene(1);
    }

    IEnumerator saveAndExitEndSessionRoutine(){
        yield return StartCoroutine(GameRequestManager.SaveGameServer(currentSession.id));
        SceneManager.LoadScene(1);
    }
    IEnumerator exitRoutine(){
        yield return StartCoroutine(GameRequestManager.DeleteGameServer(currentSession.id));
        SceneManager.LoadScene(1);
    }
}
