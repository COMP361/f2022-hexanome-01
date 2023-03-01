using System.Collections;
using System.Collections.Generic;
using System;
using UnityEngine;

public class CityRow : MonoBehaviour
{
    private CitySlot[] cities = new CitySlot[3];

    public List<City> allCities = new List<City>();
    [SerializeField] private GameObject cityObject;
    [SerializeField] private float x, y;
    
    public void SetCity(long id, int index)
    {
        GameObject prefab = Instantiate(cityObject, new Vector3(x, y + index * 0.98f, 0), Quaternion.identity);
        if (index < cities.Length)
        {
            cities[index] = prefab.GetComponent<CitySlot>();
            
            //check if city has been taken and therefore should be empty
            City toSet = null;
            if (id != -1)
                toSet = allCities.Find(x => x.id.Equals(id)); //find city with given id

            if (toSet == null)
                cities[index].EmptySlot(); //need to remove sprite if no city
            else
                cities[index].SetCity(toSet);
        }
    }

    public void RemoveCity(City city) {
        foreach (CitySlot citySlot in cities) {
            if (citySlot.GetCity().Equals(city)) {
                citySlot.EmptySlot();
            }
        }
    }

    public bool IsEmpty() {
        foreach (CitySlot citySlot in cities)
            if (citySlot) //if any city exists, return false
                return false;
        return true; //otherwise return true
    }
    
    public void GreyOutExcept(CitySlot _city)
    {
        for (int i = 0; i < cities.Length; i++) {
            if (cities[i] != _city) cities[i].GreyOut();
            else cities[i].UnGreyOut();
        }
    }

    public void GreyOut()
    {
        for (int i = 0; i < cities.Length; i++) cities[i].GreyOut();
    }

    public void UnGreyOut()
    {
        for (int i = 0; i < cities.Length; i++) cities[i].UnGreyOut();
    }

    public CitySlot GetCity(int index)
    {
        return cities[index];
    }

    public CitySlot[] GetAllCities(){
        return cities;
    }
}