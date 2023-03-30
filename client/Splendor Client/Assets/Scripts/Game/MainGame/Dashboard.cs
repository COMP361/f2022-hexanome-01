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
    public Text jTokenDisplay;

    public Text bBonusDisplay;
    public Text gBonusDisplay;
    public Text kBonusDisplay;
    public Text rBonusDisplay;
    public Text wBonusDisplay;
    public Text jBonusDisplay;

    public Image reserveCard1, reserveCard2, reserveCard3, 
        reserveNoble1, reserveNoble2, reserveNoble3, reserveNoble4, reserveNoble5,
        acquiredCity;
    public Sprite emptyReserveCard, emptyReserveNoble;

    public void UpdatePtsDisplay(long pts)
    {
        ptsDisplay.text = String.Format("{0}", pts);
    }

    public void DisplayPurchase()
    {
        endDisplay.text = "purchase";
    }

    public void DisplayReserve()
    {
        endDisplay.text = "reserve";
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

    public void UpdateReserveCardDisplay(Sprite sprite, int index) {
        switch (index) {
            case 1: reserveCard1.sprite = sprite; break;
            case 2: reserveCard2.sprite = sprite; break;
            case 3: reserveCard3.sprite = sprite; break;
        }
    }

    public void resetReservedCardDisplay(){
        Sprite sprite = Resources.Load<Sprite>("game_reservedcard");
        reserveCard1.sprite = sprite;
        reserveCard2.sprite = sprite;
        reserveCard3.sprite = sprite;
    }

    public void UpdateReserveNobleDisplay(Sprite sprite, int index)
    {
        switch (index)
        {
            case 1: reserveNoble1.sprite = sprite; break;
            case 2: reserveNoble2.sprite = sprite; break;
            case 3: reserveNoble3.sprite = sprite; break;
            case 4: reserveNoble4.sprite = sprite; break;
            case 5: reserveNoble5.sprite = sprite; break;
        }
    }

    public void SetNobleReserveCount(int count) {
        switch (count) {
            case 3:
                reserveNoble4.gameObject.SetActive(false);
                reserveNoble5.gameObject.SetActive(false);
                break;
            case 4:
                reserveNoble5.gameObject.SetActive(false);
                break;
        }
    }

    public void UpdateAcquiredCityDisplay(Sprite sprite)
    {
        acquiredCity.sprite = sprite;
    }
}
