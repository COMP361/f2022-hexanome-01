using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEditor;

/* 
 * This script it's used to display the cards and nobles that any player has in their inventory.
 * Since reserved cards and nobles are kept "in hand" or "face down" according to Splendor rules, 
 * reserved cards and nobles should not be displayed in the inventory since the inventory is public.
 */
public class InventoryPanel : MonoBehaviour {
    [SerializeField] private Player player;
    [SerializeField] private GameObject inventoryPanel; //menu do make appear/disappear through button press
    [SerializeField] private GameObject purchasedCardContent, nobleContent; //panels to display information on
    [SerializeField] private GameObject cardSlot; //Blank card prefab
    [SerializeField] private GameObject nobleSlot; //Blank noble prefab
    //Display is called by the button to open/close the panel

    public void Display() { //displays/hides the menu
        if (inventoryPanel.activeInHierarchy) {
            inventoryPanel.SetActive(false);
            CardSlot[] cards = (CardSlot[])Resources.FindObjectsOfTypeAll(typeof(CardSlot));
            NobleSlot[] nobles = (NobleSlot[])Resources.FindObjectsOfTypeAll(typeof(NobleSlot));
            CitySlot[] cities = (CitySlot[])Resources.FindObjectsOfTypeAll(typeof(CitySlot));
            foreach (CardSlot c in cards) {
                if (PrefabUtility.GetPrefabAssetType(c) == PrefabAssetType.NotAPrefab)
                    c.gameObject.SetActive(true);
            }
            foreach (NobleSlot n in nobles) {
                if (PrefabUtility.GetPrefabAssetType(n) == PrefabAssetType.NotAPrefab)
                    n.gameObject.SetActive(true);
            }
            foreach (CitySlot c in cities) {
                if (PrefabUtility.GetPrefabAssetType(c) == PrefabAssetType.NotAPrefab)
                    c.gameObject.SetActive(true);
            }
        }
        else {
            CardSlot[] cards = (CardSlot[])Resources.FindObjectsOfTypeAll(typeof(CardSlot));
            NobleSlot[] nobles = (NobleSlot[])Resources.FindObjectsOfTypeAll(typeof(NobleSlot));
            CitySlot[] cities = (CitySlot[])Resources.FindObjectsOfTypeAll(typeof(CitySlot));
            foreach (CardSlot c in cards) {
                if (PrefabUtility.GetPrefabAssetType(c) == PrefabAssetType.NotAPrefab)
                    c.gameObject.SetActive(false);
            }
            foreach (NobleSlot n in nobles) {
                if (PrefabUtility.GetPrefabAssetType(n) == PrefabAssetType.NotAPrefab)
                    n.gameObject.SetActive(false);
            }
            foreach (CitySlot c in cities) {
                if (PrefabUtility.GetPrefabAssetType(c) == PrefabAssetType.NotAPrefab)
                    c.gameObject.SetActive(false);
            }
            //set inventory panel title as the inventory owner's name
            Text ownerName = inventoryPanel.transform.Find("OwnerName").gameObject.GetComponent<Text>();
            if (ownerName != null) ownerName.text = player.GetUsername();

            inventoryPanel.SetActive(true);
            DisplayPlayerCards(player.GetAcquiredCards(), player.GetAcquiredNobles());
        }
    }

    public void DisplayPlayerCards(List<Card> playerCards, List<Noble> playerNobles) { //displays acquired cards/nobles
        ClearChildren(purchasedCardContent, nobleContent);
        foreach (Card card in playerCards) {
            GameObject temp = Instantiate(cardSlot, purchasedCardContent.transform.position, Quaternion.identity);
            temp.transform.SetParent(purchasedCardContent.transform);
            temp.transform.localScale = new Vector3(1, 1, 1);
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
