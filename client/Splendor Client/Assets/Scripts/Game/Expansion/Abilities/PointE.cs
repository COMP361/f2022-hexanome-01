using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Events;

public class PointE : MonoBehaviour, IAbility {
    private Player player;
    [SerializeField] private UnityEvent use, unuse;

    public void ChangePoints(int multiplier) {
        int activeAbilities = 0;
        foreach(IUnlockable u in player.unlockables.List) {
            if (u is TradingPost && u.Active)
                activeAbilities++;
        }
        player.AddPoints(multiplier * activeAbilities);
    }
    public void Setup(Player player) {
        this.player = player;
    }
    public void Activate() {
        use.Invoke();
    }

    public void Deactivate() {
        unuse.Invoke();
    }
}
