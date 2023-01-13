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
    public GameVersion name;
    public bool launched;
    public List<string> players;
    public string savegameid;

    public enum GameVersion { splendor, cities, tradingposts };

    public Session() {
        players = new List<string>();
    }

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
        Enum.TryParse<GameVersion>(gameParameters["name"].ToString(), out name);
    }

    public Session(SessionData data) {
        id = data.id;
        creator = data.creator;
        location = data.location;
        maxSessionPlayers = data.maxSessionPlayers;
        minSessionPlayers = data.minSessionPlayers;
        Enum.TryParse<GameVersion>(data.name, out name);
        launched = data.launched;
        if (data.players == null) players = new List<string>();
        else players = new List<string>(data.players);
        savegameid = data.savegameid;
    }

    public Session(string name, int maxSessionPlayers, List<LobbyPlayer> playerList) {
        Enum.TryParse<GameVersion>(name, out this.name);
        this.maxSessionPlayers = maxSessionPlayers;

        this.players = new List<string>();
        foreach (LobbyPlayer player in playerList) {
            players.Add(player.username);
        }
    }

    public string getName(){
        switch (name) {
            case GameVersion.splendor: return "splendor with orient";
            case GameVersion.cities: return "splendor with orient and cities";
            case GameVersion.tradingposts: return "splendor with orient and trading posts";
            default: return name.ToString();
        }
    }
}