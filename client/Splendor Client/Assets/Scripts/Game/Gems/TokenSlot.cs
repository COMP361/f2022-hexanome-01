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
    [SerializeField] private SelectedReturnTokens selectedReturnTokens;
    [SerializeField] private TokenBank tokenBank;
    public GameObject takeTokenButton;
    private bool confirm;

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
        bool active = false;
        active = selectedTokens.addOne(token.colour);
        if (active){
            //this token is the token in bank not selectedToken
            //token.amount -= 1;
            tokenBank.removeOne(token.colour);
            //this.amount.text = token.amount.ToString();
            confirm = selectedTokens.checkAmount();
            if (confirm){
                takeTokenButton.SetActive(true);
            }
            else{takeTokenButton.SetActive(false);}
        }
        //tokenBank.removeOne(token.colour);
    }

    public void passToBank(){
        //this.token now is the token in selectedTokens
        bool active = false;
        string tempColour = token.colour;
        active = selectedTokens.removeOne(tempColour);
        if (active){
            tokenBank.addOne(tempColour);
            confirm = selectedTokens.checkAmount();
            if (confirm){
                takeTokenButton.SetActive(true);
            }
            else{takeTokenButton.SetActive(false);}
        }
    }

    // Select token to be removed from the inventory
    public void passToSelectRemove() {
        bool active = false;
        string tempColour = token.colour;
        active = selectedReturnTokens.addOne(tempColour);
        if (active) {
            tokenBank.removeOne(tempColour);
            confirm = selectedReturnTokens.CheckReturnAmount();
            if (confirm) {
                takeTokenButton.SetActive(true);
            } else {
                takeTokenButton.SetActive(false);
            }
        }
    }

    // Unselect token to have it return to inventory
    public void passToWallet() {
        bool active = false;
        string tempColour = token.colour;
        active = selectedReturnTokens.removeOne(tempColour);
        if (active) {
            tokenBank.addOne(tempColour);
            confirm = selectedReturnTokens.CheckReturnAmount();
            if (confirm) {
                takeTokenButton.SetActive(true);
            } else {
                takeTokenButton.SetActive(false);
            }
        }
    }

    // Start is called before the first frame update
    void Start()
    {
        //initialises the displayed amount text to amount given to scriptableobject
        //tokenBank.setExceptGold(7);
        amount.text = token.amount.ToString();
    }

    // Update is called once per frame
    /*void Update()
    {
        amount.text = token.amount.ToString();
    }*/

}
