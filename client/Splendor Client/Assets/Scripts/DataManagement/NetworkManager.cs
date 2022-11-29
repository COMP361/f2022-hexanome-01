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
    public void GetData() => StartCoroutine(GetSession("Game1"));
    /////////////////////////////////////////////////////

    public void postSession(string sessionName, int maxPlayer, LobbyPlayer host) {
        StartCoroutine(PostSession(sessionName, maxPlayer, host));
    }

    public void getSessions(SessionData[] sessions) => StartCoroutine(GetSessions(sessions));

    public void UpdateGame(GameData newGameData) => StartCoroutine(PostGame(newGameData));

    // Update is called once per frame
    void Update()
    {
        
    }

    IEnumerator GetSession(string sessionName){
        string url = "http://localhost:4244/splendor/SessionName/" + sessionName;
        using(UnityWebRequest request = UnityWebRequest.Get(url)){
            yield return request.SendWebRequest();
            if(request.result == UnityWebRequest.Result.ProtocolError || request.result == UnityWebRequest.Result.ConnectionError){
                Debug.Log(request.error);
            }else {
                //Session session = FileManager.DecodeSession(request.downloadHandler.text, false);
                Debug.Log(request.downloadHandler.text);
                string session = request.downloadHandler.text;
                Session sessionreceived = new Session();
                
                sessionreceived = FileManager.DecodeSession(session, false);
                Debug.Log(sessionreceived.getSessionName());
            }
            
        }
    }

    IEnumerator GetSessions(SessionData[] sessions) {
        string url = "http://localhost:4244/splendor/SessionName";

        using(UnityWebRequest request = UnityWebRequest.Get(url)){
            yield return request.SendWebRequest();
            if(request.result == UnityWebRequest.Result.ProtocolError || request.result == UnityWebRequest.Result.ConnectionError) {
                Debug.Log(request.error);
            }
            else {
                string sessionString = request.downloadHandler.text;
                SessionListData sessionListData = FileManager.DecodeSessionListData(sessionString, false);

                sessions = sessionListData.sessionList;

            }
        }
    }

    IEnumerator PostSession(string sessionName, int maxPlayers, LobbyPlayer host){
       string url = "http://localhost:4244/splendor/Session";

       var request = new UnityWebRequest(url, RequestType.POST.ToString());

       Session session = new Session(sessionName, maxPlayers, new List<LobbyPlayer>());
       
       var body = FileManager.EncodeSession(session, false);

       request.uploadHandler = new UploadHandlerRaw(body);
       request.downloadHandler = new DownloadHandlerBuffer();
       request.SetRequestHeader("Content-Type", "application/json");
       yield return request.SendWebRequest();
       Debug.Log(request.downloadHandler.text);

    }

    IEnumerator GetGame(string gameId) {
        string url = "http://localhost:4244/splendor/games/" + gameId;
        using(UnityWebRequest request = UnityWebRequest.Get(url)){
            yield return request.SendWebRequest();
            if(request.result == UnityWebRequest.Result.ProtocolError || request.result == UnityWebRequest.Result.ConnectionError)
                Debug.Log(request.error);
            else
                Debug.Log(request.downloadHandler.text);

            
        }
    }

    
    IEnumerator PostGame(GameData newGameData) {
        string url = "http://localhost:4244/splendor/Game";

        var request = new UnityWebRequest(url, RequestType.POST.ToString());
        

        Debug.Log(newGameData.gameId);
        var body = FileManager.EncodeGameState(newGameData, true);
        

       request.uploadHandler = new UploadHandlerRaw(body);
       request.downloadHandler = new DownloadHandlerBuffer();
       request.SetRequestHeader("Content-Type", "application/json");
       yield return request.SendWebRequest();
       Debug.Log(request.downloadHandler.text);
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
