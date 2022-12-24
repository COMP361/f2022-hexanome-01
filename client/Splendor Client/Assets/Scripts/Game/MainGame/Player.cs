using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Player : MonoBehaviour
{
    private int pointsTotal = 0;
    //This will be a list of the ids of the purchased player cards
    public List<Card> inventory = new List<Card>(), cardReserves = new List<Card>();
    public List<Noble> noblesVisited = new List<Noble>(), nobleReserves = new List<Noble>();
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
        tokensAquired.gold = 5;
    }

    public int GetPoints()
    {
        return pointsTotal;
    }

    public bool ReserveCard(Card card) { //TODO: keep track of total gold tokens - if none left in bank, dont get a gold token when reserving. also, can only have more than 3 reserves
        if (cardReserves.Count <= 3) {
            cardReserves.Add(card);
            tokensAquired.gold++; //*******change******//
            for (int i = 0; i < turnData.cardTaken.Length; i++) {
                if (turnData.cardTaken[i] != null) {
                    turnData.cardTaken[i] = new CardData(card);
                    break;
                }
            }
            return true;
        }
        else
            return false;
    }

    public void ReserveNoble(Noble noble) {
        nobleReserves.Add(noble);
        for (int i = 0; i < turnData.nobleTaken.Length; i++) {
            if (turnData.nobleTaken[i] != null) {
                turnData.nobleTaken[i] = new NobleData(noble);
                break;
            }
        }
    }

    public void AcquireCard(Card card) { //add a card (for free) to player inventory
        pointsTotal += card.GetPoints(); //increase player point total
        bonusesAquired.AddGemsToInventory(card); //add gem discount to player info
        inventory.Add(card); //add card to inventory
        for (int i = 0; i < turnData.cardTaken.Length; i++) {
            if (turnData.cardTaken[i] != null) {
                turnData.cardTaken[i] = new CardData(card);
                break;
            }
        }
    }

    public void RemoveCard(Card card) {
        pointsTotal -= card.GetPoints(); //increase player point total
        bonusesAquired.RemoveGemsFromInventory(card); //add gem discount to player info
        inventory.Remove(card); //add card to inventory
    }

    public bool TriggerCardAdd(Card cardObject) {//need to account for gold tokens/gold discounts. also maybe include an error message somewhere to indicate you tried to buy a card but didnt have enough $$$
        if (cardObject && CardGemValue.combine(bonusesAquired, tokensAquired).CheckSufficientPay(cardObject)) { //if card exists and player has enough gems to purchase, purchase it
            tokensAquired.PayFor(cardObject); //pay for card
            AcquireCard(cardObject);
            return true;
        }
        else
            return false;
    }

    public void TriggerNobleAdd(Noble nobleObject)
    {
        pointsTotal += nobleObject.GetPoints();
        noblesVisited.Add(nobleObject);
        for (int i = 0; i < turnData.cardTaken.Length; i++) {
            if (turnData.nobleTaken[i] != null) {
                turnData.nobleTaken[i] = new NobleData(nobleObject);
                break;
            }
        }
    }

    public CardGemValue GetTokensAquired(){
        return tokensAquired;
    }

    public bool hasImpressed(Noble nobleToImpress){ //take into account satchels
       return (bonusesAquired.red >= nobleToImpress.nobleValue.red 
       && bonusesAquired.green >= nobleToImpress.nobleValue.green 
       && bonusesAquired.blue >= nobleToImpress.nobleValue.blue 
       && bonusesAquired.brown >= nobleToImpress.nobleValue.brown 
       && bonusesAquired.white >= nobleToImpress.nobleValue.white);

    }


}
