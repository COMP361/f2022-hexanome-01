using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[System.Serializable]
public class SessionData {
    public string sessionName;
    public int maxPlayers;
    public string[] playerList; //WILL NEED TO BE CHANGED/EXPANDED LATER SINCE PLAYERS ARE MORE THAN JUST A NAME

    public SessionData() { }
    public SessionData(Session session) {
        sessionName = session.sessionName;
        maxPlayers = session.maxPlayers;
        playerList = new string[session.playerList.Count];
        for(int i = 0;i < session.playerList.Count; i++) { //WILL ALSO NEED TO BE CHANGED
            playerList[i] = session.playerList[i].userName;
        }
    }
}
