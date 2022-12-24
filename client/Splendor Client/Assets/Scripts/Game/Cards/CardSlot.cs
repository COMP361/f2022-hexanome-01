using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
/*
This Script is attached to the sprite gameobject of a card in the gameobject
it includes all the playable functionality of card and takes its info from
the card object. (Make sure to call SetCard(Card card) before using functionality)
*/
public class CardSlot : MonoBehaviour {
    [SerializeField] private Card card;
    private OrientMenuManager omm;
    [SerializeField] private Image image, satchelImage;
    [SerializeField] private Text satchelCounter;
    private SpriteRenderer m_SpriteRenderer;

    private bool active = true;

    public void SetupInventory(Card card) { //sets regular display info for inventory/orient menu usage
        this.card = card;
        m_SpriteRenderer = GetComponent<SpriteRenderer>();
        m_SpriteRenderer.sprite = card.sprite;
        image.sprite = card.sprite;
        if (!omm) //if not part of orient menu, disable button component
            gameObject.GetComponent<Button>().interactable = false;
        if (card.satchels > 0) { //display satchel count on card
            satchelImage.gameObject.SetActive(true);
            satchelCounter.text = "X " + card.satchels;
        }
        else {
            satchelImage.gameObject.SetActive(false);
            satchelCounter.text = "";
        }
    }
    public void SetupOrient(OrientMenuManager omm, Card card) { //sets orient-specific display info, used when displaying options in orient menu
        this.omm = omm;
        image.color = Color.gray;
        SetupInventory(card);
    }

    public void PassToOrient() { //triggered by clicking on card, passes the card to orientMenuManager
        omm.Setup(card);
        omm.ResetHighlightedCard();
    }

    public void GreyOut() {
        m_SpriteRenderer = GetComponent<SpriteRenderer>();
        m_SpriteRenderer.color = Color.grey;
        active = false;
    }

    public void UnGreyOut() {
        m_SpriteRenderer = GetComponent<SpriteRenderer>();
        m_SpriteRenderer.color = Color.white;
        active = true;
    }
    /*Sets the card field to the card received as input and 
    updates the sprite render to display image of card
    */
    public void SetCard(Card card) { //used only for displaying cards on the actual board i believe
        this.card = card;
        gameObject.transform.localScale = new Vector3(0.5f, 0.5f, 0.5f);
        gameObject.GetComponent<Button>().interactable = false;
        m_SpriteRenderer = GetComponent<SpriteRenderer>();
        m_SpriteRenderer.sprite = card.sprite;
        image.color = Color.clear;
    }

    public void EmptySlot() {
        card = null;
        gameObject.SetActive(false);
    }

    public Card GetCard() {
        return card;
    }

}
