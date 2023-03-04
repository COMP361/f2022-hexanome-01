using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class EndTurnUI : MonoBehaviour
{
    public PlayerControl playerControl;

    void OnClick()
    {
        playerControl.EndTurn();
    }
}
