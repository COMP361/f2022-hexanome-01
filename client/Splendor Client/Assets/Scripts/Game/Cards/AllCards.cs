using System.Collections;
using System.Collections.Generic;
using System;
using UnityEngine;

public class AllCards : MonoBehaviour
{
    [SerializeField] public CardSlot[][] baseCards = new CardSlot[3][];
    [SerializeField] public CardSlot[][] orientCards = new CardSlot[3][];

    void Start() {
        for (int level = 0; level < 3; level++)
        {
            //base cards
            baseCards[level] = new CardSlot[4];

            //orient cards
            orientCards[level] = new CardSlot[2];
        }
    }

    public void GreyOutExcept(CardSlot _card)
    {
        //by level
        for (int level = 0; level < 3; level++) {
            //base cards
            for (int i = 0; i < baseCards[level].Length; i++)
            {
                if (!baseCards[level][i].Equals(_card))
                {
                    baseCards[level][i].GreyOut();
                }
            }

            //orient cards
            for (int i = 0; i < orientCards[level].Length; i++)
            {
                if (!orientCards[level][i].Equals(_card))
                {
                    orientCards[level][i].GreyOut();
                }
            }
        }
    }

    public void GreyOut()
    {
        //by level
        for (int level = 0; level < 3; level++)
        {
            //base cards
            for (int i = 0; i < baseCards[level].Length; i++)
            {
                baseCards[level][i].GreyOut();
            }

            //orient cards
            for (int i = 0; i < orientCards[level].Length; i++)
            {
                orientCards[level][i].GreyOut();
            }
        }
    }

    public void UnGreyOut()
    {
        //by level
        for (int level = 0; level < 3; level++)
        {
            //base cards
            for (int i = 0; i < baseCards[level].Length; i++)
            {
                baseCards[level][i].UnGreyOut();
            }

            //orient cards
            for (int i = 0; i < orientCards[level].Length; i++)
            {
                orientCards[level][i].UnGreyOut();
            }
        }
    }

    public void RemoveCard(CardSlot cardToRemove)
    {
        //by level
        for (int level = 0; level < 3; level++)
        {
            //base cards
            for (int i = 0; i < baseCards[level].Length; i++)
            {
                if (baseCards[level][i].Equals(cardToRemove))
                {
                    Destroy(baseCards[level][i].gameObject);
                    return;
                }
            }

            //orient cards
            for (int i = 0; i < orientCards[level].Length; i++)
            {
                if (orientCards[level][i].Equals(cardToRemove))
                {
                    Destroy(orientCards[level][i].gameObject);
                    return;
                }
            }
        }
    }

    public void RemoveCard(Card cardToRemove) {
        //by level
        for (int level = 0; level < 3; level++)
        {
            //base cards
            for (int i = 0; i < baseCards[level].Length; i++)
            {
                if (baseCards[level][i].GetCard().Equals(cardToRemove))
                {
                    Destroy(baseCards[level][i].gameObject);
                    return;
                }
            }

            //orient cards
            for (int i = 0; i < orientCards[level].Length; i++)
            {
                if (orientCards[level][i].GetCard().Equals(cardToRemove))
                {
                    Destroy(orientCards[level][i].gameObject);
                    return;
                }
            }
        }
    }

    public CardSlot GetCard(bool orient, int level, int index)
    {
        if (orient) return orientCards[level][index];
        else return baseCards[level][index];
    }

}
