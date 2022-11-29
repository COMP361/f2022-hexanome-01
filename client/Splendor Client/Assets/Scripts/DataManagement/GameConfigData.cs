using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[System.Serializable]
public class GameConfigData {
    public string gameName;
    public string hostName;
    public string[] playerIds;

    public GameConfigData() { }
    public GameConfigData(string gameName, string hostName, string[] playerIds) {
        this.gameName = gameName;
        this.hostName = hostName;
        this.playerIds = playerIds;
    }


}
