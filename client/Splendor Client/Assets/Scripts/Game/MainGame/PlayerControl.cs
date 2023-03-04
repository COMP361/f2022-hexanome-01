using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.InputSystem;

public class PlayerControl : MonoBehaviour {
    public Authentication mainPlayer;
    public Dashboard dashboard;
    [SerializeField] private GameObject cursor;
    [SerializeField] private Camera playerCamera;
    [SerializeField] private Player player; //this client/player
    [SerializeField] public List<string> gamePlayersData; //can change this to a different type later, playerData is combined from LobbyPlayer and Player class
    [SerializeField] private TokenBank tokenBank;
    [SerializeField] private SelectedTokens selectedTokens;

    public AllCards allCards;
    public CardSlot selectedCardToBuy;

    public NobleRow allNobles;

    private InputAction fire;
    private InputAction look;
    [SerializeField] private InputActionAsset controls;
    private InputActionMap _inputActionMap;

    [SerializeField] private bool waiting;

    public bool inOrientMenu, inInventory, sacrificeMade, inNobleMenu;
    private ActionManager actionManager;
    public Session currSession;

    void Start() {
        //the following was a test i made to make sure JSONHandler was working. ive left it here incase we find some uknown error with it
        /*
        Dictionary<string, string> objMapping = new Dictionary<string, string>();
        objMapping.Add("points", "5");
        objMapping.Add("price", "6$");
        JSONObject json = JSONHandler.EncodeJsonRequest("item", objMapping);
        JSONObject decodedJson = (JSONObject)JSONHandler.DecodeJsonRequest(json.ToString());
        JSONObject objDecoded = (JSONObject) JSONHandler.DecodeJsonRequest(decodedJson["parameters"].ToString());
        Debug.Log(decodedJson["parameters"]);
        Debug.Log(decodedJson["type"]);
        Debug.Log(objDecoded["points"]);
        Debug.Log(objDecoded["price"]);

        ArrayList arrMapping = new ArrayList();
        arrMapping.Add("5");
        arrMapping.Add("6");
        JSONArray json2 = JSONHandler.EncodeJsonRequest("item", arrMapping);
        JSONArray decodedJson2 = (JSONArray)JSONHandler.DecodeJsonRequest(json2.ToString());
        JSONArray arrDecoded = (JSONArray)JSONHandler.DecodeJsonRequest(decodedJson2[1].ToString());
        Debug.Log(decodedJson2[0]);
        Debug.Log(arrDecoded[0]);
        Debug.Log(arrDecoded[1]);

        Dictionary<string, string> objMapping1 = new Dictionary<string, string>();
        Dictionary<string, string> objMapping2 = new Dictionary<string, string>();
        ArrayList arrMapping2 = new ArrayList();

        objMapping1.Add("points", "5");
        objMapping1.Add("price", "6$");
        objMapping2.Add("points", "8");
        objMapping2.Add("price", "12$");
        JSONObject jsonObj1 = JSONHandler.EncodeJsonRequest("item", objMapping1);
        JSONObject jsonObj2 = JSONHandler.EncodeJsonRequest("item", objMapping2);

        arrMapping2.Add(jsonObj1.ToString());
        arrMapping2.Add(jsonObj2.ToString());
        JSONArray jsonArr = JSONHandler.EncodeJsonRequest("inventory", arrMapping2);

        JSONArray decodedArr = (JSONArray)JSONHandler.DecodeJsonRequest(jsonArr.ToString());
        JSONArray arrElements = (JSONArray)JSONHandler.DecodeJsonRequest(decodedArr[1].ToString());
        JSONObject obj1Decoded = (JSONObject)
            JSONHandler.DecodeJsonRequest(arrElements[0].ToString());
        JSONObject obj2Decoded = (JSONObject)
            JSONHandler.DecodeJsonRequest(arrElements[1].ToString());
        JSONObject obj1Params = (JSONObject)
            JSONHandler.DecodeJsonRequest(obj1Decoded["parameters"].ToString());
        JSONObject obj2Params = (JSONObject)
            JSONHandler.DecodeJsonRequest(obj2Decoded["parameters"].ToString());

        Debug.Log(decodedArr[0]);
        Debug.Log(obj1Decoded["type"]);
        Debug.Log(obj2Decoded["type"]);
        Debug.Log(obj1Params["points"]);
        Debug.Log(obj2Params["price"]);
        */

        selectedCardToBuy = null;
        _inputActionMap = controls.FindActionMap("Player");

        fire = _inputActionMap.FindAction("Fire");
        fire.performed += OnFireAction;

        look = _inputActionMap.FindAction("Look");
        look.performed += UpdateCursor;
    }

    public Player client
    {
        get { return player; }
    }

