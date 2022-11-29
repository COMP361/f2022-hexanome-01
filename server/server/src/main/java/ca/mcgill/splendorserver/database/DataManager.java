package ca.mcgill.splendorserver.database;

import ca.mcgill.splendorserver.models.SessionData;
import java.util.HashMap;

/**
 * Manages session data.
 */
public class DataManager {
  HashMap<String, SessionData> sessions = new HashMap<String, SessionData>();

  public void addSession(SessionData session) {
    sessions.put(session.getSessionName(), session);
  }

  public SessionData getSession(String sessionName) {
    return sessions.get(sessionName);
  }

}
