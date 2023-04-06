using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class WinnerScript : MonoBehaviour
{
    public Winner winner;
    public Text message;

    void Start(){
        message.text = winner.playerName + " wins the game!"; 
    }

}
