using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[System.Serializable]
public class SessionData {
    public string id;
    public string creator;
    public string location;
    public int maxSessionPlayers;
    public int minSessionPlayers;
    public string name;
    public bool launched;
    public string[] players;
    public string savegameid;

    public SessionData() { }

    
    public SessionData(Session session) {
        id = session.id;
        creator = session.creator;
        location = session.location;
        maxSessionPlayers = session.maxSessionPlayers;
        minSessionPlayers = session.minSessionPlayers;
        name = session.name;
        launched = session.launched;
        players = session.players.ToArray();
        savegameid = session.savegameid;
    }
}
