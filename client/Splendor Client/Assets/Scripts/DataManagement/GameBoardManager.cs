using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public static class GameBoardManager
{


    private static Dictionary<int, Card> cardsById;
    private static Dictionary<int, Noble> noblesById;


    static Loader(){
        Load();

    }

    private static void Load()
    {
        // Load the card prefabs from the Resources folder
        Card[] cardPrefabs = Resources.LoadAll<Card>("Cards");
        Noble[] noblePrefabs = Resources.LoadAll<Noble>("Nobles");

        // Initialize the cardsById dictionary
        cardsById = new Dictionary<int, Card>();
        foreach (Card cardPrefab in cardPrefabs) {
            string cardName = cardPrefab.name;
            int cardId = int.Parse(cardName.Replace("Card", ""));
            cardsById.Add(cardId, cardPrefab);
        }

        // Initialize the noblesById dictionary
        noblesById = new Dictionary<int, Noble>();
        foreach (Noble noblePrefab in noblePrefabs) {
            string nobleName = noblePrefab.name;
            int nobleId = int.Parse(nobleName.Replace("Noble", ""));
            noblesById.Add(nobleId, noblePrefab);
        }
    }

    public static Card[][] GetCardArray(int[][] idArray)
    {
        int numRows = idArray.Length;
        Card[][] cardArray = new Card[numRows][];
        for (int i = 0; i < numRows; i++) {
            int[] idRow = idArray[i];
            int numCols = idRow.Length;
            Card[] cardRow = new Card[numCols];
            for (int j = 0; j < numCols; j++) {
                int id = idRow[j];
                if (cardsById.ContainsKey(id)) {
                    cardRow[j] = cardsById[id];
                } else {
                    Debug.LogError("Card ID not found: " + id);
                }
            }
            cardArray[i] = cardRow;
        }
        return cardArray;
    }

    public static Noble[] GetNobleArray(int[] idArray)
    {
        int numNobles = idArray.Length;
        Noble[] nobleArray = new Noble[numNobles];
        for (int i = 0; i < numNobles; i++) {
            int id = idArray[i];
            if (id == -1) {
                nobleArray[i] = null;
            } else if (noblesById.ContainsKey(id)) {
                nobleArray[i] = noblesById[id];
            } else {
                Debug.LogError("Noble ID not found: " + id);
            }
        }
        return nobleArray;
    }



}
