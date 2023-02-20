using System.Collections;
using System.Collections.Generic;
using System;
using UnityEngine;

public class NobleRow : MonoBehaviour
{
    [SerializeField] private int size;

    public NobleSlot[] nobles = new NobleSlot[5];

    public float x;
    public float y;

    [SerializeField] private GameObject nobleObject;

    public bool IsEmpty() {
        foreach (NobleSlot ns in nobles)
            if (ns) //if any noble exists, return false
                return false;
        return true; //otherwise return false
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

    public NobleSlot GetNoble(int nobleIndex)
    {
        return nobles[nobleIndex];
    }

    public NobleSlot[] GetAllNobels(){
        return nobles;
    }
}
