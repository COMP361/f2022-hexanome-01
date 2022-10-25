using System.Collections;
using System.Collections.Generic;
using System;
using UnityEngine;

public class CardRow : MonoBehaviour
{
    [SerializeField] private int size;
    [SerializeField] private int level;

    private Card[] cards = new Card[5];

    public float x;
    public float y;

    [SerializeField] private GameObject cardObject;

    public void GreyOutExcept(int _level, int _index)
    {
        if (level != _level) for (int i=0; i<size; i++) cards[i].GreyOut();
        else for (int i=0; i<size; i++) {
            if (i != _index) cards[i].GreyOut();
            else cards[i].UnGreyOut();
        }
    }

    public void GreyOutExcept(Card _card)
    {
        for (int i=0; i<size; i++) {
            if (cards[i] != _card) cards[i].GreyOut();
            else cards[i].UnGreyOut();
        }
    }

    void FillEmptyCards()
    {
        for (int i=0; i<size; i++) {
            if (cards[i] == null) {
                GameObject prefab = Instantiate(cardObject, new Vector3(x + i*2, y, 0), Quaternion.identity);
                cards[i] = prefab.GetComponent<Card>();
            }
        }
    }

    void Start()
    {
        size = Math.Min(size, 5);
        FillEmptyCards();
    }

}
