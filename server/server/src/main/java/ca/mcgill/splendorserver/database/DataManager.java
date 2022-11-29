package ca.mcgill.splendorserver.database;

import ca.mcgill.splendorserver.models.SessionData;
import java.util.HashMap;

/**
 * Manages session data.
 */
public class DataManager {
  HashMap<String, SessionData> sessions = new HashMap<String, SessionData>();

  /**
   * Setter (put) session.
   *
   * @param session data for the session to add
   */
  public void addSession(SessionData session) {
    sessions.put(session.getSessionName(), session);
  }

  /**
   * Getter (get) session.
   *
   * @param sessionName the name of the session to get
   * @return session data for the session named as above
   */
  public SessionData getSession(String sessionName) {
    return sessions.get(sessionName);
  }

}
