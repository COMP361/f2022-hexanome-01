using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[System.Serializable]
public class GameData {
    public string gameId;
    public int currentPlayer;
    public CardData[] row1;
    public CardData[] row2;
    public CardData[] row3;
    public CardData[] exRow1;
    public CardData[] exRow2;
    public CardData[] exRow3;
    public NobleData[] noblesDisplayed;
    public PlayerData[] playersInGame; //idk where this data is stored/how to get this data
    

    public GameData() { }
    public GameData(PlayerControl boardInfo) {
        gameId = boardInfo.gameId;
        currentPlayer = boardInfo.currentPlayer;
        noblesDisplayed = boardInfo.allNobels.ToArray();

        Debug.Log(noblesDisplayed[0].points);
        row1 = boardInfo.allCards.cards[0].DeckToArray();
        row2 = boardInfo.allCards.cards[1].DeckToArray();
        row3 = boardInfo.allCards.cards[2].DeckToArray();
        exRow1 = boardInfo.allCards.cards[3].DeckToArray();
        exRow2 = boardInfo.allCards.cards[4].DeckToArray();
        exRow3 = boardInfo.allCards.cards[5].DeckToArray();
       
        playersInGame = new PlayerData[boardInfo.gamePlayersData.Count];
        for (int i = 0; i < boardInfo.gamePlayersData.Count; i++)
            playersInGame[i] = boardInfo.gamePlayersData[i];
    }


}
