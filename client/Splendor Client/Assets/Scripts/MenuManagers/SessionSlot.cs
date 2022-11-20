using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class SessionSlot : MonoBehaviour {
    public Text nameBox;
    [SerializeField] private MainMenuManager thisManager;
    [SerializeField] private Session thisSession;
    //for now clicking join on a session will just put user into a test session
    public void JoinSession() {
        //empty for now, may not even be necessary
    }

    public void Setup(MainMenuManager newManager, Session newSession) {
        thisSession = newSession;
        thisManager = newManager;
        SetText();
    }

    public void SetText() {
        nameBox.text = thisSession.sessionName;
    }

    public void OnClick() { //passes selected session to manager script
        thisManager.SetSession(thisSession);
    }
}
