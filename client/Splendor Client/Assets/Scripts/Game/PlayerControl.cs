using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.InputSystem;

public class PlayerControl : MonoBehaviour
{
    public Dashboard dashboard;
    [SerializeField] private GameObject cursor;
    [SerializeField] private Camera playerCamera;
    [SerializeField] private Player player;

    public CardRow allCards;
    private CardSlot selectedCardToBuy;

    public NobleRow allNobels; 
    public List<NobleSlot> noblesOnBoard = new List<NobleSlot>();
    
    private InputAction fire;
    private InputAction look;
 
    [SerializeField] private InputActionAsset controls;
    
    private InputActionMap _inputActionMap;
    
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
            player.TriggerCardAdd(selectedCardToBuy.GetCard());
            dashboard.UpdatePtsDisplay(player.GetPoints());
            allCards.RemoveCard(selectedCardToBuy);
            selectedCardToBuy = null;
        }

                allNobels = GameObject.Find("NobleRow").GetComponent<NobleRow>();


        //Get each slot from row of nobels
        foreach(NobleSlot nobleSlot in allNobels.GetAllNobels()){

            if(nobleSlot!=null){
             noblesOnBoard.Add(nobleSlot);

            }
        }
        



        Noble tempNoble = (Noble) ScriptableObject.CreateInstance(typeof(Noble));


        // For each noble in the row check if they are impressed
        foreach(NobleSlot noble in noblesOnBoard){

            if(noble!=null){
                tempNoble = noble.GetNoble();
                if(player.GetGreen() >= tempNoble.green && player.GetRed() >= tempNoble.red && player.GetBrown() >= tempNoble.brown && player.GetBlue() >= tempNoble.blue && player.GetWhite() >= tempNoble.white){
                    player.TriggerNobleAdd(tempNoble);
                    dashboard.UpdatePtsDisplay(player.GetPoints());
                    allNobels.RemoveNoble(noble);

                    break;
                }
            }
        }



        dashboard.ResetEndDisplay();
        allCards.GreyOut();
        StartTurn(); // Player's turn temporarily restarts immediately after end turn
    }

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
