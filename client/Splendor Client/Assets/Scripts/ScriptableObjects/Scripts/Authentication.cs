using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[CreateAssetMenu]
public class Authentication : ScriptableObject
{
    //stores information about user upon authentication with LobbyService
    public string username, access_token, refresh_token, expires_in;
}
