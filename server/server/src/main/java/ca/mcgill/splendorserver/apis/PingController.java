package ca.mcgill.splendorserver.apis;

import ca.mcgill.splendorserver.models.SessionData;
import java.util.Date;
import java.util.Timer;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;




@RestController
public class PingController {

    public long startTime = 0L;
    public long elapsedTime = 0L;


    @GetMapping("/ping")
    public String ping(){
        return "Ping";
    }

    @GetMapping("/pingPolling")
    public boolean pingPolling()
    {
        if(startTime == 0L)
        {
            startTime = System.currentTimeMillis();
            return false;
        }
        elapsedTime = (new Date()).getTime() - startTime;
        if(elapsedTime > 2*60*1000)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

//    @RequestMapping(value = "/pong", method = RequestMethod.POST, consumes = "application/json")
//    public String PostData(@RequestBody SessionData session){
//        return "session Name is :" + session.getSessionName();
//    }

//    @PostMapping(
//            value = "/pong",
//            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
//            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
//    public ResponseEntity<SessionData> postBody(@RequestBody SessionData session) {
//        SessionData persistedSession = session;
//        return ResponseEntity
//                .created(URI.create(String.format("/Session/%s", session.getSessionName())))
//                .body(persistedSession);
//    }

//    @PostMapping("/postSession")
//    String receiveSession(@RequestBody SessionData session){
//
//        //Will need to handel code to "store session"
//        return session.getSessionName();
//    }


}
