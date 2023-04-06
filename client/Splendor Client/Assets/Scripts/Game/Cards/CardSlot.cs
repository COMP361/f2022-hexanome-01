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
    [SerializeField] private Image image;
    private SpriteRenderer m_SpriteRenderer;
    [SerializeField] private Text satchelText;

    private bool active = true;

    public void SetupInventory(Card card) { //sets regular display info for inventory
        m_SpriteRenderer = GetComponent<SpriteRenderer>();
        m_SpriteRenderer.sprite = card.sprite;
        image.sprite = card.sprite;
        if (card.GetSatchels() == 0)
            satchelText.text = "";
        else
            satchelText.text = "+ " + card.GetSatchels();
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

    // Sets the card field to the card received as input and updates the sprite render to display image of card
    public void SetCard(Card card) { //used only for displaying cards on the actual board i believe
        this.card = card;
        gameObject.transform.localScale = new Vector3(0.77f, 0.77f, 0.77f);
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

    /// <summary>
    /// Overrides Equals to determine if the card in the slot is the same as the card in the given card slot.
    /// </summary>
    /// <param name="cardSlot">the card solt with which to compare this card slot</param>
    /// <returns>whether the card in the slot has the same id as the card in the given card slot</returns>
    public bool Equals(CardSlot cardSlot) {
        if (cardSlot.GetCard().Equals(card)) return true;
        return false;
    }

}
