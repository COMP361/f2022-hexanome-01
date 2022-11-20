using System.Collections;
using System.Collections.Generic;
using System;
using UnityEngine;
using UnityEngine.UI;

public class Dashboard : MonoBehaviour
{
    public Text ptsDisplay;
    public Text endDisplay;

    public void UpdatePtsDisplay(int pts)
    {
        ptsDisplay.text = String.Format("Points: {0}", pts);
    }

    public void DisplayPurchase()
    {
        endDisplay.text = "Purchase";
    }

    public void ResetEndDisplay()
    {
        endDisplay.text = "End Turn";
    }
}
