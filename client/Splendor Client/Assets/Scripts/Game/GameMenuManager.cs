using System.Collections;
using System.Collections.Generic;
using UnityEngine;

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
}
