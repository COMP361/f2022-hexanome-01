using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class TokenBank : MonoBehaviour{
    public List<Gem> tokens = new List<Gem>();
    //displayed board token bank amount
    public Text goldAmount, blueAmount, greenAmount, blackAmount, redAmount, whiteAmount;
    
    public long gold;
    public long blue;
    public long green;
    public long black;
    public long red;
    public long white;

    public void setExceptGold(long n){
        gold = 5;
        blue = n;
        green = n;
        black = n;
        red = n;
        white = n;
        goldAmount.text = "5";
        blueAmount.text = n.ToString();
        greenAmount.text = n.ToString();
        blackAmount.text = n.ToString();
        redAmount.text = n.ToString();
        whiteAmount.text = n.ToString();
        foreach (Gem gem in tokens){
            if (gem.colour != "gold"){
                gem.amount = n;
            }
            else{gem.amount = 5;}
        }
    }

    public void Set(long gold, long blue, long green, long black, long red, long white)
    {
        this.gold = gold;
        this.blue = blue;
        this.green = green;
        this.black = black;
        this.red = red;
        this.white = white;
        
        goldAmount.text = gold.ToString();
        blueAmount.text = blue.ToString();
        greenAmount.text = green.ToString();
        blackAmount.text = black.ToString();
        redAmount.text = red.ToString();
        whiteAmount.text = white.ToString();
        
        foreach (Gem gem in tokens)
        {
            switch (gem.colour)
            {
                case "gold": gem.amount = gold; break;
                case "blue": gem.amount = blue; break;
                case "green": gem.amount = green; break;
                case "black": gem.amount = black; break;
                case "red": gem.amount = red; break;
                case "white": gem.amount = white; break;
            }
        }
    }

    public void setAll(long n){
        gold = n;
        blue = n;
        green = n;
        black = n;
        red = n;
        white = n;
        goldAmount.text = n.ToString();
        blueAmount.text = n.ToString();
        greenAmount.text = n.ToString();
        blackAmount.text = n.ToString();
        redAmount.text = n.ToString();
        whiteAmount.text = n.ToString();
        foreach (Gem gem in tokens){
            gem.amount = n;
        }
    }

    public long GetTotalAmount() {
        return gold + blue + green + black + red + white;
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
            case "gold": gold -= 1; goldAmount.text = gold.ToString(); break;
            case "blue": blue -= 1; blueAmount.text = blue.ToString(); break;
            case "green": green -= 1; greenAmount.text = green.ToString(); break;
            case "black": black -= 1; blackAmount.text = black.ToString(); break;
            case "red": red -= 1; redAmount.text = red.ToString(); break;
            case "white": white -= 1; whiteAmount.text = white.ToString(); break;
        }
        foreach(Gem token in tokens){
            if (colour == token.colour){
                token.amount -= 1;
            }
        }
    }
    
    public void AddAmount(string colour, long amount) {
        switch (colour) {
            case "white": white += amount; whiteAmount.text = white.ToString(); break;
            case "red": red += amount; redAmount.text = red.ToString(); break;
            case "blue": blue += amount; blueAmount.text = blue.ToString(); break;
            case "green": green += amount; greenAmount.text = green.ToString(); break;
            case "black": black += amount; blackAmount.text = black.ToString(); break;
            case "gold": gold += amount; goldAmount.text = gold.ToString(); break;
        }
    }

    public Text getDisplayedAmount(string colour){
        switch(colour){
            case "gold": return goldAmount; 
            case "blue": return blueAmount; 
            case "green": return greenAmount; 
            case "black": return blackAmount; 
            case "red": return redAmount; 
            case "white": return whiteAmount;
        }
        return null;
    }

}
