using System.Collections;
using System.Collections.Generic;
using System.Numerics;
using UnityEngine;
using UnityEngine.UI;

public class NobleSlot : MonoBehaviour {
    [SerializeField] private Noble noble;
    [SerializeField] private OrientMenuManager omm;
    [SerializeField] private Image image;

    private SpriteRenderer m_SpriteRenderer;

    private bool active = true;

    public void SetupInventory(Noble noble) { //sets all the values for use in inventory/orient menu
        this.noble = noble;
        if (!omm) //if not part of orient menu, disable button component
            gameObject.GetComponent<Button>().interactable = false;
        m_SpriteRenderer = GetComponent<SpriteRenderer>();
        m_SpriteRenderer.sprite = noble.sprite;
        image.sprite = noble.sprite;
    }
    public void SetupOrient(OrientMenuManager omm, Noble noble) { //sets orient specific info + regular inventory info, used when filling orient menu with options
        this.omm = omm;
        image.color = Color.gray;
        SetupInventory(noble);
    }

    public void PassToOrient() { //passes noble to orient 
        omm.Setup(noble);
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

    // Sets the noble field to the noble received as input and updates the sprite render to display image of noble
    public void SetNoble(Noble noble) { //only used when displaying nobles on board i believe
        this.noble = noble;
        gameObject.transform.localScale = new UnityEngine.Vector3(0.77f, 0.77f, 0.77f);
        gameObject.GetComponent<Button>().interactable = false;
        m_SpriteRenderer = GetComponent<SpriteRenderer>();
        m_SpriteRenderer.sprite = noble.sprite;
        image.color = Color.clear; //sets canvas image to clear, since this method is used by playerControl and not orientMenuManager (which uses the image for display purposes)
    }

    public Noble GetNoble() {
        return noble;
    }
}
