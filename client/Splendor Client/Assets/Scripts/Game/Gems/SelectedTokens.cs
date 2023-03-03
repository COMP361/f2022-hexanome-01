using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class SelectedTokens : MonoBehaviour
{
    public List<Gem> sTokens = new List<Gem>();
    private PlayerControl playerControl;

    //displayed selected tokens amount
    //Text colour1, colour2, colour3;
    [SerializeField] private Image slot1, slot2, slot3;

    public long getTotalNum(){
        long n = 0;
        foreach (Gem token in sTokens){
            n += token.amount;
        }
        return n;
    }

    public long getNum(string colour){
        foreach(Gem token in sTokens){
            if (token.colour.Equals(colour)) 
                return token.amount;
        }
        return -1;
    }

    public bool addOne(string colour)
    {
        for (int i = 0; i < 3; i++){
            //cannot take more than 2 of the same colour
            if (sTokens[i].colour.Equals(colour))
            {
                long tokenNum = getTotalNum();
                if(tokenNum <= 1){
                    sTokens[i].amount += 1;
                    switch (tokenNum)
                    {
                        case 0: 
                            slot1.color = SetColour(colour);
                            slot1.gameObject.SetActive(true);
                            return true;
                        case 1:
                            slot2.color = SetColour(colour);
                            slot2.gameObject.SetActive(true);
                            return true;
                    }
                }
            }
            //when take 3 different colours
            else if (sTokens[i].colour == "none"){
                if (sTokens[0].amount<2 & sTokens[1].amount<2 & sTokens[2].amount<2){
                    sTokens[i].colour = colour;
                    switch (i)
                    {
                        case 0: 
                            slot1.color = SetColour(colour);
                            slot1.gameObject.SetActive(true);
                            return true;
                        case 1:
                            slot2.color = SetColour(colour);
                            slot2.gameObject.SetActive(true);
                            return true;
                        case 2:
                            slot3.color = SetColour(colour);
                            slot3.gameObject.SetActive(true);
                            return true;
                    }
                    sTokens[i].amount += 1;
                }
            }
        }
        return false;
    }

    public bool removeOne(string colour){
        for (int i = 2; i >= 0; i--){
            if (sTokens[i].colour == colour & sTokens[i].amount > 0){
                sTokens[i].amount -= 1;
                if (sTokens[i].amount == 0){
                    sTokens[i].colour = "none";
                    switch (i)
                    {
                        case 0:
                            slot1.gameObject.SetActive(false);
                            return true;
                        case 1:
                            slot2.gameObject.SetActive(false);
                            return true;
                        case 2:
                            slot3.gameObject.SetActive(false);
                            return true;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public void Reset(){
        foreach (Gem token in sTokens){
            token.colour = "none";
            token.amount = 0;
        }
        
        slot1.gameObject.SetActive(false);
        slot2.gameObject.SetActive(false);
        slot3.gameObject.SetActive(false);
    }

    private Color32 SetColour(string colour)
    {
        switch (colour)
        {
            case "black": return new Color32(39, 39, 52, 255);
            case "blue": return new Color32(110, 156, 198, 255);
            case "gold": return new Color32(240, 215, 148, 255);
            case "green": return new Color32(92, 138, 113, 255);
            case "red": return new Color32(203, 108, 105, 255);
            case "white": return new Color32(255, 253, 240, 255);
        }

        return new Color32(242, 236, 187, 255);
    }
}
