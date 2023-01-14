using System;
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
    public List<string> players;
    public string savegameid;
    public GameVariant variant;

    public enum GameVariant { none, splendor, cities, tradingposts };

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

    public Session(string variant, int maxSessionPlayers, List<LobbyPlayer> playerList) {
        this.SetVariant(variant);
        this.maxSessionPlayers = maxSessionPlayers;

        this.players = new List<string>();
        foreach (LobbyPlayer player in playerList) {
            players.Add(player.username);
        }
    }

    public string GetVariant(){
        switch (variant) {
            case GameVariant.splendor: return "splendor with orient";
            case GameVariant.cities: return "splendor with orient and cities";
            case GameVariant.tradingposts: return "splendor with orient and trading posts";
            case GameVariant.none: return "default (splendor)";
            default: return variant.ToString();
        }
    }

    public void SetVariant(string variant){
        Enum.TryParse<GameVariant>(variant, out this.variant);
    }

    public string PlayersToString(){
        string result = "";
        foreach (string player in players) {
            result += player + ", ";
        }
        return result.Trim().TrimEnd(',');
    }
}