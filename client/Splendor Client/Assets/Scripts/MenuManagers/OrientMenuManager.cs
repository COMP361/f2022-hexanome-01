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
    [SerializeField] private GameObject blankNobleSlot, blankCardSlot, content, backoutButton; //empty noble/card prefab, content = panel to display options, and button to backout of a sacrifice
    [SerializeField] private PlayerControl playerControl;
    [SerializeField] private Text actionText, errorText; //text boxes
    private Card currentCard1, currentCard2; //selected cards
    private Noble currentNoble; //selected noble
    private bool sacrificing = false, dominoSatchel = false; //flags

    //note: deck indexs in allcards is: 0 = vanilla1, 1 = vanilla2, 2 = vanilla3, 3 = orient1, 4 = orient2, 5 = orient3
    //note: all 'domino' cards for level 1 also have satchels on them
    //note: domino cards have access to both orient and vanilla cards of the given level
    //note: some orient cards have 2 gold tokens as 'discounts', essentially the card itself is worth 2 gold tokens. once at least one is used, 
    //      discard card from inventory (so when we implement actual purchasing logic, will need code to 1. a check for this and 2. logic to determing what
    //      gold tokens to use between actual tokens and cards, i.e. to not waste cards)
    //note: for satchels, check if player has a card with a gem discount. if no, dont go through with purchase. (this extends to domino1 cards)
    //note: for sacrifices, must discard with priority, cards with a satchel
    //note: satchels occur before every other action.

    public void SacrificingStatus() { //flips sacrifice status (needed for use with a button)
        sacrificing = !sacrificing;
    }

    public void MenuStatus() { //flips menu status (needed for use with a button)
        playerControl.inOrientMenu = !playerControl.inOrientMenu;
    }

    void ClearChildren() { //clears all things in the menu
        foreach (Transform child in content.transform)
            Destroy(child.gameObject);
    }

    void ResetSelections() {  //reset selections from previous menu usage
        currentNoble = null;
        currentCard1 = null;
        currentCard2 = null;
        errorText.text = "";
    }

    public void ResetHighlightedCard() { //highlight/lowlight cards depending on if theyre selected
        foreach (Transform c in content.transform) { //for all displayed elemenets
            if ((c.TryGetComponent(out CardSlot cs) && !(cs.GetCard().Equals(currentCard1) || cs.GetCard().Equals(currentCard2))) //if it is a card AND NOT selected, OR if it is a noble AND NOT selected, lowlight it
                || (c.TryGetComponent(out NobleSlot ns) && !ns.GetNoble().Equals(currentNoble)))
                c.GetComponent<Image>().color = Color.gray;
            else //otherwise, card/noble is selected so highlight it
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

    public void Setup(Noble noble) { //selects/deselects the passed noble
        currentNoble = noble.Equals(currentNoble) ? null : noble;
    }

    //methods for making sacrificial cards, given player inventory and currentCardBonus (check images for corresponding bonus-to-sacrifice)
    public void DisplaySacrificialCards() {
        actionText.text = "Please select which card(s) you wish to expend to acquire the new card.";
        char desired = '\0';
        List<Object> tempAll = new List<Object>(); //list for all cards with desired bonus
        List<Object> tempSatchels = new List<Object>(); //list for all cards with desired bonus AND satchels
        switch (currentCard1.GetBonus()) { //god i wished we used enums for gems. if ordered right, it could be one line of code instead of a switch
            case 'W': desired = 'K'; break; //sets desired bonus
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
        //NOTE: could probably do this using a lambda expression to just filter cards out, might be more succinct
        if (tempSatchels.Count > 0) //if any satchel cards, only display those ones. otherwise, display all applicable cards
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
            MenuStatus();
            gameObject.SetActive(false);
            return;
        }
        PopulateMenu(temp);
    }

    //method for displaying cards to add a satchel to
    public void DisplayInventory() {
        actionText.text = "Please select which card you wish to add the bought satchel card to.";
        List<Object> temp = new List<Object>(playerControl.client.inventory);
        temp.RemoveAll(card => (card as Card).GetBonus() == 'J' || (card as Card).action == ActionType.SATCHEL || (card as Card).action == ActionType.DOMINO1);
        PopulateMenu(temp);
    }

    //method for displaying available nobles to reserve one
    public void DisplayReservableNobles() {
        if (playerControl.allNobles.IsEmpty()) { //if no nobles to reserve, close menu and stop flow of logic
            MenuStatus();
            gameObject.SetActive(false);
            return;
        }
        actionText.text = "Please select which noble you wish to reserve.";
        List<Object> temp = new List<Object>();
        foreach (NobleSlot ns in playerControl.allNobles.nobles)
            if (ns)
                temp.Add(ns.GetNoble());
        PopulateMenu(temp);
    }

    void PopulateMenu(List<Object> items) { //fill the menu with the objects passed in the list
        ClearChildren(); //first clear menu of all remnants of previous populating
        ResetSelections(); //reset all card selections since this is a new selection
        foreach (Object i in items) { //for all objects in the list
            if (i is Card) { //if the object is a card
                GameObject temp = Instantiate(blankCardSlot, content.transform.position, Quaternion.identity);
                temp.transform.SetParent(content.transform);
                //temp.transform.localScale = new Vector3(1, 1, 1);
                temp.GetComponent<CardSlot>().SetupOrient(this, i as Card);
            }
            else { //otherwise, object is a noble
                GameObject temp = Instantiate(blankNobleSlot, content.transform.position, Quaternion.identity);
                temp.transform.SetParent(content.transform);
                //temp.transform.localScale = new Vector3(0.2f, 0.4f, 1);
                temp.GetComponent<NobleSlot>().SetupOrient(this, i as Noble);
            }
        }
    }
    public void PerformAction(Card card) {
        currentCard1 = card; //temporarily set currentCard1 to the action card, only necessary for sacrifices so it knows what discount is needed
        switch (card.action) { //perform action that card specifies
            case ActionType.RESERVE: DisplayReservableNobles(); break;
            case ActionType.SACRIFICE: SacrificingStatus(); backoutButton.SetActive(true); DisplaySacrificialCards(); break;
            case ActionType.DOMINO2: DisplayDominoCards(2); break;
            case ActionType.DOMINO1: dominoSatchel = true; DisplayInventory(); break; //will need to do satchel part first/as well
            case ActionType.SATCHEL: DisplayInventory(); break;
            case ActionType.NONE: MenuStatus(); gameObject.SetActive(false); break;
        }
    }

    //perform action given selected card(s)
    public void ConfirmChoice() { //confirm button logic
        if (currentNoble) { //if a noble is selected, reserve it and close menu
            playerControl.ReserveNoble(currentNoble);
            MenuStatus();
            gameObject.SetActive(false);
        }
        else if (currentCard1) { //otehrwise, if at least 1 card is selected
            if (dominoSatchel) { //if current card is the pair for a domino1 satchel, add satchel to card and do domino action
                dominoSatchel = false;
                currentCard1.AddSatchel();
                playerControl.client.bonusesAquired.ChangeGemAmount(currentCard1.GetBonus(), 1);
                DisplayDominoCards(1);
            }
            else if (sacrificing) { //if this is sacrificial
                if (currentCard1.getBonusAmount() + currentCard1.satchels >= 2 || currentCard2) { //if 2 cards are selected, then it will be enough no matter what
                    playerControl.AcquireCard(playerControl.selectedCardToBuy.GetCard());
                    playerControl.RemoveCard(currentCard1);
                    if (currentCard2)
                        playerControl.RemoveCard(currentCard2);
                    playerControl.inOrientMenu = false;
                    playerControl.sacrificeMade = true;
                    SacrificingStatus();
                    gameObject.SetActive(false);
                }
                else //error message for insufficient sacrifice
                    errorText.text = "Error: No enough discounts selected to purchase this card.";
            }
            else if (playerControl.client.inventory.Contains(currentCard1)) { //if performing satchel action, i.e. if chosen card is in inventory but we're not sacrificing
                currentCard1.AddSatchel();
                playerControl.client.bonusesAquired.ChangeGemAmount(currentCard1.GetBonus(), 1);
                MenuStatus();
                gameObject.SetActive(false);
            }
            else { //otherwise, perform domino actions (i.e. add selected card to inventory for free, and perform corresponding action)
                playerControl.AcquireCard(currentCard1);
                PerformAction(currentCard1);
            }
        }
        else //error message if nothing is selected
            errorText.text = "Error: No Selection made.";
    }
}