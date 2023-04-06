using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PopupInstance: MonoBehaviour
{
    public void ClosePopUp(){
        Destroy(this.gameObject);
    }
}
