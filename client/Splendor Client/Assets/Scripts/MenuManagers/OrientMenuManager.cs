using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.Events;

public enum ActionType {
    NONE,
    SACRIFICE,
    SATCHEL,
    RESERVE,
    DOMINO1,
    DOMINO2,
}
public class OrientMenuManager : MonoBehaviour {
    public GameObject blankNobleSlot, blankCardSlot, content;
    public PlayerControl playerControl;
    public Text actionText, errorText;
    public Card currentCard1, currentCard2;
    public Noble currentNoble;
    //public UnityEvent sacrifice, reserve, satchel, domino1, domino2;
    public bool sacrificing = false, dominoSatchel = false;

    //note: deck indexs in allcards is: 0 = vanilla1, 1 = vanilla2, 2 = vanilla3, 3 = orient1, 4 = orient2, 5 = orient3
    //note: all 'domino' cards for level 1 also have satchels on them
    //note: domino cards have access to both orient and vanilla cards of the given level
    //note: some orient cards have 2 gold tokens as 'discounts', essentially the card itself is worth 2 gold tokens. once at least one is used, 
    //      discard card from inventory (so when we implement actual purchasing logic, will need code to 1) a check for this and 2) logic to determing what
    //      gold tokens to use between actual tokens and cards, i.e. to not waste cards)
    //note: for satchels, check if player has a card with a gem discount. if no, dont go through with purchase. (this extends to domino1 cards)
    //note: for sacrifices, must discard with priority cards with a satchel
    //note: satchels occur before every other action. maybe use ienumerator?

    void ClearChildren() {
        foreach (Transform child in content.transform)
            Destroy(child.gameObject);
    }

    void ResetSelections() {
        currentNoble = null;
        currentCard1 = null;
        currentCard2 = null;
        sacrificing = false;
        errorText.text = "";
    }

    public void ResetHighlightedCard() {
        foreach (Transform c in content.transform) {
            if ((c.TryGetComponent(out CardSlot cs) && !(cs.GetCard().Equals(currentCard1) || cs.GetCard().Equals(currentCard2)))
                || (c.TryGetComponent(out NobleSlot ns) && !ns.GetNoble().Equals(currentNoble)))
                c.GetComponent<Image>().color = Color.gray;
            else
                c.GetComponent<Image>().color = Color.white;
        }
    }

    public void Setup(Card card) { //if select same card twice, deselect that card instead
        if (card.Equals(currentCard1)) {
            currentCard1 = currentCard2 ? currentCard2 : null;
            currentCard2 = null;
        }
        else if (card.Equals(currentCard2)) //if reselect second choice, deselect second
            currentCard2 = null;
        else if (sacrificing && currentCard1) {
            if (currentCard1.getBonusAmount() + currentCard1.satchels >= 2 || card.getBonusAmount() + card.satchels >= 2) {
                currentCard1 = card;
                currentCard2 = null;
            }
            else {
                currentCard1 = currentCard2 ? currentCard2 : currentCard1;
                currentCard2 = card;
            }
        }
        else
            currentCard1 = card;
    }

    public void Setup(Noble noble) {
        currentNoble = noble.Equals(currentNoble) ? null : noble;
    }

