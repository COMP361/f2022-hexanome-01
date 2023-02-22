using System.Collections;
using System.Collections.Generic;
using System;
using UnityEngine;

public class NobleRow : MonoBehaviour
{
    [SerializeField] private NobleSlot[] nobles = new NobleSlot[5];
    public int size;

    public static List<Noble> allNobles = new List<Noble>();
    [SerializeField] private GameObject nobleObject;
    [SerializeField] private float x;
    [SerializeField] private float y2Players;
    [SerializeField] private float y3Players;
    [SerializeField] private float y4Players;

    public void SetSize(int size) {
        this.size = size;
    }

    public int GetSize() {
        return size;
    }

    public void SetNoble(int id, int index) {
        float y = y2Players;
        if (nobles.Length == 4) y = y3Players;
        if (nobles.Length == 5) y = y4Players;

        GameObject prefab = Instantiate(nobleObject, new Vector3(x, y + index * 0.98f, 0), Quaternion.identity);
        nobles[index] = prefab.GetComponent<NobleSlot>();

        //check if noble has been taken and therefore should be empty
        Noble toSet = null;
        if (id != -1)
            toSet = allNobles.Find(x => x.id.Equals(id)); //find noble with given id

        if (toSet == null)
            nobles[index].EmptySlot(); //still need to remove the noble sprite if we cant find the right noble or its meant to be empty
        else
            nobles[index].SetNoble(toSet);
    }

    public bool IsEmpty() {
        foreach (NobleSlot ns in nobles)
            if (ns) //if any noble exists, return false
                return false;
        return true; //otherwise return false
    }
    public void GreyOutExcept(NobleSlot _noble)
    {
        for (int i = 0; i < nobles.Length; i++) {
            if (nobles[i] != _noble) nobles[i].GreyOut();
            else nobles[i].UnGreyOut();
        }
    }

    public void GreyOut()
    {
        for (int i = 0; i < nobles.Length; i++) nobles[i].GreyOut();
    }

    public void UnGreyOut()
    {
        for (int i = 0; i < nobles.Length; i++) nobles[i].UnGreyOut();
    }

    public NobleSlot GetNoble(int nobleIndex)
    {
        return nobles[nobleIndex];
    }

    public NobleSlot[] GetAllNobels(){
        return nobles;
    }
}
