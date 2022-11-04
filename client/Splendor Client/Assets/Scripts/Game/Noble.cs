using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[CreateAssetMenu]
public class Noble : ScriptableObject
{


    [SerializeField] public int id;
    [SerializeField] private int points;
    [SerializeField] public int white;
    [SerializeField] public int brown;
    [SerializeField] public int blue;
    [SerializeField] public int green;
    [SerializeField] public int red;

    private bool active = true;

    public Sprite sprite;

    private SpriteRenderer m_SpriteRenderer;
    
    public int GetPoints()
    {
        return points;
    }




    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        
    }
}

