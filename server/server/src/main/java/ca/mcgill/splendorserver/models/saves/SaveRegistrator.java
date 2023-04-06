package ca.mcgill.splendorserver.models.saves;

import ca.mcgill.splendorserver.Authentication;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Registers saves with the LobbyService.
 */
@Component
public class SaveRegistrator {

  private final Logger logger = LoggerFactory.getLogger(SaveRegistrator.class);
  @Value("${lobbyservice.location}")
  private String lobbyLocation;
  @Autowired
  private Authentication auth;

  /**
   * Registers a saved game to the LS.
   *
   * @param gameservice the variant of the game
   * @param saveData the save
   * @throws UnirestException when things go wrong with the LS request
   */
  public void registerSavedGameWithLobbyService(String gameservice,
                                                       LobbyServiceSaveData saveData)
      throws UnirestException {
    String url = lobbyLocation + "/api/gameservices/" + gameservice + "/savegames/"
        + saveData.getSavegameid();

    HttpResponse<String> response = Unirest
        .put(url)
        .header("Authorization", "Bearer " + auth.getAccessToken())
        .header("Content-Type", "application/json")
        .body(new Gson().toJson(saveData))
        .asString();

    if (response.getStatus() != 200) {
      logger.error("Failed to register saved game: " + saveData.getSavegameid() + ". Response: "
          + response.getBody());
      throw new RuntimeException("Failed to register saved game: " + saveData.getSavegameid()
          + ". Response: " + response.getBody());
    }

    logger.info("Successfully registered saved game with id: " + saveData.getSavegameid());
  }
}
