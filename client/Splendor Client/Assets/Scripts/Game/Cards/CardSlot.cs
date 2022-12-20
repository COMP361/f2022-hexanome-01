using System.Collections;
using System.Collections.Generic;
using UnityEngine;
/*
This Script is attached to the sprite gameobject of a card in the gameobject
it includes all the playable functionality of card and takes its info from
the card object. (Make sure to call SetCard(Card card) before using functionality)
*/
public class CardSlot : MonoBehaviour
{
    [SerializeField] private Card card;
    [SerializeField] private OrientMenuManager omm;

    private SpriteRenderer m_SpriteRenderer;

    private bool active = true;

    //using another cardslot to setup new cardslot (because for some reason a player's inventory is List<card>, but the game board has List<CardSlot>
    public void SetupOrient(OrientMenuManager omm, CardSlot cardSlot) {
        this.omm = omm;
        card = cardSlot.card;
        m_SpriteRenderer = GetComponent<SpriteRenderer>();
        m_SpriteRenderer.sprite = cardSlot.card.sprite;
    }

    //using a card object to setup new cardslot (because for some reason a player's inventory is List<card>, but the game board has List<CardSlot>
    public void SetupOrient(OrientMenuManager omm, Card card) {
        this.omm = omm;
        this.card = card;
        m_SpriteRenderer = GetComponent<SpriteRenderer>();
        m_SpriteRenderer.sprite = card.sprite;
    }

    public void GreyOut()
    {
        m_SpriteRenderer = GetComponent<SpriteRenderer>();
        m_SpriteRenderer.color = Color.grey;
        active = false;
    }

    public void UnGreyOut()
    {
        m_SpriteRenderer = GetComponent<SpriteRenderer>();
        m_SpriteRenderer.color = Color.white;
        active = true;
    }
    /*Sets the card field to the card received as input and 
    updates the sprite render to display image of card
    */
    public void SetCard(Card card)
    {
        this.card = card;
        m_SpriteRenderer = GetComponent<SpriteRenderer>();
        m_SpriteRenderer.sprite = card.sprite;
    }

    public void EmptySlot()
    {
        this.card = null;
        gameObject.SetActive(false);
    }

    public Card GetCard()
    {
        return card;
    }

}
