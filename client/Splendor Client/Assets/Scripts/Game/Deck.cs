using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System.Linq;
using System;

public class Deck : MonoBehaviour
{
    public int level;
    [SerializeField]public List<Card> cards = new List<Card>(); 
    //Takes the first card from the deck and remove it from list of cards 

    public int randomSeed;

    public Card DrawCard()
    {
        if (Count() == 0) {
            return null;
        }
        Card cardDrawn = cards[0];
        cards.Remove(cardDrawn);
        return cardDrawn;
    }

    public void ShuffleDeckWithSeed(int seed)
    {
        randomSeed = seed;
        
        var rng = new Random(seed);
        int n = cards.Count;

        while (n > 1)
        {
            n--;
            int k = rng.Next(n + 1);
            Card value = cards[k];
            cards[k] = cards[n];
            cards[n] = value;
        }

    }

    //Shuffles the whole whole deck
    public void ShuffleDeck()
    {
        Random random = new Random();
        randomSeed = random.Next();

        var rng = new Random(seed);
        int n = cards.Count;

        while (n > 1)
        {
            n--;
            int k = rng.Next(n + 1);
            Card value = cards[k];
            cards[k] = cards[n];
            cards[n] = value;
        }

    }

    public int Count()
    {
        return cards.Count;
    }

    public CardData[] ToArray()
    {
        CardData[] cardsData = new CardData[cards.Count];
        for(int i = 0; i < cards.Count; i++){
            cardsData[i] = new CardData(cards[i]);
        }   
        return cardsData;

    }

}
