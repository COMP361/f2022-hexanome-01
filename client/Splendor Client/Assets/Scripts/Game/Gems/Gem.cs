using System.Collections;
using System.Collections.Generic;
using UnityEngine;

// Gem is the same thing as token (the in game currency)
// The name "gem" is to differentiate the "token" from the one used for users

[CreateAssetMenu]
public class Gem : ScriptableObject{
    [SerializeField] public int amount;
    [SerializeField] public string colour;
    public Sprite sprite;

    public void setAmount(int n){
        amount = n;
    }

    public void decreaseAmount(int n){
        amount -= n;
    }

    public void increaseAmount(int n){
        amount += n;
    }
    
}
