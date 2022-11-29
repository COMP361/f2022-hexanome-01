package ca.mcgill.splendorserver.apis;

import ca.mcgill.splendorserver.models.SessionData;
import java.util.HashMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for sessions.
 */
@RestController
public class SessionController {
  HashMap<String, SessionData> sessions = new HashMap<String, SessionData>();

  /**
   * Put for session.
   *
   * @param session SessionData
   * @return number of max players
   */
  @PostMapping("/Session")
  public int receiveSession(@RequestBody SessionData session) {

    sessions.put(session.getSessionName(), session);

    //Will need to handle code to "store session"
    return session.getMaxPlayers();
  }

  /**
   * Getter for session.
   *
   * @param name of session
   * @return SessionData for session as named above
   */
  @GetMapping(path = {"/SessionName", "/SessionName/{sessionName}"},
      produces = "application/json; charset=UTF-8")
  @ResponseBody
  public SessionData getSession(@PathVariable(required = false, name = "sessionName") String name) {

    if (name != null) {
      return sessions.get(name);
    } else {
      return null;
    }
  }

  /**
   * Getter for sessions.
   *
   * @return array of sessions as objects
   */
  @GetMapping(path = {"/SessionName"}, produces = "application/json; charset=UTF-8")
  @ResponseBody
  public Object[] getSessions() {
    return sessions.values().toArray();
  }
}
