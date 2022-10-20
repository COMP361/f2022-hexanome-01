using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.InputSystem;

public class Parallax : MonoBehaviour
{
    public float depth;
    public RectTransform rt;
    
    void OnEnable()
    {
        rt = GetComponent<RectTransform>();
    }

    void Update()
    {
        Vector2 mousePos = Mouse.current.position.ReadValue();
        Vector2 middle = new Vector2(Screen.width/2, Screen.height/2);
        Vector2 distance = new Vector2((mousePos.x - middle.x) / depth, (mousePos.y - middle.y) / depth);
        rt.position = new Vector2(middle.x + distance.x, middle.y + distance.y);
    }
}
