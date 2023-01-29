using System.Collections;
using System.Collections.Generic;
using UnityEngine;

// Gem is the same thing as token (the in game currency)
// The name "gem" is to differentiate the "token" from the one used for users

public class GemValue {
    int _red;
    int _green;
    int _blue;
    int _brown;
    int _white;

    public GemValue() {
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


public class Gem : MonoBehaviour
{
    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        
    }
}
