using System.Collections;
using System.Collections.Generic;
using System;
using UnityEngine;

public class NobleRow : MonoBehaviour
{
    [SerializeField] private int size;
    public NobleDeck deck;

    public NobleSlot[] nobles = new NobleSlot[5];

    public float x;
    public float y;

    [SerializeField] private GameObject nobleObject;

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
                GameObject prefab = Instantiate(nobleObject, new Vector3(x + i*2, y, 0), Quaternion.identity);
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
                nobles[i] = null;
                // fillEmptyNobleSpot(i);
            }
        }
    }

    public NobleData[] ToArray()
    {
        NobleData[] nobleData = new NobleData[5];
        for(int i = 0; i < nobles.Length; i++){
            if (nobles[i] == null) nobleData[i] = null;
            else nobleData[i] = new NobleData(nobles[i].GetNoble());
        }   
        return nobleData;
    }

    // private void fillEmptyNobleSpot(int nobleIndex)
    // {
    //     GameObject prefab = Instantiate(nobleObject, new Vector3(x + nobleIndex*2, y, 0), Quaternion.identity);
    //     nobles[nobleIndex] = prefab.GetComponent<NobleSlot>();
    //     nobles[nobleIndex].SetNoble(deck.DrawNoble());
    // }

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
