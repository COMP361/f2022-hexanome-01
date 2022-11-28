using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Session {
    //insert session information, maybe wont stay as a ScriptableObject
    public string sessionName;
    public int maxPlayers;
    public List<LobbyPlayer> playerList = new List<LobbyPlayer>();

    public Session() { }

    public Session(SessionData data) {
        sessionName = data.sessionName;
        maxPlayers = data.maxPlayers;
        for (int i = 0; i < data.playerList.Length; i++)
            playerList.Add(data.playerList[i]); //WILL NEED TO CHANGE ONCE IK ACTUAL PLAYER DATA
    }

    public Session(string sessionName, int maxPlayers, List<LobbyPlayer> playerList) {
        this.sessionName = sessionName;
        this.maxPlayers = maxPlayers;
        this.playerList = playerList;
    }

    public string getSessionName(){
        return sessionName;
    }
}
