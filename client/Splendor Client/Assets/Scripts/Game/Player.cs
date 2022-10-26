using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Player : MonoBehaviour
{
    private int pointsTotal = 0;

    public int GetPoints()
    {
        return pointsTotal;
    }

    public void TriggerPointsAdd(Card cardObject)
    {
        pointsTotal += cardObject.GetPoints();
    }

}
