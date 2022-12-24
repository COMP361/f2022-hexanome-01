using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.InputSystem;

public class PlayerControl : MonoBehaviour
{
    public Dashboard dashboard;
    [SerializeField] private GameObject cursor;
    [SerializeField] private Camera playerCamera;
    [SerializeField] private Player player; //this client/player
    [SerializeField] public List<PlayerData> gamePlayersData; //can change this to a different type later, playerData is combined from LobbyPlayer and Player class
    [SerializeField] private OrientMenuManager omm;

    public Player client { 
        get { return player; }
    }

    public GlobalGameClient globalGameClient;

    public AllCards allCards;
    public CardSlot selectedCardToBuy;

    public NobleRow allNobles; 

    private InputAction fire;
    private InputAction look;
 
    [SerializeField] private InputActionAsset controls;
    
    private InputActionMap _inputActionMap;
    
    public NetworkManager db;

    public Authentication mainPlayer;

    [SerializeField] private bool waiting;
    public bool inOrientMenu, inInventory, sacrificeMade;

    void Start() {
        foreach(CardRow cr in allCards.cards) { //reset satchel values of all cards to 0, since scriptable objects remembers values between scenes, i.e. between games
            foreach(Card c in cr.deck.cards) { 
                c.satchels = 0;
            }
        }

        //waiting = true;
        //db.InitializePolling(globalGameClient.id, mainPlayer, this);

        selectedCardToBuy = null;
        _inputActionMap = controls.FindActionMap("Player");
    
        fire = _inputActionMap.FindAction("Fire");
        fire.performed += OnFireAction;

        look = _inputActionMap.FindAction("Look");
        look.performed += UpdateCursor;
    }
    
    private void OnFireAction(InputAction.CallbackContext obj)
    {
        if (waiting || inOrientMenu || inInventory) return;

        Vector2 mousePos = Mouse.current.position.ReadValue();
        Vector3 worldPos = playerCamera.ScreenToWorldPoint(mousePos);
        Vector2 worldPos2D = new Vector2(worldPos.x, worldPos.y);
        // Debug.Log(worldPos2D);

        RaycastHit2D hit = Physics2D.Raycast(worldPos2D, Vector2.zero);

        if (hit.collider != null) { // Check what was clicked (excluding UI elements)
            GameObject go = hit.collider.gameObject;
            if (go.CompareTag("Card")) {
                // Debug.Log("Card");
                CardSlot cardSlotObject = go.GetComponent<CardSlot>();
                selectedCardToBuy = cardSlotObject;
                dashboard.DisplayPurchase();
                allCards.GreyOutExcept(cardSlotObject);
            }
            // else if (go.CompareTag ...
        }
    }

    void UpdateDisplay() {
        dashboard.UpdatePtsDisplay(player.GetPoints());
        dashboard.UpdateTokenDisplay(player.GetTokensAquired());
    }

    public bool ReserveCard(Card card) {
        if (player.ReserveCard(card)) {
            UpdateDisplay();
            allCards.RemoveCard(card);
            return true;
        }
        else
            return false;
    }

    public void ReserveNoble(Noble noble) {
        player.ReserveNoble(noble);
        allNobles.RemoveNoble(noble);
    }

    public void AcquireCard(Card card) {
        player.AcquireCard(card);
        UpdateDisplay();
        allCards.RemoveCard(card);
    }

    public void RemoveCard(Card card) {
        player.RemoveCard(card);
        UpdateDisplay();
    }

    bool PurchaseAction() {
        if (selectedCardToBuy && //if a card has been selected AND if not... (i.e. if inventory is empty, cannot purchase satchel or domino1)
            !(player.inventory.Count == 0 && //player's inventory is empty AND the selected card is a satchel or domino1 orient card
            (selectedCardToBuy.GetCard().action == ActionType.SATCHEL || selectedCardToBuy.GetCard().action == ActionType.DOMINO1))) {
            if (selectedCardToBuy.GetCard().action != ActionType.SACRIFICE) {
                if (!player.TriggerCardAdd(selectedCardToBuy.GetCard()))
                    return false;
                UpdateDisplay();
                allCards.RemoveCard(selectedCardToBuy);
            }
            if (selectedCardToBuy.GetCard().action != ActionType.NONE) {
                inOrientMenu = true;
                omm.gameObject.SetActive(true);
                omm.PerformAction(selectedCardToBuy.GetCard());
            }
            return selectedCardToBuy.GetCard().action != ActionType.SACRIFICE;
        }
        else
            return false;
    }
    public void EndTurn() // Player clicks "end turn"
    {
        // Upon turn end, selected card is bought and added to inventory (points increase by card points)
        if(!PurchaseAction() || !sacrificeMade) //i vote for having a dedicated "purchase" button that either ends your turn outright (assuming your purchase goes through)
            return;             // or sets a flag making it so you cannot do another action except for ending turn

        // For each noble in the players reserves, check if they are impressed
        foreach (Noble noble in player.nobleReserves) {
            if (player.hasImpressed(noble)) {
                player.TriggerNobleAdd(noble);
                UpdateDisplay();
                //we need to be able to select a Noble when there are multiple impressed at once instead of just giving the first break;
                break;
            }
        }
        // For each noble in the row check if they are impressed (will also need to check player reserves)
        foreach (NobleSlot noble in allNobles.nobles) {
            if(noble){
                if(player.hasImpressed(noble.GetNoble())){
                    player.TriggerNobleAdd(noble.GetNoble());
                    UpdateDisplay();
                    allNobles.RemoveNoble(noble);
                    //we need to be able to select a Noble when there are multiple impressed at once instead of just giving the first break;
                    break;
                }
            }
        }

        dashboard.DisplayWaiting();
        allCards.GreyOut();

        waiting = true;

        //db.endTurn(globalGameClient.id, player.turnData, mainPlayer, this);

        /////// TEST SAVE GAME AFTER TURN ////////////
        // GameData data = new GameData(this);
        

        //db.UpdateGame(data);
        ///////////////////////////////////////////////


        // StartTurn(); // Player's turn temporarily restarts immediately after end turn
    }

    // public void SetGameData(GameData data) {
    //     gameId = data.gameId;

    //     gamePlayersData = new List<PlayerData>(data.playersInGame);
        
    //     for (int i = 0; i < data.noblesDisplayed.Length; i++)          
    //         allNobles.nobles[i].GetNoble().SetData(data.noblesDisplayed[i]);

    //     // noblesOnBoard[i].GetNoble().SetData(data.noblesDisplayed[i]);
        
    //     for (int i = 0; i < allCards.cards.Length; i++) 
    //         for (int j = 0; j < allCards.cards[i].deck.Count(); j++)
    //             allCards.cards[i].deck.cards[j].SetData(data.cards[i][j]);
    // }

    public void StartTurn() // Start of player's turn
    {
        player.turnData = new TurnData();
        dashboard.ResetEndDisplay();
        allCards.UnGreyOut();
        waiting = false;
        sacrificeMade = false;
        selectedCardToBuy = null;
    }

    private void UpdateCursor(InputAction.CallbackContext obj)
    {
        Vector2 mousePos = Mouse.current.position.ReadValue();
        cursor.transform.position = new Vector3(mousePos.x, mousePos.y, cursor.transform.position.z);
    }
}
