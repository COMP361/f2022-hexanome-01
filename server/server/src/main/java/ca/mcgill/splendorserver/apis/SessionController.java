package ca.mcgill.splendorserver.apis;

import ca.mcgill.splendorserver.models.SessionData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class SessionController {

    private static FileWriter file;


    @PostMapping("/postSession")
    int receiveSession(@RequestBody SessionData session){



        ObjectMapper mapper = new ObjectMapper();
        try {
            file = new FileWriter("../currentSession.json");
            file.write(mapper.writeValueAsString(session));
        }catch (IOException e){
            e.printStackTrace();
        }

        //Will need to handel code to "store session"
        return session.getMaxPlayers();
    }
}
