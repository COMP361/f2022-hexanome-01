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

    public void PostData() => StartCoroutine(PostSession());
    public void GetData() => StartCoroutine(GetSession("Game1"));

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

    IEnumerator PostSession(){
       string url = "http://localhost:4244/splendor/Session";

       var request = new UnityWebRequest(url, RequestType.POST.ToString());

       List<LobbyPlayer> playerList = new List<LobbyPlayer>();
       LobbyPlayer player1 = new LobbyPlayer("player1", "Test");
       LobbyPlayer player2 = new LobbyPlayer("player2", "Test");
       LobbyPlayer player3 = new LobbyPlayer("player3", "Test");
       playerList.Add(player1);
       playerList.Add(player2);
       playerList.Add(player3);

       Session session = new Session("Game1", 3, playerList);
       

       var body = FileManager.EncodeSession(session, false);

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
