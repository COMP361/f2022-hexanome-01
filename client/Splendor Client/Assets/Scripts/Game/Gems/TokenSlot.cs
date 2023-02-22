using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class TokenSlot : MonoBehaviour
{
    public Gem token;
    public Text amount;
    public Image image;

    // Start is called before the first frame update
    void Start()
    {
        //initialises the displayed amount text to amount given to scriptableobject
        amount.text = token.amount.ToString();
    }

    // Update is called once per frame
    void Update()
    {
        amount.text = token.amount.ToString();
    }

}
