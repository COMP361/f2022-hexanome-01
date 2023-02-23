using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class SelectedTokens : MonoBehaviour
{
    public List<Gem> sTokens = new List<Gem> ();
    private PlayerControl playerControl;
    public List<Text> colours = new List<Text>();
    //Text colour1, colour2, colour3;
    public List<Text> nums = new List<Text>();

    public int getTotalNum(){
        int n=0;
        foreach (Gem token in sTokens){
            n += token.amount;
        }
        return n;
    }

    public int getNum(string colour){
        foreach(Gem token in sTokens){
            if (token.colour == colour){return token.amount;break;}
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
                if (sTokens[0].amount<2 & sTokens[1].amount<2 & sTokens[2].amount<2){
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

    public void reset(){
        foreach (Gem token in sTokens){
            token.colour = "none";
            token.amount = 0;
        }
        foreach (Text colour in colours){
            colour.text = "none";
        }
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
