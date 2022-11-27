using System.Collections;
using System.Collections.Generic;
using UnityEditor.Compilation;
using UnityEngine;

[System.Serializable]
public class NobleData {
    
     public int id;
     public int points;
     public int red;
     public int blue;
     public int green;
     public int brown;
     public int white;

    public NobleData(Noble noble) {
        id = noble.id;
        points = noble.GetPoints();
        red = noble.nobleValue.red;
        blue = noble.nobleValue.blue;
        green = noble.nobleValue.green;
        brown = noble.nobleValue.brown;
        white = noble.nobleValue.white;
    }
}
