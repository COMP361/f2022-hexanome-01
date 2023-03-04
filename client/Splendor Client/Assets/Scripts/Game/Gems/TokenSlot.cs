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
    [SerializeField] private TokenBank tokenBank;

    public void passToSelected(){
        /*for (int i =0; i<3; i++){
            if (selectedTokens.sTokens[i].colour == colour){
                if(selectedTokens.getNum()<=1)
                {selectedTokens.sTokens[i].amount += 1; token.amount -= 1;}
                break;
            }
            else if (selectedTokens.sTokens[i].colour == "none"){
                if (selectedTokens.getNum()<3)
                selectedTokens.sTokens[i].colour = colour;
                selectedTokens.colours[i].text = colour;
                selectedTokens.sTokens[i].amount += 1;
                token.amount -= 1;
                break;
            }
        } */
        //selectedTokens.SetActive();
        
        if (selectedTokens.addOne(token.colour)){
            //this token is the token in bank not selectedToken
            token.amount -= 1;
            tokenBank.removeOne(token.colour);
            this.amount.text = token.amount.ToString();
        }
        //tokenBank.removeOne(token.colour);
    }

    public void passToBank(){
        //this.token now is the token in selectedTokens
        bool active = false;
        string tempColour = token.colour;
        if (selectedTokens.removeOne(tempColour)){
            tokenBank.addOne(tempColour);
        }
    }

    // Start is called before the first frame update
    void Start()
    {
        //initialises the displayed amount text to amount given to scriptableobject
        //tokenBank.setExceptGold(7);
        if (amount != null) 
            amount.text = token.amount.ToString();
    }

    // Update is called once per frame
    /*void Update()
    {
        amount.text = token.amount.ToString();
    }*/

}
