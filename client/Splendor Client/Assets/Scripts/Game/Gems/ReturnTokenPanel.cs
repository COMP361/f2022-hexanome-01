using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEditor;

public class ReturnTokenPanel : MonoBehaviour
{
    [SerializeField] private PlayerControl playerControl;

    [SerializeField] private GameObject returnTokenPanel; // Menu make appear under condition after end turn/disappear after selection
    [SerializeField] private Text messageText;
    public bool needToReturn;

    // Will display this panel when the player has more than 10 tokens after: take tokens and reserve cards
    public void Display(long overFlowAmount) {
        if (returnTokenPanel.activeInHierarchy) {
            returnTokenPanel.SetActive(false);
        } else {
            Text returnText = returnTokenPanel.transform.Find("ReturnText").gameObject.GetComponent<Text>();
            //long toReturn = playerControl.client.GetTokenBank().GetTotalAmount() - 10;

            // To self: remember to add the exsessive token number
            messageText.text = "Uh oh! You have more than 10 tokens now!";
            returnText.text = "Please return " + overFlowAmount.ToString() + " tokens";

            returnTokenPanel.SetActive(true);
        }
    }

    public void DisplayTP() {
        if (returnTokenPanel.activeInHierarchy) {
            returnTokenPanel.SetActive(false);
        }
        else {
            Text returnText = returnTokenPanel.transform.Find("ReturnText").gameObject.GetComponent<Text>();
            //long toReturn = playerControl.client.GetTokenBank().GetTotalAmount() - 10;

            // To self: remember to add the exsessive token number
            messageText.text = "Due to a trading post, you get to select a token for free! Yayyyyyy";
            returnText.text = "Please select 1 token to get for free";

            returnTokenPanel.SetActive(true);
        }
    }

    public void TurnOffDisplay() {
        returnTokenPanel.SetActive(false);
    }
}