using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[System.Serializable]
public class GameData {
    public string gameId;
    public int[] shuffleSeeds;
    public int currentPlayer;
    public CardData[] row1;
    public CardData[] row2;
    public CardData[] row3;
    public CardData[] exRow1;
    public CardData[] exRow2;
    public CardData[] exRow3;
    public NobleData[] nobles;
    public PlayerData[] players; //idk where this data is stored/how to get this data
    

    public GameData() { }
    public GameData(PlayerControl boardInfo) {
        gameId = boardInfo.gameId;
        currentPlayer = boardInfo.currentPlayer;
        nobles = boardInfo.allNobels.ToArray();

        for (int i=0; i<6; i++) {
            shuffleSeeds[i] = boardInfo.allCards.cards[i].deck.randomSeed;
        }

        row1 = boardInfo.allCards.cards[0].DeckToArray();
        row2 = boardInfo.allCards.cards[1].DeckToArray();
        row3 = boardInfo.allCards.cards[2].DeckToArray();
        exRow1 = boardInfo.allCards.cards[3].DeckToArray();
        exRow2 = boardInfo.allCards.cards[4].DeckToArray();
        exRow3 = boardInfo.allCards.cards[5].DeckToArray();
       
        players = boardInfo.gamePlayersData.ToArray();
    }


}
