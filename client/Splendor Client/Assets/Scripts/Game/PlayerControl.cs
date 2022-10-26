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
    private Card selectedCardToBuy;

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
                Card cardObject = go.GetComponent<Card>();
                selectedCardToBuy = cardObject;
                dashboard.DisplayPurchase();
                allCards.GreyOutExcept(cardObject);
            }
            // else if (go.CompareTag ...
        }
    }

    public void EndTurn() // Player clicks "end turn"
    {
        // Upon turn end, selected card is bought and added to inventory (points increase by card points)
        if (selectedCardToBuy != null) {
            player.TriggerPointsAdd(selectedCardToBuy);
            dashboard.UpdatePtsDisplay(player.GetPoints());
            selectedCardToBuy = null;
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
