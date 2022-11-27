package ca.mcgill.splendorserver.Database;

import ca.mcgill.splendorserver.models.SessionData;

import javax.websocket.Session;
import java.util.HashMap;

public class DataManager {

    HashMap<String, SessionData> sessions = new HashMap<String, SessionData>();

    public void addSession(SessionData session){
        sessions.put(session.getSessionName(), session);
    }
    public SessionData getSession(String sessionName){
        return sessions.get(sessionName);
    }

}
