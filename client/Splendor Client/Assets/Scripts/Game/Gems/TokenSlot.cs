using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using System;
using UnityEngine.Events;

public class TokenSlot : MonoBehaviour
{
    public Gem token;
    public Text amount;
    public Image image;
    [SerializeField] private SelectedTokens selectedTokens;

    public Gem get(){
        return token;
    }

    // Start is called before the first frame update
    void Start()
    {
        //initialises the displayed amount text to amount given to scriptableobject
        amount.text = token.amount.ToString();
    }

    // Update is called once per frame
    void Update()
    {
        amount.text = token.amount.ToString();
    }

}
