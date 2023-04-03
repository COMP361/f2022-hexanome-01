package ca.mcgill.splendorserver;

import ca.mcgill.splendorserver.controllers.SaveManager;
import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.saves.LobbyServiceSaveData;
import ca.mcgill.splendorserver.models.saves.SaveRegistrator;
import ca.mcgill.splendorserver.models.saves.SaveSession;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * This class is used to register the splendor gameservice to the lobby service.
 */
@Component
public class Registrator {
  private final Logger logger;
  private final String gameServiceName;
  private final String displayName;
  private final String gameServiceLocation;
  private final GameServiceRegistrationParameters gameServiceRegistrationParameters;
  @Value("${lobbyservice.location}")
  private String lobbyLocation;
  private final String[] expansionsServiceName =
        {"cities", "tradingposts"};

  @Autowired
  private SaveManager saveManager;
  @Autowired
  private Authentication auth;
  @Autowired
  private SaveRegistrator saveRegistrator;

  /**
   * This is the constructor that will get its value provided by spring from the
   * properties file.
   *
   * @param gameServiceName     the name of the game service that we want to register
   * @param displayName         the display name of the game
   * @param gameServiceLocation the url where we want the game to be at
   */
  @Autowired
  Registrator(@Value("${gameservice.name}") String gameServiceName,
              @Value("${gameservice.displayname}") String displayName,
              @Value("${gameservice.location}") String gameServiceLocation) {
    this.gameServiceName = gameServiceName;
    this.displayName = displayName;
    this.gameServiceLocation = gameServiceLocation;
    logger = LoggerFactory.getLogger(Registrator.class);
    gameServiceRegistrationParameters =
        new GameServiceRegistrationParameters(gameServiceName, displayName, gameServiceLocation);
  }

  /**
   * This class will be called as soon as we start up the server. It will register
   * the game service into the lobby service database.
   *
   * @throws InterruptedException throw error if can't access lobby service
   *                              or cannot register game service
   */
  @PostConstruct
  private void init() throws InterruptedException {
    try {
      String accessToken = auth.getAccessToken();
      //Register default (main) splendor service
      registerGameService(accessToken, gameServiceName);
      for (String expansionName : expansionsServiceName) {
        registerGameService(accessToken, expansionName);
      }
    } catch (UnirestException unirestException) {
      String errorMessage = "Failed to connect to Lobby Service";
      logger.error(errorMessage);
      throw new RuntimeException(errorMessage);
    }
  }

  /**
   * Registers the game service to the LS db by getting access token then sending request.
   */
  private void registerGameService(String accessToken, String serviceName) {
    try {
      Map<String, Object> bodyParams = new HashMap<>();
      bodyParams.put("name", serviceName);
      bodyParams.put("displayName", serviceName);
      bodyParams.put("location", gameServiceLocation + "splendor");
      bodyParams.put("maxSessionPlayers", 4);
      bodyParams.put("minSessionPlayers", 2);
      bodyParams.put("webSupport", "false");

      String lobbyGameServiceUrl = lobbyLocation + "/api/gameservices/" + serviceName;


      String body = new Gson().toJson(bodyParams);
      HttpResponse<String> response = Unirest
          .put(lobbyGameServiceUrl)
          .header("Authorization", "Bearer " + accessToken)
          .header("Content-Type", "application/json")
          .body(body)
          .asString();
      logger.info(response.getBody());

      if (response.getStatus() != 200) {
        logger.error("Register game Failed. Response: " + response.getBody());
        throw new RuntimeException("Register game Failed. Response: " + response.getBody());
      }
      logger.info(serviceName + " Game registration Success");

      // Register all saved games
      registerAllSavedGames(serviceName);
    } catch (UnirestException unirestException) {
      String errorMessage = "Failed to connect to Lobby Service";

      logger.error(errorMessage);
      throw new RuntimeException(errorMessage);
    }
  }

  /**
   * Unregisters the game service from the lobby service before the application shutdowns.
   * We don't have to restart the containers everytime now!
   */
  @PreDestroy
  public void unregisterGameService() {
    try {
      String accessToken = auth.getAccessToken();
      String lobbyGameServiceUrl = lobbyLocation + "/api/gameservices/" + gameServiceName;
      logger.info("Unregistering game service at: " + lobbyGameServiceUrl);
      HttpResponse<String> response = Unirest
          .delete(lobbyGameServiceUrl)
          .header("Authorization", "Bearer " + accessToken)
          .asString();

      if (response.getStatus() != 200) {
        logger.error("Unregister game Failed. Response: " + response.getBody());
      }

      for (String expansionName : expansionsServiceName) {
        lobbyGameServiceUrl = lobbyLocation + "/api/gameservices/" + expansionName;
        logger.info("Unregistering game service at: " + lobbyGameServiceUrl);
        response = Unirest
            .delete(lobbyGameServiceUrl)
            .header("Authorization", "Bearer " + accessToken)
            .asString();

        if (response.getStatus() != 200) {
          logger.error("Unregister game Failed. Response: " + response.getBody());
        }
      }

    } catch (UnirestException e) {
      logger.error(e.getMessage());
    }
  }

  /**
   * Registers all saved games to the lobby service.
   */
  private void registerAllSavedGames(String variant) {
    List<SaveSession> savedGames = saveManager.getAllSavedGames();

    for (SaveSession saveSession : savedGames) {
      Game game = saveSession.getGame();

      if (game.getVariant().equals(variant)) {
        LobbyServiceSaveData saveData = new LobbyServiceSaveData(game, saveSession.getSavegameid());

        try {
          saveRegistrator.registerSavedGameWithLobbyService(variant, saveData);
        } catch (UnirestException e) {
          logger.error("Failed to register saved game with id: " + game.getId(), e);
        }
      }
    }
  }
}

