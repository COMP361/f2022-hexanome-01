package ca.mcgill.splendorserver.apis;

import java.util.Date;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Tester class for networking.
 */
@RestController
public class PingController {

  public long startTime = 0L;
  public long elapsedTime = 0L;


  @GetMapping("/ping")
  public String ping() {
    return "Ping";
  }

  /**
   * Polling tester.
   *
   * @return fake ping
   */
  @GetMapping("/pingPolling")
  public boolean pingPolling() {
    if (startTime == 0L) {
      startTime = System.currentTimeMillis();
      return false;
    }
    elapsedTime = (new Date()).getTime() - startTime;
    if (elapsedTime > 2 * 60 * 1000) {
      return true;
    } else {
      return false;
    }
  }

}
