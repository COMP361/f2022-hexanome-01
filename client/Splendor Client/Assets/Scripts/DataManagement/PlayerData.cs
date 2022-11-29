using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[System.Serializable]
public class PlayerData {
    public string id;
    public int points;

    public PlayerData() { }
    public PlayerData(LobbyPlayer lobbyPlayer) { 
        id = lobbyPlayer.username;
        points = 0;
    }
    
}
