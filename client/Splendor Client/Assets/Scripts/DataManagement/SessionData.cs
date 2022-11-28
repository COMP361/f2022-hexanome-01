using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[System.Serializable]
public class SessionData {
    public string sessionName;
    public int maxPlayers;
    public LobbyPlayer[] playerList; //WILL NEED TO BE CHANGED/EXPANDED LATER SINCE PLAYERS ARE MORE THAN JUST A NAME and token maybe

    public SessionData() { }

    
    public SessionData(Session session) {
        sessionName = session.sessionName;
        maxPlayers = session.maxPlayers;
        playerList = new LobbyPlayer[session.playerList.Count];
        for(int i = 0;i < session.playerList.Count; i++) { //WILL ALSO NEED TO BE CHANGED
            playerList[i] = session.playerList[i];
        }
    }
}
