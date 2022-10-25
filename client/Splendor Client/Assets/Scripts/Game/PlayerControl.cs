using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.InputSystem;

public class PlayerControl : MonoBehaviour
{
    [SerializeField] private GameObject cursor;
    [SerializeField] private Camera playerCamera;
    [SerializeField] private Player player;

    private InputAction fire;
    private InputAction look;
 
    [SerializeField] private InputActionAsset controls;
    
    private InputActionMap _inputActionMap;
    
    private void Start()
    {
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

        if (hit.collider != null) {
            GameObject go = hit.collider.gameObject;
            if (go.CompareTag("Card")) {
                // Debug.Log("Card");
                Card cardObject = go.GetComponent<Card>();
                player.TriggerPointsAdd(cardObject);
            }
        }
    }

    private void UpdateCursor(InputAction.CallbackContext obj)
    {
        Vector2 mousePos = Mouse.current.position.ReadValue();
        cursor.transform.position = new Vector3(mousePos.x, mousePos.y, cursor.transform.position.z);
    }
}
