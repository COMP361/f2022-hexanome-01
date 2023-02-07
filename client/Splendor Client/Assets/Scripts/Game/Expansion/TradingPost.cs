using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Events;

[CreateAssetMenu(fileName = "New Trading Post", menuName = "Extensions/Trading Post")]
public class TradingPost : ScriptableObject, IUnlockable {
    [SerializeField] private Sprite sprite;
    [SerializeField] private EffectType effectType;
    [SerializeField] private bool active;
    [SerializeField] private GameObject ability;
    [SerializeField] private Condition condition;

    public bool Active { get { return active; } set { active = value; } }
    public Sprite Sprite { get { return sprite; } }
    public Condition Condition { get { return condition; } }

    public EffectType EffectType { get { return effectType; } }

    public void PerformAbility(Player player) {
        ability.GetComponent<IAbility>().Setup(player);
        ability.GetComponent<IAbility>().Activate();
    }

    public void Observe(Player player) {
        if(effectType == EffectType.Points) {
            bool temp = condition.CheckCondition(player);
            if (temp && !active) {
                active = true;
                ability.GetComponent<IAbility>().Setup(player);
                ability.GetComponent<IAbility>().Activate();
            }
            else if (!temp && active) {
                ability.GetComponent<IAbility>().Setup(player);
                ability.GetComponent<IAbility>().Deactivate();
                active = false;
            }
            else
                return;
        }
        else
            active = condition.CheckCondition(player);
    }
}
