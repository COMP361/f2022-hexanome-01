using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class TokenBank : MonoBehaviour{
    public List<Gem> tokens = new List<Gem>();
    public int gold;
    public int blue;
    public int green;
    public int black;
    public int red;
    public int white;

    public void set(int n){
        gold = 5;
        blue = n;
        green = n;
        black = n;
        red = n;
        white = n;
        foreach (Gem gem in tokens){
            if (gem.colour != "gold"){
                gem.amount = n;
            }
            else{gem.amount = 5;}
        }
    }

    //hard coded token amount; the real amount should be different according to number of players
    //gold token is always 5 regardless of number of players
    void Start(){
        foreach (Gem gem in tokens){
            if (gem.colour != "gold"){
                gem.amount = 7;
            }
            else{gem.amount = 5;}
        }
        gold = 5;
        blue = 7;
        green = 7;
        black = 7;
        red = 7;
        white = 7;
    }
}
