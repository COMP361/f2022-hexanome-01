using System.Collections;
using System.Collections.Generic;
using System;
using UnityEngine;
using UnityEngine.UI;

public class Dashboard : MonoBehaviour
{
    public Text ptsDisplay;

    public void UpdatePtsDisplay(int pts)
    {
        ptsDisplay.text = String.Format("Points: {0}", pts);
    }
}
