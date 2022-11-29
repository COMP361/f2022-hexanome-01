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

}
