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
    [SerializeField] public int id;
    [SerializeField] private int points;
    [SerializeField] private int red;
    [SerializeField] private int blue;
    [SerializeField] private int green;
    [SerializeField] private int brown;
    [SerializeField] private int white;


    [SerializeField] public NobleGemValue nobleValue = new NobleGemValue();

    //private bool active = true;

    public Sprite sprite;

    private SpriteRenderer m_SpriteRenderer;

    public JSONObject Convert() {
        Dictionary<string, string> pairs = new Dictionary<string, string>();
        pairs.Add("id", id.ToString());
        pairs.Add("points", points.ToString());
        pairs.Add("red", red.ToString());
        pairs.Add("blue", blue.ToString());
        pairs.Add("green", green.ToString());
        pairs.Add("brown", brown.ToString());
        pairs.Add("white", white.ToString());
        return new JSONObject(pairs);
    }
    public void SetData(NobleData data) {
        id = data.id;
        points = data.points;
        red = data.red;
        blue = data.blue;
        green = data.green;
        brown = data.brown;
        white = data.white;
    }
    public void SetValuesFromData(NobleData data) {
        id = data.id;
        points = data.points;
        nobleValue.red = data.red;
        nobleValue.green = data.green;
        nobleValue.blue = data.blue;
        nobleValue.brown = data.brown;
        nobleValue.white = data.white;
    }
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

