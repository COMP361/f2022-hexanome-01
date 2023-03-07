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
    public int black;
    public int generic;
    public int points;
    public bool nobleRequired;

    public bool CheckCondition(Player player) {
        return red <= player.GetBonusBank().red &&
            blue <= player.GetBonusBank().blue &&
            green <= player.GetBonusBank().green &&
            white <= player.GetBonusBank().white &&
            black <= player.GetBonusBank().black &&
            points <= player.GetPoints() &&
            (!nobleRequired || player.GetAcquiredNobles().Count > 0) && 
            (!(generic == 0) || GenericCheck(generic, player));
    }

    bool GenericCheck(int  threshold, Player player) {
        if (red == 0 && player.GetBonusBank().red >= threshold)
            return true;
        else if (blue == 0 && player.GetBonusBank().blue >= threshold)
            return true;
        else if (white == 0 && player.GetBonusBank().white >= threshold)
            return true;
        else if (green == 0 && player.GetBonusBank().green >= threshold)
            return true;
        else if (black == 0 && player.GetBonusBank().black >= threshold)
            return true;
        return false;
    }
}
public interface IUnlockable {
    public void Observe(Player player);
    public void PerformAbility();
    public bool Active { get; set; }
}
