using System;
using System.Collections;
using System.Collections.Generic;
using System.Runtime.CompilerServices;
using UnityEngine;

[System.Serializable]
public class LobbyPlayer {
    public string username, access_token, refresh_token, expires_in;

    public LobbyPlayer() {}
    public LobbyPlayer(string username, string access_token, string refresh_token, string expires_in) {
        this.username = username;
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.expires_in = expires_in;
    }

    public LobbyPlayer(Authentication authentication) {
        this.username = authentication.username;
        this.access_token = authentication.access_token;
        this.refresh_token = authentication.refresh_token;
        this.expires_in = authentication.expires_in;
    }
}
