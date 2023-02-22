using System.Collections;
using System.Collections.Generic;
using System;
using UnityEngine;
using UnityEngine.UI;

public class Dashboard : MonoBehaviour
{
    public Text ptsDisplay;
    public Text endDisplay;

    public Text bTokenDisplay;
    public Text gTokenDisplay;
    public Text kTokenDisplay;
    public Text rTokenDisplay;
    public Text wTokenDisplay;

    public Text bBonusDisplay;
    public Text gBonusDisplay;
    public Text kBonusDisplay;
    public Text rBonusDisplay;
    public Text wBonusDisplay;

    public void UpdatePtsDisplay(int pts)
    {
        ptsDisplay.text = String.Format("{0}", pts);
    }

    public void DisplayPurchase()
    {
        endDisplay.text = "purchase";
    }

    public void DisplayWaiting()
    {
        endDisplay.text = "waiting";
    }

    public void DisplayTakeTokens(){
        endDisplay.text = "take tokens";
    }

    public void ResetEndDisplay()
    {
        endDisplay.text = "end turn";
    }

    public void UpdateTokenDisplay(CardGemValue inventory)
    {
        bTokenDisplay.text = String.Format("{0}", inventory.blue);
        gTokenDisplay.text = String.Format("{0}", inventory.green);
        kTokenDisplay.text = String.Format("{0}", inventory.brown);
        rTokenDisplay.text = String.Format("{0}", inventory.red);
        wTokenDisplay.text = String.Format("{0}", inventory.white);
    }

    /*
     * Eventually should update the bonus display as well as the token display.
    public void UpdateBonusDisplay(CardGemValue inventory)
    {
        bBonusDisplay.text = String.Format("{0}", inventory.blue);
        gBonusDisplay.text = String.Format("{0}", inventory.green);
        kBonusDisplay.text = String.Format("{0}", inventory.brown);
        rBonusDisplay.text = String.Format("{0}", inventory.red);
        wBonusDisplay.text = String.Format("{0}", inventory.white);
    }
    */
}
