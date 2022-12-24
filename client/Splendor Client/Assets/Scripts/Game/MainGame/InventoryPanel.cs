using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
/*
This script it's used to display the cards that the player has in its
inventory
*/
public class InventoryPanel : MonoBehaviour {
    [SerializeField] private PlayerControl playerControl;
    [SerializeField] private GameObject inventoryPanel; //menu do make appear/disappear through button press
    [SerializeField] private GameObject purchasedCardContent, nobleContent, reservedCardContent, reservedNobleContent; //panels to display information on
    [SerializeField] private GameObject cardSlot;//Blank card prefab
    [SerializeField] private GameObject nobleSlot;//Blank noble prefab
    //Display is called by the button to open/close the panel

    public void InventoryStatus() {//switches inventory status (needed for use with button)
        playerControl.inInventory = !playerControl.inInventory;
    }
    public void Display() { //displays/hides the menu
        if (inventoryPanel.activeInHierarchy)
            inventoryPanel.SetActive(false);
        else {
            inventoryPanel.SetActive(true);
            DisplayPlayerCards(playerControl.client.inventory, playerControl.client.noblesVisited);
            DisplayReservedCards(playerControl.client.cardReserves, playerControl.client.nobleReserves);
        }
        InventoryStatus();
    }

    public void DisplayReservedCards(List<Card> playerCards, List<Noble> playerNobles) { //displays reserved cards/nobles
        ClearChildren(reservedCardContent, reservedNobleContent);
        foreach (Card card in playerCards) {
            GameObject temp = Instantiate(cardSlot, reservedCardContent.transform.position, Quaternion.identity);
            temp.transform.SetParent(reservedCardContent.transform);
            //temp.transform.localScale = new Vector3(1, 1, 1);
            temp.GetComponent<CardSlot>().SetupInventory(card);
        }
        foreach (Noble noble in playerNobles) {
            GameObject nobleInstance = Instantiate(nobleSlot, reservedNobleContent.transform.position, Quaternion.identity);
            nobleInstance.transform.SetParent(reservedNobleContent.transform);
            //nobleInstance.transform.localScale = new Vector3(0.2f, 0.4f, 1f);
            nobleInstance.GetComponent<NobleSlot>().SetupInventory(noble);
        }
    }
    public void DisplayPlayerCards(List<Card> playerCards, List<Noble> playerNobles) { //displays acquired cards/nobles
        ClearChildren(purchasedCardContent, nobleContent);
        foreach (Card card in playerCards) {
            GameObject temp = Instantiate(cardSlot, purchasedCardContent.transform.position, Quaternion.identity);
            temp.transform.SetParent(purchasedCardContent.transform);
            //temp.transform.localScale = new Vector3(1, 1, 1);
            temp.GetComponent<CardSlot>().SetupInventory(card);
        }
        foreach (Noble noble in playerNobles) {
            GameObject nobleInstance = Instantiate(nobleSlot, nobleContent.transform.position, Quaternion.identity);
            nobleInstance.transform.SetParent(nobleContent.transform);
            //nobleInstance.transform.localScale = new Vector3(0.2f, 0.4f, 1f);
            nobleInstance.GetComponent<NobleSlot>().SetupInventory(noble);
        }
    }

    private void ClearChildren(GameObject parent, GameObject nobleParent) { //Need to clear children to not repeat cards everytime we open and close the panel
        foreach (Transform child in parent.transform) {
            Destroy(child.gameObject);
        }
        foreach (Transform child in nobleParent.transform) {
            Destroy(child.gameObject);

        }
    }
}
