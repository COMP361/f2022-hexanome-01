using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[CreateAssetMenu (fileName = "New City", menuName = "Extensions/City")]
public class City : ScriptableObject, IUnlockable {
    [SerializeField] private Sprite sprite;
    [SerializeField] private EffectType effectType;
    [SerializeField] private bool acquired;
    [SerializeField] private Condition condition;
    public int id;
    public bool Active { get { return acquired; } set { acquired = value; } }
    public Sprite Sprite { get { return sprite; } }
    public Condition Condition { get { return condition; }  }

    public EffectType EffectType { get { return effectType; } }

    public JSONObject Convert() {
        Dictionary<string, string> pairs = new Dictionary<string, string>();
        pairs.Add("id", id.ToString());
        pairs.Add("condition", condition.Convert().ToString());
        pairs.Add("effectType", effectType.ToString());
        pairs.Add("acquired", ""); //player who has acquired this city
        return new JSONObject(pairs);
    }
    public void PerformAbility(Player player) {
        //start endgame
    }

    public void Observe(Player player) {
        if (condition.CheckCondition(player)) {
            //add to player hand, or give player choice if multiple cities met
        }
    }
}
