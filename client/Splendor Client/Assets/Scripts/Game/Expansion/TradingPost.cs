using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Events;

[CreateAssetMenu(fileName = "New Trading Post", menuName = "Extensions/Trading Post")]
public class TradingPost : ScriptableObject, IUnlockable
{
    [SerializeField] public long id;
    [SerializeField] private Sprite sprite;
    [SerializeField] private EffectType effectType;
    [SerializeField] private bool active;
    [SerializeField] private UnityEvent ability;
    [SerializeField] private Condition condition;

    public bool Active { get { return active; } set { active = value; } }
    public Sprite Sprite { get { return sprite; } }
    public Condition Condition { get { return condition; } }

    public EffectType EffectType { get { return effectType; } }

    public void PerformAbility() {
        //do action
    }

    public void Observe(Player player) {
        active = condition.CheckCondition(player);
    }
}
