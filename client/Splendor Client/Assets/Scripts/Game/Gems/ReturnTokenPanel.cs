using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEditor;

public class ReturnTokenPanel : MonoBehaviour
{
    [SerializeField] private PlayerControl playerControl;

    [SerializeField] private GameObject returnTokenPanel; // Menu make appear under condition after end turn/disappear after selection
    public bool needToReturn;

    public void ReturnMenuStatus() {
        playerControl.inTokenMenu = !playerControl.inTokenMenu;
    }

    public void Display() {
        if (returnTokenPanel.activeInHierarchy) {
            returnTokenPanel.SetActive(false);
        } else {
            Text returnText = returnTokenPanel.transform.Find("ReturnText").gameObject.GetComponent<Text>();
            long toReturn = playerControl.client.GetTokenBank().GetTotalAmount() - 10;

            // To self: remember to add the exsessive token number
            returnText.text = "Please return" + toReturn.ToString() + "tokens";
        }
    }

    public void TurnOffDisplay() {
        returnTokenPanel.SetActive(false);
    }


}