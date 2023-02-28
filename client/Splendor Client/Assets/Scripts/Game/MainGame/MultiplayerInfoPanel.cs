using System.Collections;
using System.Collections.Generic;
using System;
using UnityEngine;
using UnityEngine.UI;

public class MultiplayerInfoPanel : MonoBehaviour
{
    public Text nameField;
    public Text pointsField;
    public Text reservedCardsCount;
    public Text reservedNoblesCount;

    public void UpdatePlayerName(string name)
    {
        nameField.text = name;
    }

    public void UpdatePlayerPoints(long pts)
    {
        pointsField.text = String.Format("{0}", pts);
    }

    public void UpdateReservedCardsCount(int count) {
        reservedCardsCount.text = String.Format("{0}", count);
    }

    public void UpdateReservedNoblesCount(int count)
    {
        reservedNoblesCount.text = String.Format("{0}", count);
    }
}