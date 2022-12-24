using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Session {
    //insert session information, maybe wont stay as a ScriptableObject
    public string id;
    public string creator;
    public string location;
    public int maxSessionPlayers;
    public int minSessionPlayers;
    public string name;
    public bool launched;
    public string[] players;
    public string savegameid;
    
    public Session() { }

    public Session(SessionData data) {
        id = data.id;
        creator = data.creator;
        location = data.location;
        maxSessionPlayers = data.maxSessionPlayers;
        minSessionPlayers = data.minSessionPlayers;
        name = data.name;
        launched = data.launched;
        players = data.players;
        savegameid = data.savegameid;
    }

    public Session(string name, int maxSessionPlayers, List<LobbyPlayer> playerList) {
        this.name = name;
        this.maxSessionPlayers = maxSessionPlayers;

        this.players = new string[playerList.Count];
        for (int i = 0; i < playerList.Count; i++) {
            players[i] = playerList[i].username;
        }
    }

    public string getSessionName(){
        return name;
    }
}
