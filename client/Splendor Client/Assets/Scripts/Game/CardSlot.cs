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

    private SpriteRenderer m_SpriteRenderer;

    private bool active = true;

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
