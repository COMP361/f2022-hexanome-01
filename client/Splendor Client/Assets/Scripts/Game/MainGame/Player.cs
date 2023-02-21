using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Player : MonoBehaviour
{
    private string username;
    private int points = 0;
    //This will be a list of the ids of the purchased player cards
    private List<Card> cardsAcquired = new List<Card>(), cardsReserved = new List<Card>();
    private List<Noble> noblesAcquired = new List<Noble>(), noblesReserved = new List<Noble>();
    //TO DO: add tokens
    //TO DO: add bonuses

    public int GetPoints()
    {
        return points;
    }

    public List<Card> GetCardsAcquired() {
        return cardsAcquired;
    }

    public List<Noble> GetNoblesAcquired() {
        return noblesAcquired;
    }
}
