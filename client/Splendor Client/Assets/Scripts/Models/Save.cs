using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Save {

    public string gamename;
    public List<string> players;
    public string savegameid;
    //other game info, such as who can join and what their hands were, state of game, etc

    public Save() {
        players = new List<string>();
    }

    /// <summary>
    /// Used when decoding from JSON.
    /// </summary>
    /// <param name="save">all key-values stored in the LobbyServcie for this saved game</param>
    public Save(JSONObject save) {
        gamename = save["gamename"].ToString();
        this.players = new List<string>();
        JSONArray players = (JSONArray)JSONHandler.DecodeJsonRequest(save["players"].ToString());
        foreach (string player in players)
            this.players.Add(player);
        savegameid = save["savegameid"].ToString();
    }

    public string GetVariant()
    {
        switch (gamename){
            case "splendor": return "splendor with orient";
            case "cities": return "splendor with orient and cities";
            case "tradingposts": return "splendor with orient and trading posts";
            default: return gamename;
        }
    }

    /// <summary>
    /// Used for displaying saved game info in "load save" menu.
    /// </summary>
    /// <returns>string of the saved game's players</returns>
    public string PlayersToString()
    {
        string result = "";
        foreach (string player in players)
        {
            result += player + ", ";
        }
        return result.Trim().TrimEnd(',');
    }

	public void SetSave(Save save) {
		gamename = save.gamename;
		players = save.players;
		savegameid = save.savegameid;
	}

	public void Reset() {
		gamename = "";
		players = new List<string>();
		savegameid = "";
	}
}
