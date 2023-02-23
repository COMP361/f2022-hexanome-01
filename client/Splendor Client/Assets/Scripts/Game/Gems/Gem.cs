using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using System;
using UnityEngine.Events;
using System.Runtime.InteropServices.WindowsRuntime;

// Gem is the same thing as token (the in game currency)
// The name "gem" is to differentiate the "token" from the one used for users

[CreateAssetMenu]
public class Gem : ScriptableObject{
    [SerializeField] public int amount;
    [SerializeField] public string colour;
    public Sprite sprite;
    private SpriteRenderer m_SpriteRenderer;
    [SerializeField] private Image image;

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
