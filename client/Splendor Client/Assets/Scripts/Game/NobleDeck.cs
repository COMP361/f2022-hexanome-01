using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class NobleDeck : MonoBehaviour
{
     public int level;
    [SerializeField]public List<Noble> nobles = new List<Noble>(); 
    //Takes the first card from the deck and remove it from list of cards 
    public Noble DrawNoble()
    {
        Noble nobleDrawn = nobles[0];
        nobles.Remove(nobleDrawn);
        return nobleDrawn;
    }
    //Shuffles the whole whole deck
    public void ShuffleDeck()
    {
        // for (int i = 0; i < nobles.Count; i++) {
        // Noble tempNoble = nobles[i];
        // int random = Random.Range(i, nobles.Count);
        // nobles[i] = nobles[random];
        // nobles[random] = tempNoble;
        // nobles.OrderBy()
     }

    }
}

