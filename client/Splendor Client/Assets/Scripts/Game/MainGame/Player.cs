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
    [SerializeField] private TokenBank tokensAcquired;
    [SerializeField] private TokenBank bonusesAcquired;
    [SerializeField] private GameObject citySlot, tradingPostSlots, tradingPostA, tradingPostB, tradingPostC, tradingPostD, tradingPostE;

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
        Dashboard dashboard = this.GetComponent<Dashboard>();
        if (dashboard != null){
            dashboard.resetReservedCardDisplay();}
        MultiplayerInfoPanel infoPanel = this.GetComponent<MultiplayerInfoPanel>();
        if (infoPanel != null)
            infoPanel.UpdateReservedCardsCount(0);
        // foreach (TradingPostSlot tradingPost in acquiredTradingPosts)
        // {
        //     tradingPost.gameObject.SetActive(false);
        // }
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

    public List<Card> GetReservedCards() {
        return reservedCards;
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
        foreach (Gem token in tokens){
            if (token.colour != "none"){
                tokensAcquired.AddAmount(token.colour, token.amount);
            }
        }
    }

	public TokenBank GetTokenBank() {
		return tokensAcquired;
	}

	public TokenBank GetBonusBank() {
		return bonusesAcquired;
	}

    public bool RemoveCard(Card card) {
        acquiredCards.Remove(card);
        return true;
    }

    public List<Noble> GetReservedNobles() {
        return reservedNobles;
    }

    public void AddCity(City city)
    {
        Dashboard dashboard = this.GetComponent<Dashboard>();
        if (dashboard != null)
            dashboard.UpdateAcquiredCityDisplay(city.sprite);
        else
        {
            MultiplayerInfoPanel infoPanel = this.GetComponent<MultiplayerInfoPanel>();
            if (infoPanel != null)
                infoPanel.UpdateAcquiredCityDisplay(city.sprite);
        }
    }

	public GameObject GetCitySlot() {
		return citySlot;
	}

    public void AddTradingPost(string tradingPost)
    {
        switch (tradingPost)
        {
            case "A": tradingPostA.SetActive(true); break;
            case "B": tradingPostB.SetActive(true); break;
            case "C": tradingPostC.SetActive(true); break;
            case "D": tradingPostD.SetActive(true); break;
            case "E": tradingPostE.SetActive(true); break;
        }
    }

	public GameObject GetTradingPostSlots() {
		return tradingPostSlots;
	}
}
