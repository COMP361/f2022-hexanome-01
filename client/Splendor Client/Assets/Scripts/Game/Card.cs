using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using System;


public class CardGemValue{
      int _red;
      int _green;
      int _blue;
      int _brown;
      int _white;

      public CardGemValue(){
          this._red = 0;
          this._green = 0;
          this._blue = 0;
          this._brown = 0;
          this._white = 0;
      }

     public int red
    {
        get { return _red; }
        set { _red = value; }
    }
    public int green
    {
        get { return _green; }
        set { _green = value; }
    }
    public int blue
    {
        get { return _blue; }
        set { _blue = value; }
    }
    public int brown
    {
        get { return _brown; }
        set { _brown = value; }
    }
    public int white
    {
        get { return _white; }
        set { _white = value; }
    }

     public void AddGemsToInventory(Card card){
         Card tempCard = (Card) ScriptableObject.CreateInstance(typeof(Card));
         tempCard = card;

         if(tempCard != null){
            this.red += tempCard.gemValue.red;
            this.green += tempCard.gemValue.green;
            this.blue += tempCard.gemValue.blue;
            this.brown += tempCard.gemValue.brown;
            this.white += tempCard.gemValue.white;
         }
     }
     
}

[CreateAssetMenu]
public class Card : ScriptableObject
{


    [SerializeField] public int id;
    [SerializeField] private int points;
    [SerializeField] private char bonus;
    [SerializeField] private int bonusAmount = 1;
    
    [SerializeField] private int red;
    [SerializeField] private int blue;
    [SerializeField] private int green;
    [SerializeField] private int brown;
    [SerializeField] private int white;
    
    [SerializeField] public CardGemValue gemValue = new CardGemValue();
    private bool active = true;

    public Sprite sprite;

    private SpriteRenderer m_SpriteRenderer;
    
    public int GetPoints()
    {
        if (!active) return 0;
        return points;
    }

    void OnEnable()
    {

        try {
            if (this.bonus == 'W')
                gemValue.white = this.bonusAmount;
            else if (this.bonus == 'R')
                gemValue.red = this.bonusAmount;
            else if (this.bonus == 'B')
                gemValue.blue = this.bonusAmount;
            else if (this.bonus == 'G')
                gemValue.green = this.bonusAmount;
            else if (this.bonus == 'K')
                gemValue.brown = this.bonusAmount;
            
        }
        catch (NullReferenceException ex) {
            Debug.Log(ex);
        }
    }

}
