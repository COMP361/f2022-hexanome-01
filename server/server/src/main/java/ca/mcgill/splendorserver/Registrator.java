package ca.mcgill.splendorserver;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.HashMap;
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
  @Value("${oauth2.name}")
  private String serviceOauthName;
  @Value("${oauth2.password}")
  private String serviceOauthPassword;
  private final String[] expansionsServiceName =
        {"splendorOrient", "splendorOrientCities", "splendorOrientTradePosts"};

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
      String accessToken = getAccessToken();
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
   * Signs in as an user with service role to the database to get the access token for request.
   *
   * @return the access token provided by lobby service (String)
   * @throws UnirestException throws exception if can't login or can't access lobby service
   */
  private String getAccessToken() throws UnirestException {
    try {
      String lobbyServiceTokenUrl = lobbyLocation + "/oauth/token";
      String bodyString =
          "grant_type=password&username=" + serviceOauthName + "&password=" + serviceOauthPassword;
      HttpResponse<String> response = Unirest
          .post(lobbyServiceTokenUrl)

          .header("Authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
          .header("Content-Type", "application/x-www-form-urlencoded")
          .body(bodyString)
          .asString();
      if (response.getStatus() != 200) {
        String errorMessage = "LS rejected login credentials";
        logger.error(errorMessage);
        throw new RuntimeException(errorMessage);
      }
      // Server succesful token response

      JsonObject responseJson = new JsonParser().parse(response.getBody()).getAsJsonObject();
      String token = responseJson.get("access_token").toString().replaceAll("\"", "");
      logger.info("token for registration: " + token);

      return token;
    } catch (UnirestException unirestException) {
      String errorMessage = "Failed to get access token from lobby service";
      logger.error(errorMessage);
      throw unirestException;
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
      bodyParams.put("location", gameServiceLocation + serviceName);
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
      String accessToken = getAccessToken();
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
}

