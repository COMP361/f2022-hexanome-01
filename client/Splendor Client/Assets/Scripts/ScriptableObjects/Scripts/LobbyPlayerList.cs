using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[CreateAssetMenu]
public class LobbyPlayerList : ScriptableObject
{
    public List<LobbyPlayer> lobbyplayers;

    public int Count(){
        int count = 0;
        foreach (LobbyPlayer player in lobbyplayers)
        {
            count++;
        }
        return count;
    }
}
