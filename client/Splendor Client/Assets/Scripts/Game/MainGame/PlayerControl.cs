using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.InputSystem;
using UnityEngine.UI;
using System.Linq;
public class PlayerControl : MonoBehaviour {
    public Authentication mainPlayer;
    public Dashboard dashboard;
    [SerializeField] private GameObject inventoryPanel;
    [SerializeField] private GameObject cursor, purchaseOrReserve, nobleSelectButton;
    [SerializeField] private Camera playerCamera;
    [SerializeField] private Player player; //this client/player
    [SerializeField] public List<string> gamePlayersData; //can change this to a different type later, playerData is combined from LobbyPlayer and Player class
    [SerializeField] private TokenBank tokenBank;
    [SerializeField] private SelectedTokens selectedTokens;
    [SerializeField] private GameObject takeTokensButton;

    public Text errorText;

    public AllCards allCards;
    public CardSlot selectedCard;
    public CardSlot selectedCardToBuy;
    public CardSlot selectedCardToReserve;

    public NobleRow allNobles;
    public NobleSlot selectedNoble;
    [SerializeField] private ClaimNoblePanel claimNoblePanel;

    public OrientPanelManager orientPanelManager;

    private InputAction fire;
    private InputAction look;
    [SerializeField] private InputActionAsset controls;
    private InputActionMap _inputActionMap;

    [SerializeField] private bool waiting;

   
    [SerializeField] private ActionManager actionManager;
    public ActiveSession currSession;
    public bool inOrientMenu, sacrificeMade, inNobleMenu, selectReserve;

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
        selectedCardToReserve = null;
        selectedNoble = null;
        _inputActionMap = controls.FindActionMap("Player");

        fire = _inputActionMap.FindAction("Fire");
        fire.performed += OnFireAction;

