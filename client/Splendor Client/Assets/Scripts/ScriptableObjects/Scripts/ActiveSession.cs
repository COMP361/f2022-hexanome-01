using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[CreateAssetMenu]
public class ActiveSession : ScriptableObject {

    public string id;
    public string creator;
    public string location;
    public int maxSessionPlayers;
    public int minSessionPlayers;
    public string name;
    public bool launched;
    public List<string> players;
    public string savegameid;

    public ActiveSession() {
        players = new List<string>();
    }

    public void Reset()
    {
        id = "";
        creator = "";
        location = "";
        maxSessionPlayers = 0;
        minSessionPlayers = 0;
        name = "";
        launched = false;
        players = new List<string>();
        savegameid = "";
    }

    public void SetSession(Session session)
    {
        id = session.id;
        creator = session.creator;
        location = session.location;
        maxSessionPlayers = session.maxSessionPlayers;
        minSessionPlayers = session.minSessionPlayers;
        name = session.name;
        launched = session.launched;
        players = session.players;
        savegameid = session.savegameid;
    }

    public string GetVariant(){
        switch (name) {
            case "splendor": return "splendor with orient";
            case "cities": return "splendor with orient and cities";
            case "tradingposts": return "splendor with orient and trading posts";
            default: return name;
        }
    }

    /// <summary>
    /// Used for displaying session info in "join" menu.
    /// </summary>
    /// <returns>string of players to display for the session</returns>
    public string PlayersToString(){
        string result = "";
        if (players.Count > 0)
        {
            foreach (string player in players)
            {
                result += player + ", ";
            }
            result = result.Trim().TrimEnd(',');
        }
        return result;
    }

    /// <summary>
    /// For SessionManager to be able to create request bodies including player info.
    /// </summary>
    /// <returns>string representation of JSON of the session's players</returns>
    public string PlayersToJSONString() {
        string result = "[";
        if (players.Count > 0) {
            foreach (string player in players) {
                result += "\"" + player + "\", ";
            }
            result = result.Trim().TrimEnd(',');
        }
        return result + "]";
    }
}