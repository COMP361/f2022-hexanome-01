using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public enum EffectType {
    City,
    Points,
    Bank,
    Purchase
}

[System.Serializable]
public struct Condition {
    public int red;
    public int blue;
    public int green;
    public int white;
    public int brown;
    public int generic;
    public int points;
    public bool nobleRequired;

    public bool CheckCondition(Player player) {
        return red <= player.bonusesAquired.red &&
            blue <= player.bonusesAquired.blue &&
            green <= player.bonusesAquired.green &&
            white <= player.bonusesAquired.white &&
            brown <= player.bonusesAquired.brown &&
            points <= player.GetPoints() &&
            (!nobleRequired || player.GetAcquiredNobles().Count > 0) && 
            (!(generic == 0) || GenericCheck(generic, player));
    }

    bool GenericCheck(int  threshold, Player player) {
        if (red == 0 && player.bonusesAquired.red >= threshold)
            return true;
        else if (blue == 0 && player.bonusesAquired.blue >= threshold)
            return true;
        else if (white == 0 && player.bonusesAquired.white >= threshold)
            return true;
        else if (green == 0 && player.bonusesAquired.green >= threshold)
            return true;
        else if (brown == 0 && player.bonusesAquired.brown >= threshold)
            return true;
        return false;
    }
}
public interface IUnlockable {
    public void Observe(Player player);
    public void PerformAbility();
    public bool Active { get; set; }
}
