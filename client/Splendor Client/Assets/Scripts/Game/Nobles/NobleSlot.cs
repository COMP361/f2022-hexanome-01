using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class NobleSlot : MonoBehaviour {

    [SerializeField] private Noble noble;
    [SerializeField] private Image image;

    private SpriteRenderer m_SpriteRenderer;

    private bool active = true;

    public void SetupInventory(Noble noble) { //sets all the values for use in inventory
        this.noble = noble;
        m_SpriteRenderer = GetComponent<SpriteRenderer>();
        m_SpriteRenderer.sprite = noble.sprite;
        image.sprite = noble.sprite;
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
        m_SpriteRenderer = GetComponent<SpriteRenderer>();
        m_SpriteRenderer.sprite = noble.sprite;
        image.color = Color.clear; //sets canvas image to clear, since this method is used by playerControl and not orientMenuManager (which uses the image for display purposes)
    }

    public void EmptySlot() {
        noble = null;
        gameObject.SetActive(false);
    }

    public Noble GetNoble() {
        return noble;
    }

   
}
