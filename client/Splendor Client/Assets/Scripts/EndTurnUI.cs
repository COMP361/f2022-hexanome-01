using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class EndTurnUI : MonoBehaviour
{
    public Text buttonText;
    public PlayerControl playerControl;

    void OnClick()
    {
        playerControl.EndTurn();
    }
}
