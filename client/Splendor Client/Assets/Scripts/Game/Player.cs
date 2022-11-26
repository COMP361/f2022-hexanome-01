using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Player : MonoBehaviour
{
    private int pointsTotal = 0;
    //This will be a list of the ids of the purchased player cards
    public List<Card> inventory = new List<Card>();
    public List<Noble> noblesVisited = new List<Noble>();
    public CardGemValue totalGemsAquired = new CardGemValue();

    public int GetPoints()
    {
        return pointsTotal;
    }

    public void TriggerCardAdd(Card cardObject)
    {
        Card tempCard = (Card) ScriptableObject.CreateInstance(typeof(Card));
        tempCard = cardObject;

        pointsTotal += cardObject.GetPoints();

        if(cardObject != null)
        totalGemsAquired.AddGemsToInventory(tempCard);

        inventory.Add(cardObject);
    }

    public void TriggerNobleAdd(Noble nobleObject)
    {
        pointsTotal += nobleObject.GetPoints();
        noblesVisited.Add(nobleObject);
    }



    public CardGemValue GetTotalGemsAquired(){
        return totalGemsAquired;
    }

    public bool hasImpressed(Noble nobleToImpress){

    //    Debug.Log("Noble Value Red: " + nobleToImpress.nobleValue.red);
    //    Debug.Log("Player Red: " + totalGemsAquired.red);
    //    Debug.Log("Noble Value Green: " + nobleToImpress.nobleValue.green);
    //    Debug.Log("Player Green: " + totalGemsAquired.green);
    //    Debug.Log("Noble Value Blue: " + nobleToImpress.nobleValue.blue);
    //    Debug.Log("Player Blue: " + totalGemsAquired.blue);
    //    Debug.Log("Noble Value Brown: " + nobleToImpress.nobleValue.brown);
    //    Debug.Log("Player Brown: " + totalGemsAquired.brown);
    //    Debug.Log("Noble Value White: " + nobleToImpress.nobleValue.white);
    //    Debug.Log("Player White: " + totalGemsAquired.white);

       return (totalGemsAquired.red >= nobleToImpress.nobleValue.red 
       && totalGemsAquired.green >= nobleToImpress.nobleValue.green 
       && totalGemsAquired.blue >= nobleToImpress.nobleValue.blue 
       && totalGemsAquired.brown >= nobleToImpress.nobleValue.brown 
       && totalGemsAquired.white >= nobleToImpress.nobleValue.white);

    }


}
