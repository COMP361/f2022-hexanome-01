using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class Player : MonoBehaviour
{
    private string username;
    private long points = 0;
    private List<Card> acquiredCards = new List<Card>(), reservedCards = new List<Card>();
    private List<Noble> acquiredNobles = new List<Noble>(), reservedNobles = new List<Noble>();
    private CardGemValue tokensAcquired = new CardGemValue();
    public CardGemValue bonusesAquired = new CardGemValue();
    public UnlockableList unlockables;
    //TO DO: add bonuses

    private bool currentPlayer; //flag for whether the player is the current player
    [SerializeField] private GameObject turnIndicator, inventoryButton; //for displaying current player

    public void SetUsername(string username) {
        this.username = username;
        //update display
        MultiplayerInfoPanel infoPanel = this.GetComponent<MultiplayerInfoPanel>();
        if (infoPanel != null) {
            infoPanel.UpdatePlayerName(username);
        }
    }

    public string GetUsername() {
        return username;
    }

    public void SetPoints(long points) {
        this.points = points;
        //update display
        MultiplayerInfoPanel infoPanel = this.GetComponent<MultiplayerInfoPanel>();
        if (infoPanel != null)
            infoPanel.UpdatePlayerPoints(points);
        else
        {
            Dashboard dashboard = this.GetComponent<Dashboard>();
            if (dashboard != null)
                dashboard.UpdatePtsDisplay(points);
        }
    }

    public long GetPoints()
    {
        return points;
    }

    public void ResetInventory()
    {
        points = 0;
        acquiredCards = new List<Card>();
        reservedCards = new List<Card>();
        acquiredNobles = new List<Noble>();
        reservedNobles = new List<Noble>();
    }

    public void AddReservedCard(Card card)
    {
        reservedCards.Add(card);
        //update display
        Dashboard dashboard = this.GetComponent<Dashboard>();
        if (dashboard != null)
            dashboard.UpdateReserveCardDisplay(card.sprite, reservedCards.Count);
        else
        {
            MultiplayerInfoPanel infoPanel = this.GetComponent<MultiplayerInfoPanel>();
            if (infoPanel != null)
                infoPanel.UpdateReservedCardsCount(reservedCards.Count);
        }
    }

    public void AddAcquiredCard(Card card)
    {
        acquiredCards.Add(card);
    }

    public void AddReservedNoble(Noble noble)
    {
        reservedNobles.Add(noble);
        //update display
        Dashboard dashboard = this.GetComponent<Dashboard>();
        if (dashboard != null)
            dashboard.UpdateReserveNobleDisplay(noble.sprite, reservedNobles.Count);
        else
        {
            MultiplayerInfoPanel infoPanel = this.GetComponent<MultiplayerInfoPanel>();
            if (infoPanel != null)
                infoPanel.UpdateReservedNoblesCount(reservedNobles.Count);
        }
    }

    public void AddAcquiredNoble(Noble noble)
    {
        acquiredNobles.Add(noble);
    }

    public List<Card> GetAcquiredCards() {
        return acquiredCards;
    }

    public List<Noble> GetAcquiredNobles() {
        return acquiredNobles;
    }

    public void SetCurrentPlayer(bool setCurrent) {
        if (setCurrent && !currentPlayer) //set the player to the current player
        {
            currentPlayer = true;
            turnIndicator.SetActive(true);
            inventoryButton.GetComponent<Image>().color = new Color32(255, 253, 240, 255);
        }
        else if (!setCurrent && currentPlayer) //unset the player as the current player
        {
            currentPlayer = false;
            turnIndicator.SetActive(false);
            inventoryButton.GetComponent<Image>().color = new Color32(242, 236, 187, 255);
        }
    }

    public void Reset() {
        username = "";
        points = 0;
        acquiredCards = null;
        reservedCards = null;
        acquiredNobles = null;
        reservedNobles = null;
    }

    public void TakeTokens(List<Gem> tokens){
        char tempColour;
        foreach (Gem token in tokens){
            if (token.colour != "none"){
                if (token.colour == "black"){
                    tempColour = 'K';
                }
                else{tempColour = char.ToUpper(token.colour[0]);}
                tokensAcquired.ChangeGemAmount(tempColour, token.amount);
            }
        }
    }

    public bool ReserveCard(Card card)
    {
        reservedCards.Add(card);
        return true;
    }

    public bool ReserveNoble(Noble noble) {
        reservedNobles.Add(noble);
        return true;
    }

    public bool AcquireCard(Card card) {
        acquiredCards.Add(card);
        return true;
    }

    public bool RemoveCard(Card card) {
        acquiredCards.Remove(card);
        return true;
    }

    public List<Noble> GetReservedNobles() {
        return reservedNobles;
    }
}
