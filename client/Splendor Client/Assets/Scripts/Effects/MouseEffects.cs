using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.InputSystem;

public class MouseEffects : MonoBehaviour {

    [SerializeField] private GameObject cursor;
    [SerializeField] private Camera uiCamera;

    private InputAction fire;
    private InputAction look;
    [SerializeField] private InputActionAsset controls;
    private InputActionMap _inputActionMap;

    [SerializeField] ObjectPool effectPool;

    void Start() {
        uiCamera.gameObject.SetActive(true);
        _inputActionMap = controls.FindActionMap("Player");

        fire = _inputActionMap.FindAction("Fire");
        fire.performed += OnFireAction;

        //look = _inputActionMap.FindAction("Look");
        //look.performed += UpdateCursor;
    }

    private void OnFireAction(InputAction.CallbackContext obj) {


        Vector2 mousePos = Mouse.current.position.ReadValue();
        Vector3 worldPos = uiCamera.ScreenToWorldPoint(mousePos);
        Vector2 worldPos2D = new Vector2(worldPos.x, worldPos.y);

        RaycastHit2D hit = Physics2D.Raycast(worldPos2D, Vector2.zero);
        if (effectPool != null) {
            GameObject effect = effectPool.GetObject();
            effect.transform.position = worldPos2D;
            effect.SetActive(true);
        }
    }
}
