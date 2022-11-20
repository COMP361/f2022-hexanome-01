using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using System;

public class DisplayBar : MonoBehaviour
{
    public GameObject maxPlayer;
    public LobbyPlayerList playerList;
    public Text displayBar;

    public void SetUpDisplay(){
        int currentNum = playerList.Count();
        InputField maxNum = maxPlayer.GetComponent<InputField>();
        //int max = int.TryParse(maxNum.text, out int result);dicarded
        displayBar.text = String.Format("PLAYERS {0}/{1}", currentNum, maxNum.text);
    }
}
