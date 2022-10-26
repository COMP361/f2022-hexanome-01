using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Player : MonoBehaviour
{
    public Dashboard dashboard;

    private int pointsTotal = 0;

    public void TriggerPointsAdd(Card cardObject)
    {
        pointsTotal += cardObject.GetPoints();
        dashboard.UpdatePtsDisplay(pointsTotal);
    }

}
