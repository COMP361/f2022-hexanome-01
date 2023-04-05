using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using System;

public class SelectedTokens : MonoBehaviour
{
    public List<Gem> sTokens = new List<Gem> ();
    private PlayerControl playerControl;
    public GameObject takeTokenButton;

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
        for (int i =0; i<3; i++){
            //cannot take more than 2 of the same colour
            if (sTokens[i].colour == colour){
                if(this.getTotalNum()<=1){
                    sTokens[i].amount += 1; 
                    nums[i].text = sTokens[i].amount.ToString();
                    return true;}
                return false;
            }
            //when take 3 different colours
            else if (sTokens[i].colour == "none"){
                if (sTokens[0].amount<2 & sTokens[1].amount<2 & sTokens[2].amount<2
                & sTokens[0].colour!=colour & sTokens[1].colour!=colour & sTokens[2].colour!=colour){
                    sTokens[i].colour = colour;
                    colours[i].text = colour;
                    sTokens[i].amount += 1;
                    nums[i].text = sTokens[i].amount.ToString();
                    return true;}
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

    public void clearUI(){
        foreach (Gem token in sTokens){
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

    public bool checkAmount(){
        long total = getTotalNum();
        if (total == 3){
            return true;
        }
        else if (total == 2){
            for (int i =0; i<3; i++){
                if (sTokens[i].amount == 2){
                    return true;}
            }
            return false;
        }
        return false;
    }

    // public bool CheckReturnAmount() {
    //     long selectTotal = getTotalNum();
    //     long ownTotal = playerControl.client.GetTokenBank().GetTotalAmount();
    //     if (selectTotal == ownTotal - 10) {
    //         return true;
    //     }
    //     return false;
    // }
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
