using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEditor;
using UnityEngine.UI;

[CreateAssetMenu]
public class PopUpManager : ScriptableSingleton<PopUpManager>
{
    [SerializeField]
    GameObject popupPrefab;
    GameObject popupInstance;
    
    public void DisplayPopup(string message){
        GameObject canvas = GameObject.Find("Canvas");
        GameObject popupInstance = Instantiate(popupPrefab, canvas.transform);
        popupInstance.transform.GetChild(0).GetChild(0).GetComponent<Text>().text = message;
    }

}
