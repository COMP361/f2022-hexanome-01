using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class SaveSlot : MonoBehaviour {
    public Text nameBox;
    [SerializeField] private MainMenuManager thisManager;
    [SerializeField] private Save thisSave;
    //for now clicking load on a save will just put user into a session, as if they did create session
    public void LoadSave() {
        //empty for now, may not even be necessary
    }

    public void Setup(MainMenuManager newManager, Save newSave) {
        thisSave = newSave;
        thisManager = newManager;
        SetText();
    }

    public void SetText() {
        nameBox.text = "splendor with " + thisSave.PlayersToString();
    }

    public void OnClick() { //passes selected save to manager script
        thisManager.SetSave(thisSave);
    }
}
