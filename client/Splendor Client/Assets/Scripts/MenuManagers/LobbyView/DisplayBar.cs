using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using System;

public class DisplayBar : MonoBehaviour
{
    public GameObject maxPlayer;
    public Text displayBar;

    public void SetUpDisplay(){
        InputField maxNum = maxPlayer.GetComponent<InputField>();
        //int max = int.TryParse(maxNum.text, out int result);dicarded
        displayBar.text = String.Format("PLAYERS 1/{0}", maxNum.text);
    }
}
