using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class PlayerSlot : MonoBehaviour
{
    public Text playerName;
    public Color preferredColor;
    [SerializeField] private MainMenuManager thisManager;
    [SerializeField] private LobbyPlayer thisLobbyPlayer;

    public void SetColor() {
        // this will set the in game player color to the one they selected from a color palette object 
        //(this color palette object is not made yet)
    }

    public void Setup(MainMenuManager newManager, LobbyPlayer player) {
        thisManager = newManager;
        thisLobbyPlayer = player;
        SetText();
    }
    public void SetText() {
        playerName.text = thisLobbyPlayer.userName;
    }

}
