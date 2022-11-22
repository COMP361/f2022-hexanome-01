using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[CreateAssetMenu]
public class Save : ScriptableObject {
    //insert save file information, probably wont stay as a ScriptableObject
    public string saveName;
    public int maxPlayers;
    public List<LobbyPlayer> playerList = new List<LobbyPlayer>();
    //other game info, such as who can join and what their hands were, state of game, etc
}
