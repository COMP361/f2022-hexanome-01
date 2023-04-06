using System.Collections;
using System.Collections.Generic;
using System;
using UnityEngine;

public class AllCards : MonoBehaviour
{
    private CardSlot[][] baseCards =
    {
        new CardSlot[]{null, null, null, null},
        new CardSlot[]{null, null, null, null},
        new CardSlot[]{null, null, null, null}
    };
    private CardSlot[][] orientCards =
    {
        new CardSlot[]{null, null},
        new CardSlot[]{null, null},
        new CardSlot[]{null, null}
    };

    public List<Card> cards = new List<Card>();
    [SerializeField] private GameObject cardObject;
    [SerializeField] private float xBase;
    [SerializeField] private float xOrient;
    [SerializeField] private float yLevel1;
    [SerializeField] private float yLevel2;
    [SerializeField] private float yLevel3;

    private GameObject[][] basePrefabs =
    {
       new GameObject[]{null, null, null, null},
       new GameObject[]{null, null, null, null},
       new GameObject[]{null, null, null, null}
    };

    private GameObject[][] orientPrefabs =
    {
        new GameObject[] { null, null },
        new GameObject[] { null, null },
        new GameObject[] { null, null }
    };

    public void GreyOutExcept(CardSlot _card)
    {
        UnGreyOut();
        //by level
        for (int level = 0; level < 3; level++) {
            //base cards
            for (int i = 0; i < 4; i++)
            {
                if (!baseCards[level][i].Equals(_card))
                {
                    if (baseCards[level][i] != null) baseCards[level][i].GreyOut();
                }
            }

            //orient cards
            for (int i = 0; i < 2; i++)
            {
                if (!orientCards[level][i].Equals(_card))
                {
                    if (orientCards[level][i] != null) orientCards[level][i].GreyOut();
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
                if (baseCards[level][i] != null) baseCards[level][i].GreyOut();
            }

            //orient cards
            for (int i = 0; i < orientCards[level].Length; i++)
            {
                if (orientCards[level][i] != null) orientCards[level][i].GreyOut();
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
                if (baseCards[level][i] != null) baseCards[level][i].UnGreyOut();
            }

            //orient cards
            for (int i = 0; i < orientCards[level].Length; i++)
            {
                if (orientCards[level][i] != null) orientCards[level][i].UnGreyOut();
            }
        }
    }

    public void ResetAllCards()
    {
        for (int level = 0; level < 3; level++)
        {
            for (int index = 0; index < 4; index++)
            {
                Destroy(basePrefabs[level][index]);
            }

            for (int index = 0; index < 2; index++)
            {
                Destroy(orientPrefabs[level][index]);
            }
        }
    }

    public void SetCard(bool orient, int level, int index, long id)
    {
        float x = xBase;
        if (orient) x = xOrient;

        float y = yLevel1;
        if (level == 1) y = yLevel2;
        else if (level == 2) y = yLevel3;

        GameObject prefab = Instantiate(cardObject, new Vector3(x + index * 0.85F, y, 0), Quaternion.identity);
        if (orient) orientPrefabs[level][index] = prefab;
        else basePrefabs[level][index] = prefab;

        if (id != -1)
        {
            Card toSet = cards.Find(x => x.id.Equals(id)); //find card with given id


            if (orient)
            {
                orientCards[level][index] = prefab.GetComponent<CardSlot>();

                if (toSet == null)
                    orientCards[level][index]
                        .EmptySlot(); //still need to remove the last card sprite if we cant find the right card
                else
                    orientCards[level][index].SetCard(toSet);
            }
            else
            {
                baseCards[level][index] = prefab.GetComponent<CardSlot>();

                if (toSet == null)
                    baseCards[level][index]
                        .EmptySlot(); //still need to remove the last card sprite if we cant find the right card
                else
                    baseCards[level][index].SetCard(toSet);
            }
        }
        else
        {
            if (orient)
            {
                Destroy(orientPrefabs[level][index]);
            }
            else
            {
                Destroy(baseCards[level][index]);
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
                if (baseCards[level][i] != null && baseCards[level][i].Equals(cardToRemove))
                {
                    Destroy(baseCards[level][i].gameObject);
                    return;
                }
            }

            //orient cards
            for (int i = 0; i < orientCards[level].Length; i++)
            {
                if (orientCards[level][i] != null && orientCards[level][i].Equals(cardToRemove))
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
                if (baseCards[level][i] != null && baseCards[level][i].GetCard().Equals(cardToRemove))
                {
                    Destroy(baseCards[level][i].gameObject);
                    return;
                }
            }

            //orient cards
            for (int i = 0; i < orientCards[level].Length; i++)
            {
                if (orientCards[level][i] != null && orientCards[level][i].GetCard().Equals(cardToRemove))
                {
                    Destroy(orientCards[level][i].gameObject);
                    return;
                }
            }
        }
    }

    public Card GetCardFromId(long id){
        return cards.Find(x => x.id.Equals(id));
    }

}
