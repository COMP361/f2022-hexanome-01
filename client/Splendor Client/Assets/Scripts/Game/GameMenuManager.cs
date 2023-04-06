using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class GameMenuManager : MonoBehaviour
{
    [SerializeField] private ActiveSession currentSession;
    [SerializeField] private Authentication mainPlayer;
    
    [SerializeField] private FadeOut successText;
    [SerializeField] private FadeOut failText;

    public void OnSaveClick()
    {
        StartCoroutine(GameRequestManager.SaveGameServer(currentSession.id, successText, failText));
    }

    public void OnExitClick()
    {
        StartCoroutine(exitRoutine());
    }

    public void OnExitEndSessionClick(){
        SceneManager.LoadScene(0);
    }

    public void OnExitWin() {
        SceneManager.LoadScene(0);
    }

    public void OnSaveAndExitClick(){
        StartCoroutine(saveAndExitRoutine());
    }

    public void OnSaveAndExitEndSessionClick(){
        StartCoroutine(saveAndExitEndSessionRoutine());
    }

    IEnumerator saveAndExitRoutine(){
        yield return StartCoroutine(GameRequestManager.SaveGameServer(currentSession.id, successText, failText));
        Debug.Log("saved game");
        yield return StartCoroutine(GameRequestManager.DeleteGameServer(currentSession.id));
        SceneManager.LoadScene(0);
    }

    IEnumerator saveAndExitEndSessionRoutine(){
        yield return StartCoroutine(GameRequestManager.SaveGameServer(currentSession.id, successText, failText));
        SceneManager.LoadScene(0);
    }
    IEnumerator exitRoutine(){
        yield return StartCoroutine(GameRequestManager.DeleteGameServer(currentSession.id));
        SceneManager.LoadScene(0);
    }
}
