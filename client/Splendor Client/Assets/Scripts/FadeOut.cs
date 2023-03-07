using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class FadeOut : MonoBehaviour
{

    private Text text;

    // Start is called before the first frame update
    void Start()
    {
        text = gameObject.GetComponent<Text>();
        CompleteFade();
    }

    public void ResetFade()
    {
        text.text = "Nuh uh uh. You cannot make that move right now.";
        text.color = new Color(text.color.r, text.color.g, text.color.b, 1);
    }

    public void ResetFade(bool n)
    {
        if (n) text.text = "Nuh uh uh. Please wait your turn.";
        text.color = new Color(text.color.r, text.color.g, text.color.b, 1);
    }

    public void CompleteFade()
    {
        text.color = new Color(text.color.r, text.color.g, text.color.b, 0);
    }

    // Update is called once per frame
    void Update()
    {
        if (text.color.a > 0.0f)
            text.color = new Color(text.color.r, text.color.g, text.color.b, text.color.a - (Time.deltaTime / 5f));
    }
}