        look = _inputActionMap.FindAction("Look");
        //look.performed += UpdateCursor;
    }

    public Player client
    {
        get { return player; }
    }

    private void OnFireAction(InputAction.CallbackContext obj) {

        if (waiting || inventoryPanel.activeInHierarchy) return;

        Vector2 mousePos = Mouse.current.position.ReadValue();
        Vector3 worldPos = playerCamera.ScreenToWorldPoint(mousePos);
        Vector2 worldPos2D = new Vector2(worldPos.x, worldPos.y);

        //Debug.Log(worldPos2D);

        RaycastHit2D hit = Physics2D.Raycast(worldPos2D, Vector2.zero);

        if (hit.collider != null) { // Check what was clicked (excluding UI elements)
            GameObject go = hit.collider.gameObject;
            if (go.CompareTag("Card")) {

                //Debug.Log("Card");
                //Debug.Log(selectReserve);
                purchaseOrReserve.SetActive(true);
                selectedCard = go.GetComponent<CardSlot>();
                allCards.GreyOutExcept(selectedCard);
                /*CardSlot cardSlotObject = go.GetComponent<CardSlot>();
                allCards.GreyOutExcept(cardSlotObject);
                if (!selectReserve){
                    selectedCardToBuy = cardSlotObject;
                    dashboard.DisplayPurchase();
                    Debug.Log("select purchase");
                }
                else{
                    selectedCardToReserve = cardSlotObject;
                    dashboard.DisplayReserve();
                    Debug.Log("select reserve");
                }*/
            }
            /*else{
                allCards.UnGreyOut();
                selectedCardToBuy = null;
                selectedCardToReserve = null;
                purchaseOrReserve.SetActive(false);
                dashboard.DisplayWaiting();
            }*/
            // else if (go.CompareTag ...
            if (go.CompareTag("Noble")) {
                selectedNoble = go.GetComponent<NobleSlot>();
                nobleSelectButton.SetActive(true);
            }
        }
    }

    bool PurchaseAction() { //attempt to purchase a card
        return false;
    }

    void endTurnAction() {
        actionManager.MakeApiRequest(currSession.id, null, ActionManager.ActionType.endTurn, ActionManager.RequestType.POST, (response) => {

            if (response != null && ((string)response["status"]).Equals("success")) {
                errorText.GetComponent<FadeOut>().CompleteFade();
            }

        });
    }


    public void purchaseCardAction(){
        Dictionary<string, object> requestDict = new Dictionary<string, object>();
        JSONObject selectedCardJson = new JSONObject(requestDict);
        selectedCardJson.Add("playerId", player.GetUsername());
        selectedCardJson.Add("cardId", selectedCardToBuy.GetCard().GetId());
        Debug.Log(currSession);
        Debug.Log(selectedCardJson);
        actionManager.MakeApiRequest(currSession.id, selectedCardJson, ActionManager.ActionType.purchaseCard,ActionManager.RequestType.POST, (response) => {

            if(response != null)
            {
                UnityEngine.Debug.Log(response.ToJSONString());
                string status = (string)response["status"];

                if (status.Equals("failure")) {
                    errorText.GetComponent<FadeOut>().ResetFade();
                    return;
                };

                string action = (string)response["action"];
                JSONArray jsonNoblesVisited = (JSONArray)response["noblesVisiting"];
                
                long[] noblesVisiting = new long[jsonNoblesVisited.Count];
                for (int i = 0; i < jsonNoblesVisited.Count; i++) {
                    noblesVisiting[i] = (long)jsonNoblesVisited[i];
                }
                if(action.Equals("domino1") || action.Equals("domino2") || action.Equals("satchel") || action.Equals("reserve")){
                    Debug.Log("GOT TO ORIENT");
                    JSONArray jsonChoices = (JSONArray)JSONHandler.DecodeJsonRequest((string)response["options"]);
                    if(action.Equals("reserve")){
                        List<long> ids = new List<long>();
                        for(int i = 0; i < jsonChoices.Count; i++){
                            ids.Add((long)jsonChoices[i]);
                        }
                        List<Noble> nobleChoices = getNobles(ids);
                        orientPanelManager.gameObject.SetActive(true);
                        orientPanelManager.Display(new List<Card>(), nobleChoices, ActionManager.ActionType.reserveNoble);
                    }
                    else{
                        List<Card> cardChoices = new List<Card>();
                        for (int i = 0; i < jsonChoices.Count; i++) {
                            cardChoices.Add(allCards.GetCardFromId((long)jsonChoices[i]));
                        }
                        orientPanelManager.gameObject.SetActive(true);
                        if(action.Equals("domino2")){
                            orientPanelManager.Display(cardChoices, new List<Noble>(), ActionManager.ActionType.domino);
                        }
                        else if(action.Equals("domino1")){
                            orientPanelManager.Display(cardChoices, new List<Noble>(), ActionManager.ActionType.dominoSatchel);
                        }
                        else if(action.Equals("satchel")){
                            Debug.Log(cardChoices.Count);
                            orientPanelManager.Display(cardChoices, new List<Noble>(), ActionManager.ActionType.satchel);
                        }
                    }

                }
                else if (noblesVisiting.Count() == 0) {
                    endTurnAction();
                }

                else {
                    claimNoblePanel.DisplayNobleClaim(allNobles, noblesVisiting);
                }

            }
            else {
                    errorText.GetComponent<FadeOut>().ResetFade(true);
            }
        });
    }

    public void selectNobleAction(){
        Dictionary<string, object> requestDict = new Dictionary<string, object>();
        JSONObject selectNobleJson = new JSONObject(requestDict);
        selectNobleJson.Add("playerId", player.GetUsername());
        selectNobleJson.Add("nobleId", selectedNoble.GetNoble().id);
        actionManager.MakeApiRequest(currSession.id, selectNobleJson, ActionManager.ActionType.claimNoble,ActionManager.RequestType.POST, (response) => {

            if(response != null){
                string status = (string)response["status"];

                if (status.Equals("failure")) {
                    errorText.GetComponent<FadeOut>().ResetFade();
                    return;
                };
                endTurnAction();

            }
            else {
                errorText.GetComponent<FadeOut>().ResetFade(true);
            }


        });
        
    }

    public void takeTokensAction(){
        Dictionary<string, object> requestDict = new Dictionary<string, object>();
        JSONObject chosenTokensJson = new JSONObject(requestDict);
        chosenTokensJson.Add("playerId", player.GetUsername());
        //Text[] tokenColours = selectedTokens.colours.toArray();
        string[] tokenColours = selectedTokens.colours.Select(t => t.text).ToArray();
        string[] tokenNums = selectedTokens.nums.Select(t => t.text).ToArray();

        List<string> tokenList = new List<string>();

        for (int i=0; i<3; i++) {
            if (tokenColours[i].Equals("none") || tokenColours[i].Equals("New")) continue;
            for (int mult=0; mult<Int16.Parse(tokenNums[i]); mult++) {
                tokenList.Add(tokenColours[i]);
            }
        }

        chosenTokensJson.Add("tokens", tokenList.ToArray());
        actionManager.MakeApiRequest(currSession.id, chosenTokensJson, ActionManager.ActionType.takeTokens, ActionManager.RequestType.POST, (response) => {
            
            takeTokensButton.SetActive(false);
            selectedTokens.reset(tokenBank);
                
            if(response != null){

                string status = (string)response["status"];

                if (status.Equals("failure")) {
                    errorText.GetComponent<FadeOut>().ResetFade();
                    return;
                };

                long overFlowAmount = (long)response["tokenOverflow"];
                if(overFlowAmount == 0){
                    // Handle removal of selected tokens
                }else{
                    // Handle too many tokens
                }
                endTurnAction();

            }
            else {
                    errorText.GetComponent<FadeOut>().ResetFade(true);
            }



        });

    }

    public void reserveCardAction(){
        Dictionary<string, object> requestDict = new Dictionary<string, object>();

        JSONObject reserveCardJson = new JSONObject(requestDict);
        reserveCardJson.Add("playerId", player.GetUsername());
        reserveCardJson.Add("source", "board"); //TO DO: add deck source option i.e. reserve card at top of deck for some given deck
        reserveCardJson.Add("cardId", selectedCardToReserve.GetCard().GetId());
        actionManager.MakeApiRequest(currSession.id, reserveCardJson, ActionManager.ActionType.reserveCard, ActionManager.RequestType.POST,(response) => {
            if(response != null){
                string status = (string)response["status"];

                if (status.Equals("failure")) {
                    errorText.GetComponent<FadeOut>().ResetFade();
                    return;
                };
                endTurnAction();
            }
            else {
                    errorText.GetComponent<FadeOut>().ResetFade(true);
            }
        });
        
    }

    public void reserveNobleAction(long nobleId){
        Dictionary<string, object> requestDict = new Dictionary<string, object>();

        JSONObject reserveCardJson = new JSONObject(requestDict);
        reserveCardJson.Add("playerId", player.GetUsername());
        reserveCardJson.Add("nobleId", nobleId);
        actionManager.MakeApiRequest(currSession.id, reserveCardJson, ActionManager.ActionType.reserveNoble, ActionManager.RequestType.POST,(response) => {
            if(response != null){
                string status = (string)response["status"];

                if (status.Equals("failure")) {
                    errorText.GetComponent<FadeOut>().ResetFade();
                    return;
                };

                JSONArray jsonNoblesVisited = (JSONArray)response["noblesVisiting"];//int[] noblesVisiting = new int[]

                long[] noblesVisiting = new long[jsonNoblesVisited.Count];
                for (int i = 0; i < jsonNoblesVisited.Count; i++) {
                    noblesVisiting[i] = (int)jsonNoblesVisited[i];
                }

                if (noblesVisiting.Count() == 0) {
                    endTurnAction();
                }
                else {
                    claimNoblePanel.DisplayNobleClaim(allNobles, noblesVisiting);
                }
            }
            else {
                    errorText.GetComponent<FadeOut>().ResetFade(true);
            }
        });
        
    }
    

    public void dominoCardAction(long cardId){
        Dictionary<string, object> requestDict = new Dictionary<string, object>();
        JSONObject selectedCardJson = new JSONObject(requestDict);
        selectedCardJson.Add("playerId", player.GetUsername());
        selectedCardJson.Add("cardId", cardId);
        actionManager.MakeApiRequest(currSession.id, selectedCardJson, ActionManager.ActionType.domino,ActionManager.RequestType.POST, (response) => {

            if (response != null) {
                string status = (string)response["status"];
                if (status.Equals("failure")) {
                    errorText.GetComponent<FadeOut>().ResetFade();
                    return;
                };
                string action = (string)response["action"];
                JSONArray jsonNoblesVisited = (JSONArray)response["noblesVisiting"];//int[] noblesVisiting = new int[]

                long[] noblesVisiting = new long[jsonNoblesVisited.Count];
                for (int i = 0; i < jsonNoblesVisited.Count; i++) {
                    noblesVisiting[i] = (int)jsonNoblesVisited[i];
                }

                if (action.Equals("domino1") || action.Equals("domino2") || action.Equals("satchel") || action.Equals("reserve")) {
                    Debug.Log("GOT TO ORIENT");
                    JSONArray jsonChoices = (JSONArray)JSONHandler.DecodeJsonRequest((string)response["options"]);
                    if (action.Equals("reserve")) {
                        List<long> ids = new List<long>();
                        for (int i = 0; i < jsonChoices.Count; i++) {
                            ids.Add((long)jsonChoices[i]);
                        }
                        List<Noble> nobleChoices = getNobles(ids);
                        orientPanelManager.gameObject.SetActive(true);
                        orientPanelManager.Display(new List<Card>(), nobleChoices, ActionManager.ActionType.reserveNoble);
                    }
                    else {
                        List<Card> cardChoices = new List<Card>();
                        for (int i = 0; i < jsonChoices.Count; i++) {
                            cardChoices.Add(allCards.GetCardFromId((long)jsonChoices[i]));
                        }
                        orientPanelManager.gameObject.SetActive(true);
                        if (action.Equals("domino2")) {
                            orientPanelManager.Display(cardChoices, new List<Noble>(), ActionManager.ActionType.domino);
                        }
                        else if (action.Equals("domino1")) {
                            orientPanelManager.Display(cardChoices, new List<Noble>(), ActionManager.ActionType.dominoSatchel);
                        }
                        else if (action.Equals("satchel")) {
                            Debug.Log(cardChoices.Count);
                            orientPanelManager.Display(cardChoices, new List<Noble>(), ActionManager.ActionType.satchel);
                        }
                    }

                }
                else if (noblesVisiting.Count() == 0) {
                    endTurnAction();
                }
                else {
                    claimNoblePanel.DisplayNobleClaim(allNobles, noblesVisiting);
                }

            }
            else {
                errorText.GetComponent<FadeOut>().ResetFade(true);
            }
        });
    }

    public void dominoSatchelAction(long cardId){
        Dictionary<string, object> requestDict = new Dictionary<string, object>();
        JSONObject selectedCardJson = new JSONObject(requestDict);
        selectedCardJson.Add("playerId", player.GetUsername());
        selectedCardJson.Add("cardId", cardId);
        actionManager.MakeApiRequest(currSession.id, selectedCardJson, ActionManager.ActionType.dominoSatchel,ActionManager.RequestType.POST, (response) => {

            if(response != null){
                string status = (string)response["status"];
                if (status.Equals("failure")) {
                    errorText.GetComponent<FadeOut>().ResetFade();
                    return;
                };
                string action = (string)response["action"];
                JSONArray jsonOptions = (JSONArray)JSONHandler.DecodeJsonRequest((string)response["options"]);

                List<Card> cardChoices = new List<Card>();
                 for (int i = 0; i < jsonOptions.Count; i++) {
                    cardChoices.Add(allCards.GetCardFromId((long)jsonOptions[i]));
                }
                orientPanelManager.gameObject.SetActive(true);
                orientPanelManager.Display(cardChoices, new List<Noble>(), ActionManager.ActionType.domino);
                //int[] noblesVisiting = new int[]
                /*
                int[] noblesVisiting = new int[jsonNoblesVisited.Count];
                for (int i = 0; i < jsonNoblesVisited.Count; i++) {
                    noblesVisiting[i] = (int)jsonNoblesVisited[i];
                }

                if(action.Equals("domino1") || action.Equals("domino2") || action.Equals("satchel")|| action.Equals("reserve")){
                    Debug.Log("GOT TO ORIENT");
                    JSONArray jsonChoices = (JSONArray)JSONHandler.DecodeJsonRequest((string)response["choices"]);
                    if(action.Equals("reserve")){
                        List<Noble> nobleChoices = new List<Noble>();
                        for(int i = 0; i < jsonChoices.Count; i++){
                            nobleChoices.Add(allNobles.allNobles.Find(x => x.id.Equals((long)jsonChoices[i])));
                        }
                        orientPanelManager.gameObject.SetActive(true);
                        orientPanelManager.Display(new List<Card>(), nobleChoices, ActionManager.ActionType.reserveNoble);
                    }
                    else{
                        List<Card> cardChoices = new List<Card>();
                        for (int i = 0; i < jsonChoices.Count; i++) {
                            cardChoices.Add(allCards.GetCardFromId((long)jsonChoices[i]));
                        }
                        orientPanelManager.gameObject.SetActive(true);
                        if(action.Equals("domino2")){
                            orientPanelManager.Display(cardChoices, new List<Noble>(), ActionManager.ActionType.domino);
                        }
                        else if(action.Equals("domino1")){
                            orientPanelManager.Display(cardChoices, new List<Noble>(), ActionManager.ActionType.dominoSatchel);
                        }
                        else if(action.Equals("satchel")){
                            Debug.Log(cardChoices.Count);
                            orientPanelManager.Display(cardChoices, new List<Noble>(), ActionManager.ActionType.satchel);
                        }
                    }

                }
                else if (noblesVisiting.Count() == 0) {
                    endTurnAction();
                }*/

                
            }
            else {
                    errorText.GetComponent<FadeOut>().ResetFade(true);
            }
                //HANDLE EXTRA CASES
                

            
        });
    }

    public void satchelAction(long cardId){
        Dictionary<string, object> requestDict = new Dictionary<string, object>();
        JSONObject selectedCardJson = new JSONObject(requestDict);
        selectedCardJson.Add("playerId", player.GetUsername());
        selectedCardJson.Add("cardId", cardId);
        actionManager.MakeApiRequest(currSession.id, selectedCardJson, ActionManager.ActionType.satchel,ActionManager.RequestType.POST, (response) => {

            if (response != null) {
                string status = (string)response["status"];
                if (status.Equals("failure")) {
                    errorText.GetComponent<FadeOut>().ResetFade();
                    return;
                };
                string action = (string)response["action"];
                JSONArray jsonNoblesVisited = (JSONArray)JSONHandler.DecodeJsonRequest((string)response["noblesVisiting"]);
                //int[] noblesVisiting = new int[]

                long[] noblesVisiting = new long[jsonNoblesVisited.Count];
                for (int i = 0; i < jsonNoblesVisited.Count; i++) {
                    noblesVisiting[i] = (int)jsonNoblesVisited[i];
                }

                if (action.Equals("domino1") || action.Equals("domino2") || action.Equals("satchel") || action.Equals("reserve")) {
                    Debug.Log("GOT TO ORIENT");
                    JSONArray jsonChoices = (JSONArray)JSONHandler.DecodeJsonRequest((string)response["options"]);
                    if (action.Equals("reserve")) {
                        List<long> ids = new List<long>();
                        for (int i = 0; i < jsonChoices.Count; i++) {
                            ids.Add((long)jsonChoices[i]);
                        }
                        List<Noble> nobleChoices = getNobles(ids);
                        orientPanelManager.gameObject.SetActive(true);
                        orientPanelManager.Display(new List<Card>(), nobleChoices, ActionManager.ActionType.reserveNoble);
                    }
                    else {
                        List<Card> cardChoices = new List<Card>();
                        for (int i = 0; i < jsonChoices.Count; i++) {
                            cardChoices.Add(allCards.GetCardFromId((long)jsonChoices[i]));
                        }
                        orientPanelManager.gameObject.SetActive(true);
                        if (action.Equals("domino2")) {
                            orientPanelManager.Display(cardChoices, new List<Noble>(), ActionManager.ActionType.domino);
                        }
                        else if (action.Equals("domino1")) {
                            orientPanelManager.Display(cardChoices, new List<Noble>(), ActionManager.ActionType.dominoSatchel);
                        }
                        else if (action.Equals("satchel")) {
                            Debug.Log(cardChoices.Count);
                            orientPanelManager.Display(cardChoices, new List<Noble>(), ActionManager.ActionType.satchel);
                        }
                    }

                }
                else if (noblesVisiting.Count() == 0) {
                    endTurnAction();
                }
                else {
                    claimNoblePanel.DisplayNobleClaim(allNobles, noblesVisiting);
                }
            }
            else {
                errorText.GetComponent<FadeOut>().ResetFade(true);
            }
                //HANDLE EXTRA CASES
                
        });
    }


    // Sets all users tokens to 99 
    public void debugAction(){
        Dictionary<string, object> requestDict = new Dictionary<string, object>();
        JSONObject playerJson = new JSONObject(requestDict);
        playerJson.Add("playerId", player.GetUsername());
        actionManager.MakeApiRequest(currSession.id, playerJson, ActionManager.ActionType.freeTokens, ActionManager.RequestType.POST, (response) => {

            string status = (string)response["status"];

            if(status == "success"){
                return;
            }else{
                return;
            }

        });
    }


    public void setReserveToTrue(){
        selectReserve = true;
        selectedCardToReserve = selectedCard;
        selectedCardToBuy = null;
        dashboard.DisplayReserve();
        allCards.UnGreyOut();
        Debug.Log("select reserve");
        reserveCardAction();
    }
    public void setReserveToFalse(){
        selectReserve = false;
        selectedCardToBuy = selectedCard;
        selectedCardToReserve = null;
        dashboard.DisplayPurchase();
        allCards.UnGreyOut();
        Debug.Log("select purchase");
        purchaseCardAction();
        
    }

    public void selectNobleToClaim() {
        if (selectedNoble != null) {
            //Add noble to inventory
            claimNoblePanel.TurnOffDisplay();
            selectNobleAction();
        }
    }

    public void EndTurn() // Player clicks "end turn"
    {
        //Check if player has impressed any noble
        //If true, pop up claim noble panel
        //Uncomment the following line to test functionality:
        //claimNoblePanel.checkAvailNobles(allNobles);
    }

    public void StartTurn() // Start of player's turn
    {
        dashboard.ResetEndDisplay();
        waiting = false;
        selectedCard = null;
        selectedCardToBuy = null;
        selectedCardToReserve = null;
        selectedNoble = null;
        purchaseOrReserve.SetActive(false);
    }



    private List<Noble> getNobles(List<long> ids){
        List<Noble> availNobles = new List<Noble>();
        NobleSlot[] nobleSlots = allNobles.GetAllNobels();
        for (int i = 0; i < nobleSlots.Length; i++) {
            if (nobleSlots[i] != null)
            {
                Noble noble = nobleSlots[i].GetNoble();
                for (int j = 0; j < ids.Count; j++)
                {
                    if (noble.id == ids[j])
                    {
                        availNobles.Add(noble);
                    }
                }
            }
        }
        return availNobles;
    }
}
