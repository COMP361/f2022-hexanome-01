using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Player : MonoBehaviour
{
    private string username;
    private int points = 0;
    private List<Card> acquiredCards = new List<Card>(), reservedCards = new List<Card>();
    private List<Noble> acquiredNobles = new List<Noble>(), reservedNobles = new List<Noble>();
    //TO DO: add tokens
    //TO DO: add bonuses

    public void SetUsername(string username) {
        this.username = username;
    }

    public string GetUsername() {
        return username;
    }

    public void SetPoints(int points) {
        this.points = points;
    }

    public int GetPoints()
    {
        return points;
    }

    public void ResetInventory()
    {
        acquiredCards = new List<Card>();
        reservedCards = new List<Card>();
        acquiredNobles = new List<Noble>();
        reservedNobles = new List<Noble>();
    }

    //find the correct card by id and add it to the reserved cards
    public void SetReservedCards(JSONArray ids)
    {
        IEnumerator idEnumerator = ids.GetEnumerator();

        while (idEnumerator.MoveNext()) {
            reservedCards.Add(AllCards.cards.Find(x => x.id.Equals((int)idEnumerator.Current))); 
        }
    }

    //find the correct card by id and add it to the acquired cards
    public void SetAcquiredCards(JSONArray ids)
    {
        IEnumerator idEnumerator = ids.GetEnumerator();

        while (idEnumerator.MoveNext())
        {
            acquiredCards.Add(AllCards.cards.Find(x => x.id.Equals((int)idEnumerator.Current)));
        }
    }

    //find the correct noble by id and add it to the reserved nobles
    public void SetReservedNobles(JSONArray ids)
    {
        IEnumerator idEnumerator = ids.GetEnumerator();

        while (idEnumerator.MoveNext())
        {
            reservedNobles.Add(NobleRow.allNobles.Find(x => x.id.Equals((int)idEnumerator.Current)));
        }
    }

    //find the correct noble by id and add it to the acquired nobles
    public void SetAcquiredNobles(JSONArray ids)
    {
        IEnumerator idEnumerator = ids.GetEnumerator();

        while (idEnumerator.MoveNext())
        {
            acquiredNobles.Add(NobleRow.allNobles.Find(x => x.id.Equals((int)idEnumerator.Current)));
        }
    }

    public List<Card> GetAcquiredCards() {
        return acquiredCards;
    }

    public List<Noble> GetAcquiredNobles() {
        return acquiredNobles;
    }

    public void Reset() {
        username = "";
        points = 0;
        acquiredCards = null;
        reservedCards = null;
        acquiredNobles = null;
        reservedNobles = null;
    }
}
