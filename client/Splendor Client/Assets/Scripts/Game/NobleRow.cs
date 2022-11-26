using System.Collections;
using System.Collections.Generic;
using System;
using UnityEngine;

public class NobleRow : MonoBehaviour
{
    [SerializeField] private int size;
    [SerializeField] private int level;
    public NobleDeck deck;

    private NobleSlot[] nobles = new NobleSlot[5];

    public float x;
    public float y;

    [SerializeField] private GameObject nobleObject;

    public void GreyOutExcept(int _level, int _index) // Unused secondary implementation
    {
        if (level != _level) for (int i=0; i<size; i++) nobles[i].GreyOut();
        else for (int i=0; i<size; i++) {
            if (i != _index) nobles[i].GreyOut();
            else nobles[i].UnGreyOut();
        }
    }

    public void GreyOutExcept(NobleSlot _noble)
    {
        for (int i=0; i<size; i++) {
            if (nobles[i] != _noble) nobles[i].GreyOut();
            else nobles[i].UnGreyOut();
        }
    }

    public void GreyOut()
    {
        for (int i=0; i<size; i++) nobles[i].GreyOut();
    }

    public void UnGreyOut()
    {
        for (int i=0; i<size; i++) nobles[i].UnGreyOut();
    }

    void FillEmptyNobles()
    {
        deck.ShuffleDeck();
        for (int i=0; i<size; i++) {
            if (nobles[i] == null) {
                GameObject prefab = Instantiate(nobleObject, new Vector3(x + i*2, 3, 0), Quaternion.identity);
                nobles[i] = prefab.GetComponent<NobleSlot>();
                nobles[i].SetNoble(deck.DrawNoble());
            }
        }
    }

    public void RemoveNoble(NobleSlot nobleToRemove)
    {
        for(int i = 0; i < nobles.Length; i++)
        {
            if(nobles[i] == nobleToRemove)
            {
                Destroy(nobles[i].gameObject);
                fillEmptyNobleSpot(i);
            }
        }
    }

    private void fillEmptyNobleSpot(int nobleIndex)
    {
        GameObject prefab = Instantiate(nobleObject, new Vector3(x + nobleIndex*3, y, 0), Quaternion.identity);
        nobles[nobleIndex] = prefab.GetComponent<NobleSlot>();
        nobles[nobleIndex].SetNoble(deck.DrawNoble());
    }

    public NobleSlot GetNoble(int nobleIndex)
    {
        return nobles[nobleIndex];
    }

    public NobleSlot[] GetAllNobels(){
        return nobles;
    }

    void Start()
    {
        size = Math.Min(size, 5);
        FillEmptyNobles();
        GreyOut();
    }
}
