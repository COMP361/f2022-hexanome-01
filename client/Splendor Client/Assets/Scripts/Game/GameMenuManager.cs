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
        StartCoroutine(LSRequestManager.DeleteSession(currentSession.id));
        SceneManager.LoadScene(1);
    }

    public void OnSaveAndExitClick(){
        StartCoroutine(saveAndExitRoutine());
    }

    IEnumerator saveAndExitRoutine(){
        yield return StartCoroutine(GameRequestManager.SaveGameServer(currentSession.id));
        Debug.Log("saved game");
        yield return StartCoroutine(LSRequestManager.DeleteSession(currentSession.id));
        SceneManager.LoadScene(1);
    }
}
