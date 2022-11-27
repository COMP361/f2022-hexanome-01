using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GameData {
    public CardData[][] cards;
    public NobleData[] noblesDisplayed;
    public PlayerData[] playersInGame; //idk where this data is stored/how to get this data

    public GameData() { }
    public GameData(PlayerControl boardInfo) {
        noblesDisplayed = new NobleData[boardInfo.noblesOnBoard.Count];
        //for (int i = 0: i < boardInfo.allCards.cards.; int++)
    }
}
