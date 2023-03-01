using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class CitySlot : MonoBehaviour {
    [SerializeField] private City city;
    [SerializeField] private Image image;

    private SpriteRenderer m_SpriteRenderer;

    private bool active = true;

    public void SetupInventory(City city) { //sets all the values for use in inventory
        this.city = city;
        m_SpriteRenderer = GetComponent<SpriteRenderer>();
        m_SpriteRenderer.sprite = city.GetSprite();
        image.sprite = city.GetSprite();
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

    // Sets the city field to the city received as input and updates the sprite render to display image of city
    public void SetCity(City city) { //only used when displaying cities on board i believe
        this.city = city;
        gameObject.transform.localScale = new UnityEngine.Vector3(0.77f, 0.77f, 0.77f);
        gameObject.GetComponent<Button>().interactable = false;
        m_SpriteRenderer = GetComponent<SpriteRenderer>();
        m_SpriteRenderer.sprite = city.GetSprite();
        image.color = Color.clear; //sets canvas image to clear, since this method is used by playerControl and not orientMenuManager (which uses the image for display purposes)
    }

    public void EmptySlot() {
        city = null;
        gameObject.SetActive(false);
    }

    public City GetCity() {
        return city;
    }
    
}
