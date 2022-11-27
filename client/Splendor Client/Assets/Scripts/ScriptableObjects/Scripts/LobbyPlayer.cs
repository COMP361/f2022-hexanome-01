using System.Collections;
using System.Collections.Generic;
using System.Runtime.CompilerServices;
using UnityEngine;

[System.Serializable]
public class LobbyPlayer {
    public string username, token;

    public LobbyPlayer(string username, string token) {
        this.username = username;
    }
}
