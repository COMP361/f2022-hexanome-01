using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using System;
using UnityEngine.Events;
using System.Runtime.InteropServices.WindowsRuntime;

public class CardGemValue {
    int _red;
    int _green;
    int _blue;
    int _brown;
    int _white;
    int _gold;

    public CardGemValue() {
        this._red = 0;
        this._green = 0;
        this._blue = 0;
        this._brown = 0;
        this._white = 0;
        this._gold = 0;
    }

    public int gold {
        get { return _gold; }
        set { _gold = value; }
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

    public static CardGemValue combine(CardGemValue a, CardGemValue b) {
        CardGemValue c = new CardGemValue();
        c.blue = a.blue + b.blue;
        c.green = a.green + b.green;
        c.brown = a.brown + b.brown;
        c.red = a.red + b.red;
        c.white = a.white + b.white;
        c.gold = a.gold + b.gold;
        return c;
    }

    public void RemoveGemsFromInventory(Card card) {
        if (card) {
            red -= card.gemValue.red;
            green -= card.gemValue.green;
            blue -= card.gemValue.blue;
            brown -= card.gemValue.brown;
            white -= card.gemValue.white;
            gold -= card.gemValue.gold;
        }
    }

    public void ChangeGemAmount(char gem, int amount) {
        switch (gem) {
            case 'W': white += amount; break;
            case 'R': red += amount; break;
            case 'B': blue += amount; break;
            case 'G': green += amount; break;
            case 'K': brown += amount; break;
        }
    }

    public void AddGemsToInventory(Card card) {
        if (card) {
            red += card.gemValue.red;
            green += card.gemValue.green;
            blue += card.gemValue.blue;
            brown += card.gemValue.brown;
            white += card.gemValue.white;
            gold += card.gemValue.gold;
        }
    }

    public bool CheckSufficientPay(Card card) { //need to check for gold tokens + gem discounts       
        if (card == null) return false;
        return !(blue < card.blue || green < card.green ||
                brown < card.brown || red < card.red ||
                white < card.white);
    }

    public void PayFor(Card card) { //need to check for gold tokens + gem discounts
        this.red -= card.red;
        this.green -= card.green;
        this.blue -= card.blue;
        this.brown -= card.brown;
        this.white -= card.white;
    }

}

[CreateAssetMenu]
public class Card : ScriptableObject {
    [SerializeField] public long id;
    [SerializeField] private int points;
    [SerializeField] private char bonus;
    [SerializeField] private int bonusAmount = 1;
    [SerializeField] private long satchels = 0;

    [SerializeField] public int red;
    [SerializeField] public int blue;
    [SerializeField] public int green;
    [SerializeField] public int brown;
    [SerializeField] public int white;

    [SerializeField] public CardGemValue gemValue = new CardGemValue();

    private bool active = true;

    public Sprite sprite;

    private SpriteRenderer m_SpriteRenderer;

    public long GetSatchels() {
        return satchels;
    }

    public void SetSatchels(long amount) {
        satchels = amount;
    }

    public long GetId() {
        return id;
    }

    public int GetPoints() {
        if (!active) return 0;
        return points;
    }

    public char GetBonus() {
        return bonus;
    }

    public int getBonusAmount() {
        return bonusAmount;
    }

    void OnEnable() {

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
            else if (bonus == 'J')
                gemValue.gold = bonusAmount;
        }
        catch (NullReferenceException ex) {
            Debug.Log(ex);
        }
    }

    public bool Equals(Card card) {
        if (card.GetId() == id) return true;
        return false;
    }
}
