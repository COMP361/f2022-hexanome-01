using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.Networking;
using System.Text;

public enum RequestType {
    GET = 0,
    POST = 1,
    PUT = 2
}

public class Ping {
    public string ping;
}

public class NetworkManager : MonoBehaviour
{

//FileManager fileManager = new FileManager();

   // InputField outputArea;
   private string HOST = "10.122.184.196";

    // Start is called before the first frame update
    void Start()
    {
       // outputArea = GameObject.Find("OutputArea").GetComponent<InputField>();
        //GameObject.Find("GetButton").GetComponent<Button>().onClick.AddListener(GetData);
    }


    ////////////// For Testing on test scene ////////////
    public void PostData() { 
        GameData game = new GameData();
        string gameId = "Game1";
        //StartCoroutine(PostSession());
    }
    // public void GetData() => StartCoroutine(GetSession("Game1"));
    /////////////////////////////////////////////////////

    // public void postSession(string sessionName, int maxPlayer, LobbyPlayer host) {
    //     StartCoroutine(PostSession(sessionName, maxPlayer, host));
    // }

    // public void getSessions(SessionData[] sessions) => StartCoroutine(GetSessions(sessions));

    public void InitializePolling(string gameId, Authentication player, PlayerControl control) => StartCoroutine(StartPolling(gameId, player, control));
    public void joinPolling(string gameId, MainMenuManager control) => StartCoroutine(JoinPolling(gameId, control));

    public void endTurn(string gameId, TurnData turnData, Authentication mainPlayer, PlayerControl control) => StartCoroutine(EndTurnUpdate(gameId, turnData, mainPlayer, control));

    public void registerGame(GameConfigData config) => StartCoroutine(PostGame(config));

    //public void GameboardPolling() => StartCoroutine(StartPolling("test"));

    // Update is called once per frame
    void Update()
    {
        
    }

    //Its recieved as a long do we store it as a long or string
    // IEnumerator GetSessions(){
    //     string url = "http://localhost:4242/api/sessions/";
    //     using(UnityWebRequest request = UnityWebRequest.Get(url)){
    //         yield return request.SendWebRequest();
    //         if(request.result == UnityWebRequest.Result.ProtocolError || request.result == UnityWebRequest.Result.ConnectionError){
    //             Debug.Log(request.error);
    //         }else {
    //             Debug.Log(request.downloadHandler.text);
    //             string session = request.downloadHandler.text;
               
    //             Session[] sessionreceived = FileManager.DecodeSession(session, false);
    //             Debug.Log(sessionreceived.getSessionName());
    //         }
            
    //     }
    // }
    
    IEnumerator EndTurnUpdate(string gameId, TurnData turnData, Authentication mainPlayer, PlayerControl control) {

        string url = "http://" + HOST + ":4244/splendor/endturn/" + gameId;

        var request = new UnityWebRequest(url, RequestType.POST.ToString());
        
        var body = FileManager.EncodeTurnData(turnData, false);

        request.uploadHandler = new UploadHandlerRaw(body);
        request.downloadHandler = new DownloadHandlerBuffer();
        request.SetRequestHeader("Content-Type", "application/json");
        yield return request.SendWebRequest();
        Debug.Log(request.downloadHandler.text);

        StartCoroutine(StartPolling(gameId, mainPlayer, control));
    }

    IEnumerator PostGame(GameConfigData gameConfigData) {
        string url = "http://" + HOST + ":4244/splendor/register";

        var request = new UnityWebRequest(url, RequestType.POST.ToString());

        var body = FileManager.EncodeGameConfig(gameConfigData, false);
        

       request.uploadHandler = new UploadHandlerRaw(body);
       request.downloadHandler = new DownloadHandlerBuffer();
       request.SetRequestHeader("Content-Type", "application/json");
       yield return request.SendWebRequest();
       Debug.Log(request.downloadHandler.text);
    }

    

    private IEnumerator JoinPolling(string gameId, MainMenuManager control){
       string url = "http://" + HOST + ":4244/splendor/update/" + gameId;
       
       for(;;){
        using(UnityWebRequest request = UnityWebRequest.Get(url)){
            yield return request.SendWebRequest();
            if(request.result == UnityWebRequest.Result.ProtocolError || request.result == UnityWebRequest.Result.ConnectionError){
                Debug.Log(request.error);
            }else {
                Debug.Log(request.downloadHandler.text);

                string gameString = request.downloadHandler.text;

                GameData game = FileManager.DecodeGameState(gameString, false);

                if(game != null && game.gameId != null) {
                    control.StartJoinedGame();
                    break;
                }

            
            }
            yield return new WaitForSeconds(3);
            
            //StartCoroutine(PollTimer());
            
        }
       }
    }

    

    private IEnumerator StartPolling(string gameId, Authentication mainPlayer, PlayerControl control){
       string url = "http://" + HOST + ":4244/splendor/update/" + gameId;
       
       for(;;){
        using(UnityWebRequest request = UnityWebRequest.Get(url)){
            yield return request.SendWebRequest();
            if(request.result == UnityWebRequest.Result.ProtocolError || request.result == UnityWebRequest.Result.ConnectionError){
                Debug.Log(request.error);
            }else {
                Debug.Log(request.downloadHandler.text);

                string gameString = request.downloadHandler.text;

                GameData game = FileManager.DecodeGameState(gameString, false);

                if(game != null && game.currentPlayer != null && game.currentPlayer.id == mainPlayer.username) {
                    control.StartTurn();
                    break;
                }

            
            }
            yield return new WaitForSeconds(3);
            
            //StartCoroutine(PollTimer());
            
        }
       }
    }

    private IEnumerator PollTimer(){
        yield return new WaitForSeconds(3);
        Debug.Log("starting another request for game Update");
    }

    // private IEnumerator MakeRequests(){

    //     var getRequest = CreateRequest("http://locahost:4244/splendor/ping");
    //     yield return getRequest.SendWebRequest();
    //     var deserializedGetData = JsonUtility.FromJson<Ping>(getRequest.downloadHandler.text);
    //     //outputArea.text = deserializedGetData.ping;
    //     Debug.Log(deserializedGetData);
        
    // }

    // private UnityWebRequest CreateRequest(string path, RequestType reqType = RequestType.GET, object data = null) {
    //     var request = new UnityWebRequest(path, reqType.ToString());
    //     if (data != null) {
    //         var body = Encoding.UTF8.GetBytes(JsonUtility.ToJson(data));
    //         request.uploadHandler = new UploadHandlerRaw(body);
    //     }

    //     request.downloadHandler = new DownloadHandlerBuffer();
    //     request.SetRequestHeader("content-Type", "application/json");

    //     return request;
    // }



    
}
