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
    public Player player;
    public GameObject cardInventoryPrefab;//Blank card gameobject that we will change its image depending on player's inventory
    public Transform purchasedCardsParent;//Parent as to where we will display the cards in the inventory

    //Display is called by the button to open up the panel
    public void Display()
    {
        DisplayPlayerCards(player.inventory);
    }
    public void DisplayPlayerCards(List<Card> playerCards)
    {
        ClearChildren(purchasedCardsParent);
        foreach(Card card in playerCards)
        {
            GameObject cardInstance = Instantiate(cardInventoryPrefab, purchasedCardsParent);
            Image cardImage = cardInstance.GetComponent<Image>();
            cardImage.sprite = card.sprite;
        }
    }
    //Need to clear children to not repeat cards everytime we open and close the panel
    private void ClearChildren(Transform parent)
    {
        foreach(Transform child in parent)
        {
            Destroy(child.gameObject);
        }
    }
}
