using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[System.Serializable]
public class SessionListData {
    public SessionData[] sessionList;

    public SessionListData() { }
    public SessionListData (List<Session> sessions) {
        sessionList = new SessionData[sessions.Count];
        for (int i = 0; i < sessions.Count; i++)
            sessionList[i] = new SessionData(sessions[i]);
    }
}
