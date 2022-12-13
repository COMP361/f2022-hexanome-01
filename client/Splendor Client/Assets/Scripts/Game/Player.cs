using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Player : MonoBehaviour
{
    private int pointsTotal = 0;
    //This will be a list of the ids of the purchased player cards
    public List<Card> inventory = new List<Card>();
    public List<Noble> noblesVisited = new List<Noble>();
    public CardGemValue bonusesAquired = new CardGemValue();
    public CardGemValue tokensAquired = new CardGemValue();

    public Authentication mainPlayer;

    public TurnData turnData = new TurnData();

    void Start()
    {
        tokensAquired.blue = 5; // Hardcode for demo only; REMOVE FOR PROD
        tokensAquired.green = 5;
        tokensAquired.brown = 5;
        tokensAquired.red = 5;
        tokensAquired.white = 5;
    }

    public int GetPoints()
    {
        return pointsTotal;
    }

    public bool TriggerCardAdd(Card cardObject) //need to account for gold tokens
    {
        Card tempCard = (Card) ScriptableObject.CreateInstance(typeof(Card));
        tempCard = cardObject;

        if (!CardGemValue.combine(bonusesAquired, tokensAquired).CheckSufficientPay(tempCard)) return false;
        tokensAquired.PayFor(tempCard);

        pointsTotal += cardObject.GetPoints();

        if(cardObject != null)
            bonusesAquired.AddGemsToInventory(tempCard);

        inventory.Add(cardObject);
        turnData.cardTaken = new CardData(cardObject);

        return true;
    }

    public void TriggerNobleAdd(Noble nobleObject)
    {
        pointsTotal += nobleObject.GetPoints();
        noblesVisited.Add(nobleObject);
        turnData.nobleTaken = new NobleData(nobleObject);
    }

    public CardGemValue GetTokensAquired(){
        return tokensAquired;
    }

    public bool hasImpressed(Noble nobleToImpress){
       return (bonusesAquired.red >= nobleToImpress.nobleValue.red 
       && bonusesAquired.green >= nobleToImpress.nobleValue.green 
       && bonusesAquired.blue >= nobleToImpress.nobleValue.blue 
       && bonusesAquired.brown >= nobleToImpress.nobleValue.brown 
       && bonusesAquired.white >= nobleToImpress.nobleValue.white);

    }


}
