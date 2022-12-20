using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.Events;

public enum ActionType {
    SACRIFICE,
    SATCHEL,
    RESERVE,
    DOMINO1,
    DOMINO2,
    NONE
}
public class OrientMenuManager : MonoBehaviour {
    public GameObject blankNobleSlot, blankCardSlot, content;
    public PlayerControl playerControl;
    public Text actionText;
    public Card currentCard;
    public Noble currentNoble;
    public UnityEvent sacrifice, reserve, satchel, domino1, domino2;

    //note: deck indexs in allcards is: 0 = vanilla1, 1 = vanilla2, 2 = vanilla3, 3 = orient1, 4 = orient2, 5 = orient3
    //note: all 'domino' cards for level 1 also have satchels on them
    //note: domino cards have access to both orient and vanilla cards of the given level
    //note: some orient cards have 2 gold tokens as 'discounts', essentially the card itself is worth 2 gold tokens. once at least one is used, 
    //      discard card from inventory (so when we implement actual purchasing logic, will need code to 1) a check for this and 2) logic to determing what
    //      gold tokens to use between actual tokens and cards, i.e. to not waste cards)
    //note: for satchels, check if player has a card with a gem discount. if no, dont go through with purchase.
    //note: for sacrifices, must discard with priority cards with a satchel

    void ClearChildren() {
        foreach (Transform child in content.transform)
            Destroy(child.gameObject);
    }

    //methods for making sacrificial cards, given player inventory and currentCardBonus (check images for corresponding bonus-to-sacrifice)
    public void DisplaySacrificialCards() {
        char desired = ' ';
        List<Object> tempAll = new List<Object>();
        List<Object> tempSatchels = new List<Object>();
        switch (currentCard.GetBonus()) { //god i wished we used enums for this. if ordered right, it could be one line of code instead of a switch
            case 'W': desired = 'K'; break;
            case 'R': desired = 'G'; break;
            case 'B': desired = 'W'; break;
            case 'G': desired = 'B'; break;
            case 'K': desired = 'R'; break;
        }

        foreach (Card c in playerControl.client.inventory) {
            if (tempSatchels.Count > 0 || (c.GetBonus() == desired && c.satchels > 0)) //prioritizes cards with satchels
                tempSatchels.Add(c);
            else if (c.GetBonus() == desired)
                tempAll.Add(c);
        }

        if (tempSatchels.Count > 0) //if any satchel cards, only display those ones. otherwise, display all aplicable cards
            PopulateMenu(tempSatchels);
        else
            PopulateMenu(tempAll);
    }
    //methods for displaying cards in 2nd exRow for domino2, then does event for next card

    //methods for displaying cards in 1st exRow for domino1, which after new cards is bought then does satchel event for current card, then event for next card
    public void DisplayDominoCards(int dominoLevel) { 
        List<Object> temp = new List<Object>(playerControl.allCards.cards[dominoLevel - 1].cards); //vanilla cards (i - 1 = j, index j is level i vanilla cards)
        temp.AddRange(new List<Object>(playerControl.allCards.cards[dominoLevel + 2].cards)); //orient cards (i - 1 + 3 -> i + 2 = j, index j is level i orient)
        PopulateMenu(temp);
    }

    //method for displaying cards to add a satchel to
    public void DisplayInventory() {
        PopulateMenu(new List<Object>(playerControl.client.inventory));
    }

    //method for displaying available nobles to reserve one
    public void DisplayReservableNobles() {
        PopulateMenu(new List<Object>(playerControl.allNobels.nobles));
    }

    //if we can decide if we cant be consistent in using either Noble/Card or NobleSlot/CardSlot for lists (i.e. player inventory vs NobleRow/CardRow) this can be simplified a bit
    void PopulateMenu(List<Object> items) {
        ClearChildren();
        foreach (Object i in items) {
            if(i is Card) {
                GameObject temp = Instantiate(blankCardSlot, content.transform.position, Quaternion.identity);
                temp.transform.SetParent(content.transform);
                temp.transform.localScale = new Vector3(1, 1, 1);
                temp.GetComponent<CardSlot>().SetupOrient(this, (i as Card));
            }
            if (i is CardSlot) {
                GameObject temp = Instantiate(blankCardSlot, content.transform.position, Quaternion.identity);
                temp.transform.SetParent(content.transform);
                temp.transform.localScale = new Vector3(1, 1, 1);
                temp.GetComponent<CardSlot>().SetupOrient(this, (i as CardSlot));
            }
            else {
                GameObject temp = Instantiate(blankNobleSlot, content.transform.position, Quaternion.identity);
                temp.transform.SetParent(content.transform);
                temp.transform.localScale = new Vector3(1, 1, 1);
                temp.GetComponent<NobleSlot>().SetupOrient(this, i as NobleSlot);
            }
        }   
    }

    public void DetermineAction() {
        if (currentCard) {
            switch (currentCard.action) {
                case ActionType.RESERVE: reserve.Invoke(); break;
                case ActionType.SACRIFICE: sacrifice.Invoke(); break;
                case ActionType.DOMINO2: domino2.Invoke(); break;
                case ActionType.DOMINO1: domino1.Invoke(); break;
                case ActionType.SATCHEL: satchel.Invoke();  break;
                case ActionType.NONE: break;
            }
        }
    }

    // Start is called before the first frame update
    void Start() {

    }
}
