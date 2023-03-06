using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class OrientPanelManager : MonoBehaviour
{
    [SerializeField] private GameObject panel;

    private PlayerControl playerControl;

    [SerializeField] private GameObject cardSlot; //Blank card prefab
    [SerializeField] private GameObject nobleSlot; //Blank noble prefab
    [SerializeField] private GameObject cardContent, nobleContent; 

    [SerializeField] private long selectedCard;
    [SerializeField] private long selectedNoble;

    public void Display(List<Card> cards, List<Noble> nobles){
        panel.SetActive(true);
        DisplayPlayerCards(cards, nobles);
    }

    public void DisplayPlayerCards(List<Card> cards, List<Noble> nobles) { //displays acquired cards/nobles
        ClearChildren(cardContent, nobleContent);
        if(cards.Count > 0){
            cardContent.SetActive(true);
            foreach (Card card in cards) {
                GameObject temp = Instantiate(cardSlot, cardContent.transform.position, Quaternion.identity);
                long x = card.GetId();
                temp.transform.GetComponent<Button>().onClick.AddListener(delegate {SelectCard(x);});
                temp.transform.SetParent(cardContent.transform);
                //temp.transform.localScale = new Vector3(1, 1, 1);
                temp.GetComponent<CardSlot>().SetupInventory(card);
            }
        }
        if(nobles.Count > 0){
            nobleContent.SetActive(true);
            foreach (Noble noble in nobles) {
                GameObject nobleInstance = Instantiate(nobleSlot, nobleContent.transform.position, Quaternion.identity);
                nobleInstance.transform.SetParent(nobleContent.transform);
                //nobleInstance.transform.localScale = new Vector3(0.2f, 0.4f, 1f);
                nobleInstance.GetComponent<NobleSlot>().SetupInventory(noble);
            }
        }
    }

    public void SelectCard(long selected){
        selectedCard = selected;
        selectedNoble = -1;
    }

    public void SelectNoble(long selected){
        selectedNoble = selected;
        selectedCard = -1;
    }

    public void Select(){
        if(selectedCard != -1){
            playerControl.dominoCardAction(selectedCard);
            panel.SetActive(false);
        }
    }

    private void ClearChildren(GameObject parent, GameObject nobleParent) { //Need to clear children to not repeat cards everytime we open and close the panel
        foreach (Transform child in parent.transform) {
            Destroy(child.gameObject);
        }
        parent.SetActive(false);
        foreach (Transform child in nobleParent.transform) {
            Destroy(child.gameObject);

        }
        nobleParent.SetActive(false);
    }
}
