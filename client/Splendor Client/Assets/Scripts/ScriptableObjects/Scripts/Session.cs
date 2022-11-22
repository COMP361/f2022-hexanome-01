using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Session {
    public string sessionName;
    public int maxPlayers;
    public List<LobbyPlayer> playerList = new List<LobbyPlayer>();

    public Session() { }
    public Session(string sessionName, int maxPlayers, List<LobbyPlayer> playerList) {
        this.sessionName = sessionName;
        this.maxPlayers = maxPlayers;
        this.playerList = playerList;
    }

    public Session(SessionData data) {
        sessionName = data.sessionName;
        maxPlayers = data.maxPlayers;
        //instantiate players. maybe remove 'LobbyPlayerList' entirely and just store list of players in session class?
        for (int i = 0; i < data.playerList.Length; i++)
            playerList.Add(new LobbyPlayer(data.playerList[i]));
    }
}
