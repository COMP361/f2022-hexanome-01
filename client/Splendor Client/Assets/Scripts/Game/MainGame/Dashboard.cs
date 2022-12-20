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

    public void UpdatePtsDisplay(int pts)
    {
        ptsDisplay.text = String.Format("Points: {0}", pts);
    }

    public void DisplayPurchase()
    {
        endDisplay.text = "Purchase";
    }

    public void DisplayWaiting()
    {
        endDisplay.text = "Waiting";
    }

    public void ResetEndDisplay()
    {
        endDisplay.text = "End Turn";
    }

    public void UpdateTokenDisplay(CardGemValue inventory)
    {
        bTokenDisplay.text = String.Format("{0}", inventory.blue);
        gTokenDisplay.text = String.Format("{0}", inventory.green);
        kTokenDisplay.text = String.Format("{0}", inventory.brown);
        rTokenDisplay.text = String.Format("{0}", inventory.red);
        wTokenDisplay.text = String.Format("{0}", inventory.white);
    }
}
