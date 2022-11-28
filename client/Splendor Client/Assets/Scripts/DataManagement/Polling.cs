using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Networking;

public class Polling : MonoBehaviour
{
    private void Start() {
        StartCoroutine(MakeRequests());
    }
    private IEnumerator MakeRequests(){
       string url = "http://localhost:4244/splendor/pingPolling/";
       bool ready = false;
       while(!ready)
        using(UnityWebRequest request = UnityWebRequest.Get(url)){
            yield return request.SendWebRequest();
            if(request.result == UnityWebRequest.Result.ProtocolError || request.result == UnityWebRequest.Result.ConnectionError){
                Debug.Log(request.error);
            }else {
                Debug.Log(request.downloadHandler.text);
                string readyText = request.downloadHandler.text;
                ready = bool.Parse(readyText);
                Debug.Log(ready);
            }
            
        }
    }

}
