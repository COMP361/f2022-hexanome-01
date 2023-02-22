using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

//TO DO: add token bank

[CreateAssetMenu]
public class Board : ScriptableObject
{
    [SerializeField] private NobleRow nobles;
    [SerializeField] private AllCards cards;
    [SerializeField] private Player[] players = new Player[4];
    private int playerCount;

    public void launch(string host, string id) {
        //get board data & set it
        GameRequestManager.instance.launch(host, id, SetBoard);
    }

    public void SetBoard(JSONObject boardData) {
        //STEP 1: set cards
        JSONArray cardsData = (JSONArray)boardData["cards"];
        //for each level
        IEnumerator cardLevelEnumerator = cardsData.GetEnumerator();
        for (int level = 0; cardLevelEnumerator.MoveNext(); level++)
        {
            bool orient = false;
            if (level > 3) orient = true;

            //for each card at a level
            IEnumerator cardEnumerator = ((JSONArray)cardLevelEnumerator.Current).GetEnumerator();
            for (int i = 0; cardEnumerator.MoveNext(); i++)
            {
                cards.SetCard(orient, level % 3, i, (int)cardEnumerator.Current);
            }
        }

        //set nobles
        JSONArray noblesData = (JSONArray)boardData["nobles"];
        IEnumerator nobleEnumerator = noblesData.GetEnumerator();

        nobles.SetSize(noblesData.Count);
        for (int i = 0;  nobleEnumerator.MoveNext(); i++)
        {
            nobles.SetNoble((int)nobleEnumerator.Current, i);
        }

        //TO DO: set token bank

        //set players and their inventories
        IDictionary inventories = (IDictionary)boardData["inventories"];
        playerCount = inventories.Count;

        if (players[0].GetUsername().Equals("")) // if the players havent been set yet
        {
            IEnumerator usernames = ((ICollection)inventories.Keys).GetEnumerator();
            for (int i = 0; usernames.MoveNext(); i++) {
                players[i].SetUsername((string)usernames.Current);
            }

            //excess players should not be visible
            for (int i = playerCount - 1; i < players.Length; i++)
            {
                players[i].gameObject.SetActive(false);
            }
        }
        else
        {
            for (int i = 0; i < playerCount; i++) {
                players[i].ResetInventory();
            }
        }

        //fill inventories
        for (int i = 0; i < playerCount; i++) {
            IDictionary inventory = (IDictionary)inventories[players[i].GetUsername()];

            //set points 
            players[i].SetPoints((int)inventory["points"]);

            //set reserved cards
            IEnumerator reservedCards = ((JSONArray)inventory["reservedCards"]).GetEnumerator();
            while (reservedCards.MoveNext())
            {
                players[i].AddReservedCard(cards.cards.Find(x => x.id.Equals((int)reservedCards.Current)));
            }

            //set acquired cards
            IEnumerator acquiredCards = ((JSONArray)inventory["acquiredCards"]).GetEnumerator();
            while (acquiredCards.MoveNext())
            {
                players[i].AddAcquiredCard(cards.cards.Find(x => x.id.Equals((int)acquiredCards.Current)));
            }

            //set reserved nobles
            IEnumerator reservedNobles = ((JSONArray)inventory["reservedNobles"]).GetEnumerator();
            while (reservedNobles.MoveNext())
            {
                players[i].AddReservedNoble(nobles.allNobles.Find(x => x.id.Equals((int)reservedNobles.Current)));
            }

            //set acquired nobles
            IEnumerator acquiredNobles = ((JSONArray)inventory["acquiredNobles"]).GetEnumerator();
            while (acquiredNobles.MoveNext())
            {
                players[i].AddAcquiredNoble(nobles.allNobles.Find(x => x.id.Equals((int)acquiredNobles.Current)));
            }

            //TO DO: set token counts

            //TO DO: set bonus counts
        }
    }
}
