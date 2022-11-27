using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[System.Serializable]
public class CardData {
     public int id;
     public int points;
     public char bonus;
     public int bonusAmount;

     public int red;
     public int blue;
     public int green;
     public int brown;
     public int white;

    public CardData(Card card) {
        id = card.id;
        points = card.GetPoints();
        bonus = card.GetBonus();
        bonusAmount = card.getBonusAmount();
        red = card.red;
        blue = card.blue;
        green = card.green;
        brown = card.brown;
        white = card.white;
    }
}
