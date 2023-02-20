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
}
