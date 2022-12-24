using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class NobleSlot : MonoBehaviour
{
    [SerializeField] private Noble noble;
    [SerializeField] private OrientMenuManager omm;
    [SerializeField] private Image image;

    private SpriteRenderer m_SpriteRenderer;

    private bool active = true;

    public void SetupInventory(Noble noble) {
        this.noble = noble;
        m_SpriteRenderer = GetComponent<SpriteRenderer>();
        m_SpriteRenderer.sprite = noble.sprite;
        image.sprite = noble.sprite;
    }
    public void SetupOrient(OrientMenuManager omm, Noble noble) {
        this.omm = omm;
        image.color = Color.gray;
        SetupInventory(noble);
    }

    public void PassToOrient() {
        if (omm) {
            omm.Setup(noble);
            omm.ResetHighlightedCard();
        }
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
    public void SetNoble(Noble noble)
    {
        this.noble = noble;
        gameObject.transform.localScale = new Vector3(0.2f, 0.2f, 0.2f);
        gameObject.GetComponent<Button>().interactable = false;
        m_SpriteRenderer = GetComponent<SpriteRenderer>();
        m_SpriteRenderer.sprite = noble.sprite;
        image.color = Color.clear;
    }

    public Noble GetNoble()
    {
        return noble;
    }
}
