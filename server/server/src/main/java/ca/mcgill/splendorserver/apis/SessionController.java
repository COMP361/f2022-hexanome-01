package ca.mcgill.splendorserver.apis;

import ca.mcgill.splendorserver.models.SessionData;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap; // import the HashMap class
import java.util.Map;


@RestController
public class SessionController {
    HashMap<String, SessionData> sessions = new HashMap<String, SessionData>();

    @PostMapping("/postSession")
    int receiveSession(@RequestBody SessionData session){

        sessions.put(session.getSessionName(), session);

        //Will need to handel code to "store session"
        return session.getMaxPlayers();
    }

    @GetMapping(path = {"/getSession", "/getSession/{sessionName}"})
    public String getSession(@PathVariable(required=false,name="sessionName") String name) {


        if (name != null) {
            return sessions.get(name).getSessionName();
        } else{
           return "";
        }
    }

}
