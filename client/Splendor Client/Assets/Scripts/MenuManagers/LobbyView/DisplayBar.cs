using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using System;
using System.Linq;

public class DisplayBar : MonoBehaviour
{
    public GameObject maxPlayer;
    public Text displayBar;
    public Toggle twoPlayersToggle;
    public Toggle threePlayersToggle;
    public Toggle fourPlayersToggle;

    public void SetUpDisplay(){
        int maxNum = 0;

        if (twoPlayersToggle.isOn) maxNum = 2;
        else if (threePlayersToggle.isOn) maxNum = 3;
        else if (fourPlayersToggle.isOn) maxNum = 4;

        //int max = int.TryParse(maxNum.text, out int result);dicarded
        displayBar.text = String.Format("PLAYERS 1/{0}", maxNum);
    }
}
