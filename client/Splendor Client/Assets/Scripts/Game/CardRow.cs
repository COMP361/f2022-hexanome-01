using System.Collections;
using System.Collections.Generic;
using System;
using UnityEngine;

public class CardRow : MonoBehaviour
{
    [SerializeField] private int size;
    [SerializeField] private int level;
    public Deck deck;

    private CardSlot[] cards = new CardSlot[4];

    public float x;
    public float y;

    [SerializeField] private GameObject cardObject;

    public void GreyOutExcept(int _level, int _index) // Unused secondary implementation
    {
        if (level != _level) for (int i=0; i<size; i++) cards[i].GreyOut();
        else for (int i=0; i<size; i++) {
            if (i != _index) cards[i].GreyOut();
            else cards[i].UnGreyOut();
        }
    }

    public void GreyOutExcept(CardSlot _card)
    {
        for (int i=0; i<size; i++) {
            if (cards[i] != _card) cards[i].GreyOut();
            else cards[i].UnGreyOut();
        }
    }

    public void GreyOut()
    {
        for (int i=0; i<size; i++) cards[i].GreyOut();
    }

    public void UnGreyOut()
    {
        for (int i=0; i<size; i++) cards[i].UnGreyOut();
    }

    void FillEmptyCards()
    {
        deck.ShuffleDeck();
        for (int i=0; i<size; i++) {
            if (cards[i] == null) {
                GameObject prefab = Instantiate(cardObject, new Vector3(x + i*2, y, 0), Quaternion.identity);
                cards[i] = prefab.GetComponent<CardSlot>();
                cards[i].SetCard(deck.DrawCard());
            }
        }
    }

    public void RemoveCard(CardSlot cardToRemove)
    {
        for(int i = 0; i < cards.Length; i++)
        {
            if(cards[i] == cardToRemove)
            {
                Destroy(cards[i].gameObject);
                fillEmptyCardSpot(i);
            }
        }
    }

    private void fillEmptyCardSpot(int cardIndex)
    {
        GameObject prefab = Instantiate(cardObject, new Vector3(x + cardIndex*2, y, 0), Quaternion.identity);
        cards[cardIndex] = prefab.GetComponent<CardSlot>();
        cards[cardIndex].SetCard(deck.DrawCard());
    }

    public CardSlot GetCard(int cardIndex)
    {
        return cards[cardIndex];
    }

    void Start()
    {
        size = Math.Min(size, 4);
        FillEmptyCards();
    }

}