    //methods for making sacrificial cards, given player inventory and currentCardBonus (check images for corresponding bonus-to-sacrifice)
    public void DisplaySacrificialCards() {
        actionText.text = "Please select which card(s) you wish to expend to acquire the new card.";
        sacrificing = true;
        char desired = ' ';
        List<Object> tempAll = new List<Object>();
        List<Object> tempSatchels = new List<Object>();
        switch (currentCard1.GetBonus()) { //god i wished we used enums for gems. if ordered right, it could be one line of code instead of a switch
            case 'W': desired = 'K'; break;
            case 'R': desired = 'G'; break;
            case 'B': desired = 'W'; break;
            case 'G': desired = 'B'; break;
            case 'K': desired = 'R'; break;
        }

        foreach (Card c in playerControl.client.inventory) {
            if (c.GetBonus() == desired && c.satchels > 0) //prioritizes cards with satchels
                tempSatchels.Add(c);
            else if (tempSatchels.Count == 0 && c.GetBonus() == desired) //if no satchel cards, add cards with desired discount
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
        actionText.text = "Please select which card you wish to receive for free.";
        List<Object> temp = new List<Object>();
        foreach (CardSlot cs in playerControl.allCards.cards[dominoLevel - 1].cards)//vanilla cards (i - 1 = j, index j is level i vanilla cards)
            temp.Add(cs.GetCard());
        foreach (CardSlot cs in playerControl.allCards.cards[dominoLevel + 2].cards)//orient cards (i - 1 + 3 -> i + 2 = j, index j is level i orient)
            temp.Add(cs.GetCard());
        if (temp.Count == 0) { //if no cards to domino, close menu and stop flow of logic
            gameObject.SetActive(false);
            return;
        }
        PopulateMenu(temp);
    }

    //method for displaying cards to add a satchel to
    public void DisplayInventory() {
        actionText.text = "Please select which card you wish to add the bought satchel card to.";
        List<Object> temp = new List<Object>(playerControl.client.inventory);
        temp.RemoveAll(card => (card as Card).GetBonus() == 'J');
        PopulateMenu(temp);
    }

    //method for displaying available nobles to reserve one
    public void DisplayReservableNobles() {
        if (playerControl.allNobles.nobles.Length == 0) { //if no nobles to reserve, close menu and stop flow of logic
            gameObject.SetActive(false);
            return;
        }
        actionText.text = "Please select which noble you wish to reserve.";
        List<Object> temp = new List<Object>();
        foreach (NobleSlot ns in playerControl.allNobles.nobles)
            temp.Add(ns.GetNoble());
        PopulateMenu(temp);
    }

    void PopulateMenu(List<Object> items) {
        ClearChildren();
        ResetSelections();
        foreach (Object i in items) {
            if(i is Card) {
                GameObject temp = Instantiate(blankCardSlot, content.transform.position, Quaternion.identity);
                temp.transform.SetParent(content.transform);
                //temp.transform.localScale = new Vector3(1, 1, 1);
                temp.GetComponent<CardSlot>().SetupOrient(this, i as Card);
            }
            else {
                GameObject temp = Instantiate(blankNobleSlot, content.transform.position, Quaternion.identity);
                temp.transform.SetParent(content.transform);
                //temp.transform.localScale = new Vector3(0.2f, 0.4f, 1);
                temp.GetComponent<NobleSlot>().SetupOrient(this, i as Noble);
            }
        }   
    }
    public void PerformAction(Card card) {
        currentCard1 = card;
        switch (card.action) {
            case ActionType.RESERVE: DisplayReservableNobles(); break;
            case ActionType.SACRIFICE: DisplaySacrificialCards(); break;
            case ActionType.DOMINO2: DisplayDominoCards(2); break;
            case ActionType.DOMINO1: dominoSatchel = true; DisplayInventory(); break; //will need to do satchel part first/as well
            case ActionType.SATCHEL: DisplayInventory(); break;
            case ActionType.NONE: playerControl.inOrientMenu = false; gameObject.SetActive(false); break;
        }
    }

    //perform action given selected card(s)
    public void ConfirmChoice() { //*****confirm that cards for dominos are given to player, and sacrifices are removed*****
        if (currentNoble) {
            playerControl.ReserveNoble(currentNoble);
            playerControl.inOrientMenu = false;
            gameObject.SetActive(false);
        }
        else if (currentCard1) {
            if (dominoSatchel) { //if current card is the pair for a domino1 satchel
                dominoSatchel = false;
                currentCard1.AddSatchel();
                DisplayDominoCards(1);
            }
            else if (sacrificing) { //if this is sacrificial
                if (currentCard1.getBonusAmount() + currentCard1.satchels >= 2 || currentCard2) { //if 2 cards are selected, then it will be enough no matter what
                    playerControl.AcquireCard(currentCard1);
                    playerControl.RemoveCard(currentCard1);
                    if (currentCard2)
                        playerControl.RemoveCard(currentCard2);
                    playerControl.inOrientMenu = false;
                    playerControl.sacrificeMade = true;
                    gameObject.SetActive(false);
                }
                else
                    errorText.text = "Error: No enough discounts selected to purchase this card.";
            }
            else if (playerControl.client.inventory.Contains(currentCard1)) { //if performing satchel action, i.e. of chosen card is in inventory but not sacrificial
                currentCard1.AddSatchel();
                playerControl.inOrientMenu = false;
                gameObject.SetActive(false);
            }
            else {
                playerControl.AcquireCard(currentCard1);
                PerformAction(currentCard1); //perform action for free domino card
            }
        }
        else
            errorText.text = "Error: No Selection made.";
    }
}

//TODO: make turndata or whatever a list of acquired cards instead of a single one, likewise same with nobles (its possible to impress one AND reserve one the same turn)
//TODO: add reserve inventory (visually)
//TODO: if there are no nobles to reserve, or no cards to select for domino, player still acquires the orient card but does not do associated action