using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class TokenBank : MonoBehaviour{
    public List<Gem> tokens = new List<Gem>();
    //displayed board token bank amount
    public Text goldAmount, blueAmount, greenAmount, blackAmount, redAmount, whiteAmount;
    
    public int gold;
    public int blue;
    public int green;
    public int black;
    public int red;
    public int white;

    public void setExceptGold(int n){
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

    public void setAll(int n){
        gold = n;
        blue = n;
        green = n;
        black = n;
        red = n;
        white = n;
        foreach (Gem gem in tokens){
            gem.amount = n;
        }
    }

    public void addOne(string colour){
        switch(colour){
            case "gold": gold += 1; goldAmount.text = gold.ToString(); break;
            case "blue": blue += 1; blueAmount.text = blue.ToString(); break;
            case "green": green += 1; greenAmount.text = green.ToString(); break;
            case "black": black += 1; blackAmount.text = black.ToString(); break;
            case "red": red += 1; redAmount.text = red.ToString(); break;
            case "white": white += 1; whiteAmount.text = white.ToString(); break;
        }
        foreach(Gem token in tokens){
            if (colour == token.colour){
                token.amount += 1;
            }
        }
    }

    public void removeOne(string colour){
        switch(colour){
            case "gold": gold -= 1; break;
            case "blue": blue -= 1; break;
            case "green": green -= 1; break;
            case "black": black -= 1; break;
            case "red": red -= 1; break;
            case "white": white -= 1; break;
        }
        /*foreach(Gem token in tokens){
            if (colour == token.colour){
                token.amount -= 1;
            }
        }*/
    }

    //hard coded token amount; the real amount should be different according to number of players
    //gold token is always 5 regardless of number of players
    void Start(){
        setExceptGold(7);
        /*goldSlot.amount.text = goldSlot.token.amount.ToString();
        blueSlot.amount.text = blueSlot.token.amount.ToString();
        greenSlot.amount.text = greenSlot.token.amount.ToString();
        blackSlot.amount.text = blackSlot.token.amount.ToString();
        redSlot.amount.text = redSlot.token.amount.ToString();
        whiteSlot.amount.text = whiteSlot.token.amount.ToString();
        */
    }

    public Text getDisplayedAmount(string colour){
        switch(colour){
            case "gold": return goldAmount; break;
            case "blue": return blueAmount; break;
            case "green": return greenAmount; break;
            case "black": return blackAmount; break;
            case "red": return redAmount; break;
            case "white": return whiteAmount; break;
        }
        return null;
    }

}
