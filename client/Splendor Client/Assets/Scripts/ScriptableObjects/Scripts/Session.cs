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
    public string sessionName;

    public enum GameVersion { splendor, cities, tradingposts };

    public Session() {
        players = new List<string>();
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
        sessionName = data.sessionName;
    }

    public Session(string name, int maxSessionPlayers, List<LobbyPlayer> playerList, string sessionName) {
        Enum.TryParse<GameVersion>(name, out this.name);
        this.maxSessionPlayers = maxSessionPlayers;

        this.players = new List<string>();
        foreach (LobbyPlayer player in playerList) {
            players.Add(player.username);
        }

        this.sessionName = sessionName;
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