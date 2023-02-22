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
        //update display
        MultiplayerInfoPanel infoPanel = this.GetComponent<MultiplayerInfoPanel>();
        if (infoPanel != null) {
            infoPanel.UpdatePlayerName(username);
        }
    }

    public string GetUsername() {
        return username;
    }

    public void SetPoints(int points) {
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

    public int GetPoints()
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
    }

    public void AddAcquiredCard(Card card)
    {
        acquiredCards.Add(card);
    }

    public void AddReservedNoble(Noble noble)
    {
        reservedNobles.Add(noble);
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

    public void Reset() {
        username = "";
        points = 0;
        acquiredCards = null;
        reservedCards = null;
        acquiredNobles = null;
        reservedNobles = null;
    }
}
