using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GameData {
    public string gameId;
    public CardData[][] cards;
    public NobleData[] noblesDisplayed;
    public PlayerData[] playersInGame; //idk where this data is stored/how to get this data

    public GameData() { }
    public GameData(PlayerControl boardInfo) {
        gameId = boardInfo.gameId;
        noblesDisplayed = new NobleData[boardInfo.noblesOnBoard.Count];
        for (int i = 0; i < boardInfo.noblesOnBoard.Count; i++)
            noblesDisplayed[i] = new NobleData(boardInfo.noblesOnBoard[i].GetNoble());
        cards = new CardData[boardInfo.allCards.cards.Length][];
        for (int i = 0; i < boardInfo.allCards.cards.Length; i++) {
            cards[i] = new CardData[boardInfo.allCards.cards[i].deck.Count()];
            for (int j = 0; j < boardInfo.allCards.cards[i].deck.Count(); j++)
                cards[i][j] = new CardData(boardInfo.allCards.cards[i].deck.cards[j]);
        }
        playersInGame = new PlayerData[boardInfo.gamePlayersData.Count];
        for (int i = 0; i < boardInfo.gamePlayersData.Count; i++)
            playersInGame[i] = boardInfo.gamePlayersData[i];
    }
}
