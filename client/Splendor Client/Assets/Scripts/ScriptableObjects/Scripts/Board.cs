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
    private Player[] players = new Player[4];
    private string currentPlayer;

    private int playerCount;
    [SerializeField] private Authentication mainPlayer;

    public void launch(string host, string id) {
        //get board data & set it
        GameRequestManager.instance.launch(host, id, SetBoard);
    }

    public void SetBoard(JSONObject boardData) {
        //STEP 1: set current player
        currentPlayer = (string)boardData["currentPlayer"];

        //STEP 2: set cards
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

        //STEP 3: set nobles
        JSONArray noblesData = (JSONArray)boardData["nobles"];
        IEnumerator nobleEnumerator = noblesData.GetEnumerator();

        nobles.SetSize(noblesData.Count);
        for (int i = 0;  nobleEnumerator.MoveNext(); i++)
        {
            nobles.SetNoble((int)nobleEnumerator.Current, i);
        }

        //TO DO: STEP 4: set token bank

        //STEP 5: set players and their inventories
        IDictionary inventories = (IDictionary)boardData["inventories"];
        playerCount = inventories.Count;

        //set players if they havent yet been set
        if (players[0] == null)
        {
            players[0] = GameObject.Find("Main Player").GetComponent<Player>();
            players[0].SetUsername(mainPlayer.username);
            players[1] = GameObject.Find("Player 2").GetComponent<Player>();
            players[2] = GameObject.Find("Player 3").GetComponent<Player>();
            players[3] = GameObject.Find("Player 4").GetComponent<Player>();

            IEnumerator usernames = ((ICollection)inventories.Keys).GetEnumerator();

            for (int i = 1; usernames.MoveNext(); i++)
            {
                string username = (string)usernames.Current;
                if (!username.Equals(mainPlayer.username))
                {
                    players[i].SetUsername(username);
                }
            }

            //excess players should not be visible
            for (int i = playerCount - 1; i < players.Length; i++)
            {
                players[i].gameObject.SetActive(false);
            }
        }

        //reset all inventories
        for (int i = 0; i < playerCount; i++) {
            players[i].ResetInventory();
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
