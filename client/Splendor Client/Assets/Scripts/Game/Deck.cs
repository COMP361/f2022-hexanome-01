using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System.Linq;

public class Deck : MonoBehaviour
{
    public int level;
    [SerializeField]public List<Card> cards = new List<Card>(); 
    //Takes the first card from the deck and remove it from list of cards 
    public Card DrawCard()
    {
        Card cardDrawn = cards[0];
        cards.Remove(cardDrawn);
        return cardDrawn;
    }
    //Shuffles the whole whole deck
    public void ShuffleDeck()
    {
        for (int i = 0; i < cards.Count; i++) {
        Card tempCard = cards[i];
        int random = Random.Range(i, cards.Count);
        cards[i] = cards[random];
        cards[random] = tempCard;
     }

    }
}
