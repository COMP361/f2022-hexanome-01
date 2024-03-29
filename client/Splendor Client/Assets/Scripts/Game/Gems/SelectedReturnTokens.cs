using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using System;

public class SelectedReturnTokens : MonoBehaviour
{
    public List<Gem> sTokens = new List<Gem> ();
    public PlayerControl playerControl;
    public GameObject returnTokenButton;

    //displayed selected tokens amount
    //Text colour1, colour2, colour3;
    public List<Text> colours = new List<Text>();

    //displayed selected tokens amount
    public List<Text> nums = new List<Text>();

    public long getTotalNum(){
        long n=0;
        foreach (Gem token in sTokens){
            n += token.amount;
        }
        return n;
    }

    public long getNum(string colour){
        foreach(Gem token in sTokens){
            if (token.colour == colour){return token.amount;}
        }
        return -1;
    }

    public bool addOne(string colour){
        if (this.getTotalNum() == playerControl.tokenOverflow) {
            return false;
        } else {
            for (int i = 0; i < 3; i++) {
                if (sTokens[i].colour == colour) {
                    sTokens[i].amount += 1;
                    nums[i].text = sTokens[i].amount.ToString();
                    return true;
                } else if (sTokens[i].colour == "none") {
                    sTokens[i].colour = colour;
                    colours[i].text = colour;
                    sTokens[i].amount += 1;
                    nums[i].text = sTokens[i].amount.ToString();
                    return true;
                }
            }
        }
        return false;
        
    }

    public bool removeOne(string colour){
        for (int i =0; i<3; i++){
            if (sTokens[i].colour == colour & sTokens[i].amount > 0){
                sTokens[i].amount -= 1;
                nums[i].text = sTokens[i].amount.ToString();
                if (sTokens[i].amount == 0){
                    sTokens[i].colour = "none";
                    colours[i].text = "none";
                    return true;
                    }
                return true;
            }
        }
        return false;
    }

    public void reset(TokenBank tokenBank){
        foreach (Gem token in sTokens){
            tokenBank.AddAmount(token.colour, token.amount);
            token.colour = "none";
            token.amount = 0;
        }
        foreach (Text colour in colours){
            colour.text = "none";
        }
        foreach (Text amount in nums){
            amount.text = "0";
        }
    }

    public bool CheckReturnAmount() {
        // long selectTotal = getTotalNum();
        if (this.getTotalNum() == playerControl.tokenOverflow) {
            return true;
        }
        return false;
    }
    // Start is called before the first frame update
    /*void Start()
    {
        reset();
    }*/

    // Update is called once per frame
    void Update()
    {
        
    }
}
