using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[CreateAssetMenu]
public class LobbyPlayerList : ScriptableObject
{
    public List<LobbyPlayer> lobbyPlayers;

    public int Count(){
        return lobbyPlayers.Count;
    }
}
