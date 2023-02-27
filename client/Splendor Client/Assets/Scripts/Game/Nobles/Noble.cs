using System.Collections;
using System.Collections.Generic;
using UnityEngine;


public class NobleGemValue {
    int _red;
    int _green;
    int _blue;
    int _brown;
    int _white;

    public NobleGemValue() {
        this._red = 0;
        this._green = 0;
        this._blue = 0;
        this._brown = 0;
        this._white = 0;
    }



    public int red {
        get { return _red; }
        set { _red = value; }
    }
    public int green {
        get { return _green; }
        set { _green = value; }
    }
    public int blue {
        get { return _blue; }
        set { _blue = value; }
    }
    public int brown {
        get { return _brown; }
        set { _brown = value; }
    }
    public int white {
        get { return _white; }
        set { _white = value; }
    }
}


[CreateAssetMenu]
public class Noble : ScriptableObject {
    [SerializeField] public long id;
    [SerializeField] private int points;
    [SerializeField] private int red;
    [SerializeField] private int blue;
    [SerializeField] private int green;
    [SerializeField] private int brown;
    [SerializeField] private int white;


    [SerializeField] public NobleGemValue nobleValue = new NobleGemValue();

    private bool active = true;

    public Sprite sprite;

    private SpriteRenderer m_SpriteRenderer;

    public int GetPoints() {
        return points;
    }

    void OnEnable() {
        nobleValue.red = this.red;
        nobleValue.blue = this.blue;
        nobleValue.green = this.green;
        nobleValue.brown = this.brown;
        nobleValue.white = this.white;
    }
}

