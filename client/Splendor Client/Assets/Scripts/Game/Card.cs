using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

[CreateAssetMenu]
public class Card : ScriptableObject
{
    [SerializeField] public int id;
    [SerializeField] private int points;
    private bool active = true;

    public Sprite sprite;

    private SpriteRenderer m_SpriteRenderer;
    
    public int GetPoints()
    {
        if (!active) return 0;
        return points;
    }

}
