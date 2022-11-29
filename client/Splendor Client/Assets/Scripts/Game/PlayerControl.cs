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

    public string gameId;
    public int currentPlayer;

    public AllCards allCards;
    private CardSlot selectedCardToBuy;

    public NobleRow allNobels; 

    private InputAction fire;
    private InputAction look;
 
    [SerializeField] private InputActionAsset controls;
    
    private InputActionMap _inputActionMap;
    
    public NetworkManager db;

    private void Start()
    {
        selectedCardToBuy = null;
        _inputActionMap = controls.FindActionMap("Player");
    
        fire = _inputActionMap.FindAction("Fire");
        fire.performed += OnFireAction;

        look = _inputActionMap.FindAction("Look");
        look.performed += UpdateCursor;
    }
    
    private void OnFireAction(InputAction.CallbackContext obj)
    {
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

    public void EndTurn() // Player clicks "end turn"
    {
        // Upon turn end, selected card is bought and added to inventory (points increase by card points)
        if (selectedCardToBuy != null) {
            if (!player.TriggerCardAdd(selectedCardToBuy.GetCard())) return;
            dashboard.UpdatePtsDisplay(player.GetPoints());
            dashboard.UpdateTokenDisplay(player.GetTokensAquired());
            allCards.RemoveCard(selectedCardToBuy);
            selectedCardToBuy = null;
        }
        
        Noble tempNoble = (Noble) ScriptableObject.CreateInstance(typeof(Noble));

        // For each noble in the row check if they are impressed
        foreach(NobleSlot noble in allNobels.nobles){
            if(noble!=null){
                tempNoble = noble.GetNoble();
                if(player.hasImpressed(tempNoble)){
                    player.TriggerNobleAdd(tempNoble);
                    dashboard.UpdatePtsDisplay(player.GetPoints());
                    allNobels.RemoveNoble(noble);

                    //Select Noble when there are multiple impressed at once instead of break;
                    break;
                }
            }
        }

        dashboard.ResetEndDisplay();
        allCards.GreyOut();


        /////// TEST SAVE GAME AFTER TURN ////////////
        GameData data = new GameData(this);
        db.UpdateGame(data);
        ///////////////////////////////////////////////


        StartTurn(); // Player's turn temporarily restarts immediately after end turn
    }

    // public void SetGameData(GameData data) {
    //     gameId = data.gameId;

    //     gamePlayersData = new List<PlayerData>(data.playersInGame);
        
    //     for (int i = 0; i < data.noblesDisplayed.Length; i++)          
    //         allNobels.nobles[i].GetNoble().SetData(data.noblesDisplayed[i]);

    //     // noblesOnBoard[i].GetNoble().SetData(data.noblesDisplayed[i]);
        
    //     for (int i = 0; i < allCards.cards.Length; i++) 
    //         for (int j = 0; j < allCards.cards[i].deck.Count(); j++)
    //             allCards.cards[i].deck.cards[j].SetData(data.cards[i][j]);
    // }

    public void StartTurn() // Start of player's turn
    {
        allCards.UnGreyOut();
    }

    private void UpdateCursor(InputAction.CallbackContext obj)
    {
        Vector2 mousePos = Mouse.current.position.ReadValue();
        cursor.transform.position = new Vector3(mousePos.x, mousePos.y, cursor.transform.position.z);
    }
}
