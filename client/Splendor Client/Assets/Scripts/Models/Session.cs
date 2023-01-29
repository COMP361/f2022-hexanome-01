using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Session {

    public string id;
    public string creator;
    public string location;
    public int maxSessionPlayers;
    public int minSessionPlayers;
    public string name;
    public bool launched;
    public List<string> players;
    public string savegameid;

    public Session() {
        players = new List<string>();
    }

    /// <summary>
    /// Used when decoding from JSON.
    /// </summary>
    /// <param name="id">the LobbyService's id for the session</param>
    /// <param name="values">all other key-values stored in the LobbyServcie for this session</param>
    public Session(string id, IDictionary values) {
        this.id = id;
        creator = values["creator"].ToString();
        launched = bool.Parse(values["launched"].ToString());
        savegameid = values["savegameid"].ToString();
        JSONObject gameParameters = (JSONObject)JSONHandler.DecodeJsonRequest(values["gameParameters"].ToString());
        JSONArray players = (JSONArray)JSONHandler.DecodeJsonRequest(values["players"].ToString());
        this.players = new List<string>();
        foreach (string player in players)
            this.players.Add(player);
        maxSessionPlayers = int.Parse(gameParameters["maxSessionPlayers"].ToString());
        minSessionPlayers = int.Parse(gameParameters["minSessionPlayers"].ToString());
        location = gameParameters["location"].ToString();
        name = gameParameters["name"].ToString();
    }

    //public Session(string variant, int maxSessionPlayers, List<LobbyPlayer> playerList) {
    //    this.SetVariant(variant);
    //    this.maxSessionPlayers = maxSessionPlayers;

    //    this.players = new List<string>();
    //    foreach (LobbyPlayer player in playerList) {
    //        players.Add(player.username);
    //    }
    //}

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