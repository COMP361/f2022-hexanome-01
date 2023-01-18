using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[System.Serializable]
public class GameData {
    public string gameId;
    public PlayerData currentPlayer;
    public CardData[] row1;
    public CardData[] row2;
    public CardData[] row3;
    public CardData[] exRow1;
    public CardData[] exRow2;
    public CardData[] exRow3;
    public NobleData[] nobles;
    public PlayerData[] players; //idk where this data is stored/how to get this data

    /* WIP
    public GameData(IDictionary values) {
        gameId = values["gameId"].ToString();

        JSONArray currentPlayer = (JSONArray)JSONHandler.DecodeJsonRequest(values["currentPlayer"].ToString());
        this.currentPlayer = new PlayerData(currentPlayer[0].ToString(), Int.Parse(currentPlayer[1].ToString));

        SetRow(false, 1, values["row1"].ToString());
        SetRow(false, 2, values["row2"].ToString());
        SetRow(false, 3, values["row3"].ToString());
        SetRow(true, 1, values["row1"].ToString());
        SetRow(true, 2, values["row2"].ToString());
        SetRow(true, 3, values["row3"].ToString());

        nobles = values["nobles"];
        players = values["players"];
    }

    private void SetRow(bool orient, int row, string values) {

        if (orient) {
            switch (row) {
                case 1: exRow1 = 
            }
        }

    }
    */
}
