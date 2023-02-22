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
        tokensAquired.blue = 50; // Hardcode for demo only; REMOVE FOR PROD
        tokensAquired.green = 50;
        tokensAquired.brown = 50;
        tokensAquired.red = 50;
        tokensAquired.white = 50;
        tokensAquired.gold = 50;
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
        if (card.action != ActionType.SATCHEL && card.action != ActionType.DOMINO1) //dont add satchel cards to inventory, just to save space and less info overload
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
        bonusesAquired.RemoveGemsFromInventory(card); //remove normal bonus
        bonusesAquired.ChangeGemAmount(card.GetBonus(), -card.satchels); //remove satchel-induced bonus
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

    public void TakeTokens(List<Gem> tokens){
        char tempColour;
        foreach (Gem token in tokens){
            if (token.colour != "none"){
                if (token.colour == "black"){
                    tempColour = 'K';
                }
                else{tempColour = char.ToUpper(token.colour[0]);}
                tokensAquired.ChangeGemAmount(tempColour, token.amount);
            }
        }
    }

}
