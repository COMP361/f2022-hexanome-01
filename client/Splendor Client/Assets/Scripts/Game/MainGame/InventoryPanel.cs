using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
/*
This script it's used to display the cards that the player has in its
inventory
*/
public class InventoryPanel : MonoBehaviour
{
    [SerializeField] private PlayerControl playerControl;
    [SerializeField] private GameObject inventoryPanel;
    [SerializeField] private GameObject purchasedCardContent, nobleContent, reservedCardContent, reservedNobleContent;
    public GameObject cardSlot;//Blank card gameobject that we will change its image depending on player's inventory
    public GameObject nobleSlot;//Blank noble gameobject that we will change its image depending on player's inventory
    //Display is called by the button to open up the panel

    public void InventoryStatus() {
        playerControl.inInventory = playerControl.inInventory ? false : true;
    }
    public void Display() {
        if (inventoryPanel.activeInHierarchy)
            inventoryPanel.SetActive(false);
        else {
            inventoryPanel.SetActive(true);
            DisplayPlayerCards(playerControl.client.inventory, playerControl.client.noblesVisited);
            DisplayReservedCards(playerControl.client.cardReserves, playerControl.client.nobleReserves);
        }
        InventoryStatus();
    }

    public void DisplayReservedCards(List<Card> playerCards, List<Noble> playerNobles) {
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
    public void DisplayPlayerCards(List<Card> playerCards, List<Noble> playerNobles) {
        ClearChildren(purchasedCardContent, nobleContent);
        foreach(Card card in playerCards)
        {
            GameObject temp = Instantiate(cardSlot, purchasedCardContent.transform.position, Quaternion.identity);
            temp.transform.SetParent(purchasedCardContent.transform);
            //temp.transform.localScale = new Vector3(1, 1, 1);
            temp.GetComponent<CardSlot>().SetupInventory(card);
        }
        foreach(Noble noble in playerNobles)
        {
            GameObject nobleInstance = Instantiate(nobleSlot, nobleContent.transform.position, Quaternion.identity);
            nobleInstance.transform.SetParent(nobleContent.transform);
            //nobleInstance.transform.localScale = new Vector3(0.2f, 0.4f, 1f);
            nobleInstance.GetComponent<NobleSlot>().SetupInventory(noble);
        }
    }
    //Need to clear children to not repeat cards everytime we open and close the panel
    private void ClearChildren(GameObject parent, GameObject nobleParent) {
        foreach(Transform child in parent.transform)
        {
            Destroy(child.gameObject);
        }
        foreach(Transform child in nobleParent.transform){
            Destroy(child.gameObject);
            
        }
    }
}
