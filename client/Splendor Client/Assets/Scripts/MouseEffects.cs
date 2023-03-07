using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.InputSystem;

public class MouseEffects : MonoBehaviour {

    [SerializeField] private GameObject cursor;
    [SerializeField] private Camera uiCamera;
    [SerializeField] private GameObject effectPanel;

    private InputAction fire;
    private InputAction look;
    [SerializeField] private InputActionAsset controls;
    private InputActionMap _inputActionMap;

    [SerializeField] GameObject clickEffect;

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
        if (clickEffect != null) {
            if(effectPanel.transform.childCount > 0)
                Destroy(effectPanel.transform.GetChild(0).gameObject);
            GameObject go = Instantiate(clickEffect, worldPos2D, Quaternion.identity);
            go.transform.SetParent(effectPanel.transform);
            go.transform.localScale = new Vector3(100, 100, 100);
            go.GetComponent<ParticleSystem>().Play();
        }
    }
}
