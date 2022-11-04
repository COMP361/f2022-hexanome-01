using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Player : MonoBehaviour
{
    private int pointsTotal = 0;
    //This will be a list of the ids of the purchased player cards
    public List<Card> inventory = new List<Card>();
    public List<Noble> noblesVisited = new List<Noble>();

    public int GetPoints()
    {
        return pointsTotal;
    }

    public void TriggerCardAdd(Card cardObject)
    {
        pointsTotal += cardObject.GetPoints();
        inventory.Add(cardObject);
    }

    public void TriggerNobleAdd(Noble nobleObject)
    {

        pointsTotal += nobleObject.GetPoints();
        noblesVisited.Add(nobleObject);
    }

    public int GetRed(){
        int _red = 0;
        foreach(Card card in this.inventory){
            _red += card.red;
        }
        return _red;
    }
    public int GetGreen(){
        int _green = 0;
        foreach(Card card in this.inventory){
            _green += card.green;
        }
        return _green;
    }
    public int GetBlue(){
        int _blue = 0;
        foreach(Card card in this.inventory){
            _blue += card.blue;
        }
        return _blue;
    }
    public int GetBrown(){
        int _brown = 0;
        foreach(Card card in this.inventory){
            _brown += card.brown;
        }
        return _brown;
    }
    public int GetWhite(){
        int _white = 0;
        foreach(Card card in this.inventory){
            _white += card.white;
        }
        return _white;
    }

}