    private void OnFireAction(InputAction.CallbackContext obj) {
        if (waiting || inInventory) return;

        Vector2 mousePos = Mouse.current.position.ReadValue();
        Vector3 worldPos = playerCamera.ScreenToWorldPoint(mousePos);
        Vector2 worldPos2D = new Vector2(worldPos.x, worldPos.y);

        //Debug.Log(worldPos2D);

        RaycastHit2D hit = Physics2D.Raycast(worldPos2D, Vector2.zero);

        if (hit.collider != null) { // Check what was clicked (excluding UI elements)
            GameObject go = hit.collider.gameObject;
            if (go.CompareTag("Card")) {

                //Debug.Log("Card");
                CardSlot cardSlotObject = go.GetComponent<CardSlot>();
                selectedCardToBuy = cardSlotObject;
                dashboard.DisplayPurchase();
                allCards.GreyOutExcept(cardSlotObject);
            }
            // else if (go.CompareTag ...
            // else if (go.CompareTag("Token")){
            //     Debug.Log("Token");
            //     TokenSlot tokenSlotObject = go.GetComponent<TokenSlot>();
            //     dashboard.DisplayTakeTokens();
            // }
            //else{Debug.Log(go.tag);}
        }
    }

    bool PurchaseAction() { //attempt to purchase a card
        return false;
    }


    public void purchaseCardAction(){
        JSONObject selectedCardJson = new JSONObject();
        json.Add("playerId", player.GetUsername());
        json.Add("cardId", selectedCardToBuy.GetCard().GetId());
        actionManager.MakeApiRequest(currSession.id, selectedCardJson, ActionType.performCardPurchase, (response) => {

            if(response != null){
                string status = response.Get("status");
                string action = response.Get("action");
                int[] choices = response.Get("choices");
                JSONArray jsonNoblesVisited= response.GetArray("noblesVisiting");
                int[] noblesVisiting = jsonNoblesVisited.ToIntArray();

                switch (action)
                {
                    case "none":
                        Console.WriteLine("none");
                        break;
                    case "DOMINO1":
                        Console.WriteLine("Domino1");
                        break;  
                    case "DOMINO2":
                        Console.WriteLine("Domino2");
                        break;
                    case "SATCHEL":
                        Console.WriteLine("Satchel");
                        break;
                    case "SACRAFICE":
                        Console.WriteLine("Sacrafice");
                        break;
                    default:
                        Console.WriteLine("Invalid Action");
                        break;
                }

            }
        });
    }

    public void selectNobleAction(Noble chosenNoble){
        JSONObject selectNobleJson = new JSONObject();
        selectNobleJson.Add("playerId", player.GetUsername());
        selectNobleJson.Add("nobleId", chosenNoble.GetId());
        actionManager.MakeApiRequest(currSession.id, selectedCardJson, ActionType.selectNobleAction, (response) => {

            if(response != null){
                string status = response.Get("status");


                if(status == "success"){
                    // Add selectedNobleToInventory
                }else{
                    // Handle failed status
                }

            }else{
                //Handle null return
            }


        });
        
    }

    public void takeTokensAction(){
        JSONObject chosenTokensJson = new JSONObject();
        chosenTokensJson.Add("player", player.GetUsername());
        string[] tokenColours = selectedTokens.colours.toArray();
        chosenTokensJson.Add("tokens", tokenColours);
        actionManager.MakeApiRequest(currSession.id, chosenTokensJson, ActionType.takeTokensAction, (response) => {
            if(response != null){
                string status = response.Get("status");
                int overFlowAmount = response.Get("tokenOverFlow");
                if(status == "success"){
                    if(overFlowAmount == 0){
                        // Handle removal of selected tokens
                    }else{
                        // Handle too many tokens
                    }
                }else{
                    // Handle Unsuccessful return
                }

            }else{
                // Handle null response from server
            }



        });

    }

    public void reserveCardAction(){
        JSONObject reserveCardJson = new JSONObject();
        reserveCardJson.Add("playerId", player.GetUsername());
        reserveCardJson.Add("cardId", selectedCardToBuy.GetCard().GetId());
        actionManager.MakeApiRequest(currSession.id, reserveCardJson, ActionType.reserveCardAction, (response) => {
            if(response != null){
                string status = response.Get("status");
                if(status == "success"){
                    // Add to reserve cards
                }else{
                    // Handle reserve card failure
                }
                

            }else{
                // Handle null return
            }
        });
        
    }


    public void EndTurn() // Player clicks "end turn"
    { }

    public void StartTurn() // Start of player's turn
    {
        dashboard.ResetEndDisplay();
        waiting = false;
        selectedCardToBuy = null;
    }

    private void UpdateCursor(InputAction.CallbackContext obj) {
        Vector2 mousePos = Mouse.current.position.ReadValue();
        cursor.transform.position = new Vector3(mousePos.x, mousePos.y, cursor.transform.position.z);
    }
}
