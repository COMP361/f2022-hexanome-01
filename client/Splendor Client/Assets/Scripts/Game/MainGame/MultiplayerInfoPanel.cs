using System.Collections;
using System.Collections.Generic;
using System;
using UnityEngine;
using UnityEngine.UI;

public class MultiplayerInfoPanel : MonoBehaviour
{
    public Text nameField;
    public Text pointsField;

    public void UpdatePlayerName(string name)
    {
        nameField.text = name;
    }

    public void UpdatePlayerPoints(int pts)
    {
        pointsField.text = String.Format("Points: {0}", pts);
    }
}