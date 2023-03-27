using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class WinnerScript : MonoBehaviour
{
    public Winner winner;
    public Text message;

    public void setMessage(string winnerName) { 
        winner.playerName = winnerName; 
        message.text = winner.playerName + "wins the game!"; 
        }

}
