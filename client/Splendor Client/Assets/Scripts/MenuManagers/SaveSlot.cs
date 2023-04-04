using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class SaveSlot : MonoBehaviour {
    public Text nameBox;
    public GameObject startButton;
    [SerializeField] private MainMenuManager thisManager;
    [SerializeField] private Save thisSave;

    public void Setup(MainMenuManager newManager, Save newSave, GameObject startButton) {
        thisSave = newSave;
        thisManager = newManager;
        this.startButton = startButton;
        SetText();
    }

    public void SetText() {
        nameBox.text = thisSave.GetVariant() + " with " + thisSave.PlayersToString();
    }

    public void OnClick() { //passes selected save to manager script
        thisManager.SetSave(thisSave);
        startButton.SetActive(true);
    }
}
