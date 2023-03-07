using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[CreateAssetMenu]
public class Authentication : ScriptableObject
{
    //stores information about user upon authentication with LobbyService
    private string username, access_token, refresh_token;

    public void SetUsername(string username) {
        this.username = username;
    }

    public void SetAccessToken(string accessToken) {
        this.access_token = accessToken;
    }

    public void SetRefreshToken(string refreshToken) {
        this.refresh_token = refreshToken;
    }

    public string GetUsername() {
        return username;
    }

    public string GetAccessToken() {
        return access_token;
    }

    public string GetRefreshToken() {
        return refresh_token;
    }
}
