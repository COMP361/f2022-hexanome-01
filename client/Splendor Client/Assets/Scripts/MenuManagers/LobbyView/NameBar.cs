using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using System;

public class NameBar : MonoBehaviour
{
    public Text sessionName;
    public InputField inputName;

    public void Setup(){
        sessionName.text = String.Format("{0}", inputName.text);
    }
}
