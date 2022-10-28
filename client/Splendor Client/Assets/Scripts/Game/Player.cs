using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Player : MonoBehaviour
{
    private int pointsTotal = 0;
    //This will be a list of the ids of the purchased player cards
    public List<Card> inventory = new List<Card>();

    public int GetPoints()
    {
        return pointsTotal;
    }

    public void TriggerCardAdd(Card cardObject)
    {
        pointsTotal += cardObject.GetPoints();
        inventory.Add(cardObject);
    }

}
