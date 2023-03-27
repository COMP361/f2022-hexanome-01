using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEditor;

public class OrientPanelManager : MonoBehaviour {
    [SerializeField] private GameObject panel;

    [SerializeField] private PlayerControl playerControl;

    [SerializeField] private GameObject cardSlot; //Blank card prefab
    [SerializeField] private GameObject nobleSlot; //Blank noble prefab
    [SerializeField] private GameObject cardContent, nobleContent;

    [SerializeField] private long selectedCard = -1, secondCard = -1, purchasingCard = -1;
    [SerializeField] private long selectedNoble = -1;
    [SerializeField] private ActionManager.ActionType action;
    [SerializeField] private List<Card> cardsDebug;

    public void PassOriginalCard(long cardId) {
        purchasingCard = cardId;
    }

    public void Display(List<Card> cards, List<Noble> nobles, ActionManager.ActionType _action) {
            cardsDebug = cards;
            this.action = _action;
            panel.SetActive(true);
            Debug.Log(cards.Count);
            DisplayPlayerCards(cards, nobles);
    }

    public void DisplayPlayerCards(List<Card> cards, List<Noble> nobles) { //displays acquired cards/nobles
        ClearChildren(cardContent, nobleContent);
        if (cards.Count > 0) {
            cardContent.SetActive(true);
            foreach (Card card in cards) {
                GameObject temp = Instantiate(cardSlot, cardContent.transform.position, Quaternion.identity);
                long x = card.GetId();
                temp.transform.GetComponent<Button>().onClick.AddListener(() => SelectCard(x));
                temp.transform.SetParent(cardContent.transform);
                temp.transform.localScale = new Vector3(1, 1, 1);
                temp.GetComponent<CardSlot>().SetupInventory(card);
            }
        }
        if (nobles.Count > 0) {
            cardContent.SetActive(true);
            foreach (Noble noble in nobles) {
                GameObject temp = Instantiate(nobleSlot, cardContent.transform.position, Quaternion.identity);
                long x = noble.id;
                temp.transform.GetComponent<Button>().onClick.AddListener(() => SelectCard(x));
                temp.transform.SetParent(cardContent.transform);
                temp.transform.localScale = new Vector3(1, 1, 1);
                temp.GetComponent<NobleSlot>().SetupInventory(noble);
            }
        }
    }

    public void SelectCard(long selected) {
        if (purchasingCard != -1) { //if doing sacrifice action
            if (secondCard == selected) //if reselect the second option, deselect it
                secondCard = -1;
            else if (selectedCard == selected) //if reselect first option, deselect it
                selectedCard = -1;
            else if (selectedCard == -1) //if no first card selected, it becomes selected
                selectedCard = selected;
            else if (secondCard == -1) //if no second card selected, it becomes selected
                secondCard = selected;
        } //if both options already selected, need to deselect one first
        else //if not sacrifice, do same thing as it was doing before my changes
            selectedCard = selected;
        selectedNoble = -1;
    }

    public void SelectNoble(long selected) {
        selectedNoble = selected;
        selectedCard = -1;
    }

    public void Select() {
        if (selectedCard != -1 && action == ActionManager.ActionType.domino) {
            playerControl.dominoCardAction(selectedCard);
            panel.SetActive(false);
        }
        else if (selectedCard != -1 && action == ActionManager.ActionType.satchel) {
            playerControl.satchelAction(selectedCard);
            panel.SetActive(false);
        }
        else if (selectedCard != -1 && action == ActionManager.ActionType.dominoSatchel) {
            playerControl.dominoSatchelAction(selectedCard);
            panel.SetActive(false);
        }
        else if (selectedCard != -1 && action == ActionManager.ActionType.reserveNoble) {
            playerControl.reserveNobleAction(selectedCard);
            panel.SetActive(false);
        }
        else if ((selectedCard != -1 || secondCard != -1 ) && action == ActionManager.ActionType.sacrifice) {
            playerControl.sacrificeCardAction(selectedCard, secondCard, purchasingCard);
            purchasingCard = -1; //this may lead to an error somewhere, might need to be moved
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
