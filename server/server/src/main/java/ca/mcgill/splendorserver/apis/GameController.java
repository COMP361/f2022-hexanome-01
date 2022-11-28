package ca.mcgill.splendorserver.apis;

import ca.mcgill.splendorserver.models.GameData;
import ca.mcgill.splendorserver.models.PlayerData;
import org.springframework.expression.AccessException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class GameController {

    private HashMap<String, GameData> games = new HashMap<String, GameData>();

    @GetMapping(path = {"/games", "/games/{gameId}"})
    public String getGame(@PathVariable(required = false, name = "gameId") String gameId){
        if (gameId != null) {
            return "Splendor";
        }
        else {
            return "empty";
        }
    }

    @PostMapping("/games")
    public String createGame(@RequestBody String gameId) {
        games.put(gameId, new GameData());

        return "gameId";
    }


}
