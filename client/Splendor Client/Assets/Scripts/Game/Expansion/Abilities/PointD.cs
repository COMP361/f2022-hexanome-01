using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Events;

public class PointD : MonoBehaviour, IAbility {
    private Player player;
    [SerializeField] private UnityEvent use, unuse;

    public void ChangePoints(int change) {
        player.AddPoints(change);
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
