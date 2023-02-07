using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public interface IAbility {
    public void Setup(Player player);
    public void Activate();
    public void Deactivate();
}
