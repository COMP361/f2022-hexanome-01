package ca.mcgill.splendorserver.apis;

import ca.mcgill.splendorserver.models.GameData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class GameController {

    private HashMap<String, GameData> games = new HashMap<String, GameData>();

    @GetMapping(path = {"/GameId", "/GameId/{gameId}"}, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public GameData getGame(@PathVariable(required = false, name = "gameId") String gameId){
        if (gameId != null) {
            return games.get(gameId);
        }
        else {
            return null;
        }
    }

    @PostMapping("/Game")
    public String createGame(@RequestBody GameData game) throws JsonProcessingException {

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(game);
        games.put(game.getGameId(), game);
        System.out.println(json);


        return game.getGameId();
    }

    @PutMapping(path= {"/Game/GameId/{gameId}"}, consumes= "application/json; charset=UTF-8")
    public String lauchGame(@PathVariable String gameId, @RequestBody GameData game) throws JsonProcessingException {
        return createGame(game);
    }

}
