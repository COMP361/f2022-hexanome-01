using System.Collections;
using System.Collections.Generic;
using System;
using UnityEngine;

public class AllCards : MonoBehaviour
{
    [SerializeField] public CardRow[] cards = new CardRow[6];
    [SerializeField] private int rows;

    public void GreyOutExcept(CardSlot _card)
    {
        for (int level=0; level<rows; level++)
            cards[level].GreyOutExcept(_card);
    }

    public void GreyOut()
    {
        for (int level=0; level<rows; level++)
            cards[level].GreyOut();
    }

    public void UnGreyOut()
    {
        for (int level=0; level<rows; level++)
            cards[level].UnGreyOut();
    }

    public void RemoveCard(CardSlot cardToRemove)
    {
        for (int level=0; level<rows; level++)
            cards[level].RemoveCard(cardToRemove);
    }

    public void RemoveCard(Card cardToRemove) {
        for (int level = 0; level < rows; level++)
            cards[level].RemoveCard(cardToRemove);
    }

    public CardSlot GetCard(int rowIndex, int cardIndex)
    {
        return cards[rowIndex].GetCard(cardIndex);
    }

    void Start()
    {
        rows = Math.Min(rows, 6);
    }

}
