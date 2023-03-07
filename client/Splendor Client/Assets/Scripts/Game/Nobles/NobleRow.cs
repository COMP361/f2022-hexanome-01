using System.Collections;
using System.Collections.Generic;
using System;
using UnityEngine;

public class NobleRow : MonoBehaviour
{
    [SerializeField] private NobleSlot[] nobles = new NobleSlot[5];
    public int size;

    public List<Noble> allNobles = new List<Noble>();
    [SerializeField] private GameObject nobleObject;
    [SerializeField] private float x;
    [SerializeField] private float y2Players;
    [SerializeField] private float y3Players;
    [SerializeField] private float y4Players;

    private List<GameObject> prefabs = new List<GameObject>();

    public void SetSize(int size) {
        this.size = size;
    }

    public int GetSize() {
        return size;
    }

    public void ResetAllNobles()
    {
        foreach (var noble in prefabs) {
            Destroy(noble);
        }
    }

    public void SetNoble(long id, int index)
    {
        float y = y2Players;
        if (size == 4) y = y3Players;
        if (size == 5) y = y4Players;

        GameObject prefab = Instantiate(nobleObject, new Vector3(x, y + index * 0.98f, 0), Quaternion.identity);
        prefabs.Add(prefab);
        if (index < size)
        {
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
    }

    public void RemoveNoble(Noble noble) {
        foreach (NobleSlot nobleSlot in nobles) {
            if (nobleSlot.GetNoble().Equals(noble)) {
                nobleSlot.EmptySlot();
            }
        }
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