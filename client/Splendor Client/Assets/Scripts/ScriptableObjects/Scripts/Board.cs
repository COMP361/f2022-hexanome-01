using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

//TO DO: add token bank

[CreateAssetMenu]
public class Board : ScriptableObject
{
    private NobleRow nobles;
    private AllCards cards;
    private List<Player> players;

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

        nobles.SetNobleSize(noblesData.Count);
        for (int i = 0;  nobleEnumerator.MoveNext(); i++)
        {
            nobles.SetNoble((int)nobleEnumerator.Current, i);
        }

        //set tokens

        //set inventories

            //set token counts

            //set bonus counts

            //set points

            //set reserved cards

            //set acquired cards

            //set reserved nobles

            //set acquired nobles
    }
}
