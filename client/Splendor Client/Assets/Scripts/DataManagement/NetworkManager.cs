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
   // InputField outputArea;

    // Start is called before the first frame update
    void Start()
    {
       // outputArea = GameObject.Find("OutputArea").GetComponent<InputField>();
        //GameObject.Find("GetButton").GetComponent<Button>().onClick.AddListener(GetData);
    }

    public void GetData() => StartCoroutine(GetData_Coroutine());

    // Update is called once per frame
    void Update()
    {
        
    }

    IEnumerator GetData_Coroutine(){
        string url = "http://localhost:4244/splendor/ping";
        using(UnityWebRequest request = UnityWebRequest.Get(url)){
            yield return request.SendWebRequest();
            if(request.isNetworkError || request.isHttpError)
                Debug.Log(request.error);
            else
                Debug.Log(request.downloadHandler.text);

            
        }
    }

    // private IEnumerator MakeRequests(){

    //     var getRequest = CreateRequest("https://locahost:4244/spendor/ping");
    //     yield return getRequest.SendWebRequest();
    //     var deserializedGetData = JsonUtility.FromJson<Ping>(getRequest.downloadHandler.text);
    //     //outputArea.text = deserializedGetData.ping;
    //     Debug.Log(deserializedGetData.ping);
        
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
