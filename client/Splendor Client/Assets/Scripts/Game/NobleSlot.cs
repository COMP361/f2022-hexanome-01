using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class NobleSlot : MonoBehaviour
{
    [SerializeField] private Noble noble;

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
    public void SetNoble(Noble noble)
    {
        this.noble = noble;
        m_SpriteRenderer = GetComponent<SpriteRenderer>();
        m_SpriteRenderer.sprite = noble.sprite;
    }

    public Noble GetNoble()
    {
        return noble;
    }
}
