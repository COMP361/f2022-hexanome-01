package ca.mcgill.splendorserver.apis;

import ca.mcgill.splendorserver.models.SessionData;
import java.util.Date;
import java.util.Timer;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

  long startTime = 0L;
  long elapsedTime = 0L;


  @GetMapping("/ping")
  public String ping(){
    return "Ping";
  }

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
