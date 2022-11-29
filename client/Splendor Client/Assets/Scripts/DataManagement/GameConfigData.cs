using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[System.Serializable]
public class GameConfigData {
    public string gameName;
    public string hostId;
    public string[] playerIds;
  
    public CardData[] deck1;
    public CardData[] deck2;
    public CardData[] deck3;
    public CardData[] exDeck1;
    public CardData[] exDeck2;
    public CardData[] exDeck3;
    
    public NobleData[] allNobles;

    public GameConfigData() { }
    public GameConfigData(Authentication mainPlayer, SessionData session, AllCards allCards, NobleRow allNobles) {
        gameName = session.sessionName;
        hostId = mainPlayer.username;
        playerIds = new string[4];
        for(int i = 0;i < session.playerList.Length; i++) {
            playerIds[i] = session.playerList[i].username;
        }
        deck1 = allCards.cards[0].deck.ToArray();
        deck2 = allCards.cards[1].deck.ToArray();
        deck3 = allCards.cards[2].deck.ToArray();
        exDeck1 = allCards.cards[3].deck.ToArray();
        exDeck2 = allCards.cards[4].deck.ToArray();
        exDeck3 = allCards.cards[5].deck.ToArray();

        this.allNobles = allNobles.ToArray();
    }


}
