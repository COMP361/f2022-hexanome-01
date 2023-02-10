using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class SessionSlot : MonoBehaviour {
    public Text nameBox;
    public GameObject joinButton;
    [SerializeField] private MainMenuManager thisManager;
    [SerializeField] private Session thisSession;

    public void Setup(MainMenuManager newManager, Session newSession, GameObject joinButton) {
        thisSession = newSession;
        thisManager = newManager;
        this.joinButton = joinButton;
        SetText();
    }

    public void SetText() {
        nameBox.text = thisSession.creator + "'s " + thisSession.GetVariant() + " with " + thisSession.PlayersToString();
    }

    public void OnClick() { //passes selected session to manager script
        thisManager.SetSession(thisSession);
        joinButton.SetActive(true);
    }
}
