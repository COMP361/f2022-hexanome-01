using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[CreateAssetMenu(fileName = "New List", menuName = "Extensions/Unlockable List")]
public class UnlockableList : ScriptableObject {
    [SerializeField] private List<ScriptableObject> input;
    private List<IUnlockable> unlockables = new List<IUnlockable>();

    public List<IUnlockable> List { get { return unlockables; } }

    public void Reset() {
        foreach (IUnlockable un in unlockables)
            un.Active = false;
    }

    public void Init() {
        unlockables.Clear();
        foreach(ScriptableObject obj in input) 
            unlockables.Add(obj as IUnlockable);
        Reset();
    }

    public int Count { get { return unlockables.Count; } }

}
