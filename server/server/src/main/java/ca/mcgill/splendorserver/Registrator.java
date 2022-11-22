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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * This class is used to register the splendor gameservice to the lobby service.
 *
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

  @PostConstruct
  private void init() throws InterruptedException {

    registerGameService();
  }

  private String getAccessToken() throws UnirestException {

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
  }

  private void registerGameService() {
    try {
      String accessToken = getAccessToken();
      Map<String, Object> bodyParams = new HashMap<>();
      bodyParams.put("name", gameServiceName);
      bodyParams.put("displayName", displayName);
      bodyParams.put("location", gameServiceLocation);
      bodyParams.put("maxSessionPlayers", 4);
      bodyParams.put("minSessionPlayers", 2);
      bodyParams.put("webSupport", "false");

      String lobbyGameServiceUrl = lobbyLocation + "/api/gameservices/" + gameServiceName;

      String body = new Gson().toJson(gameServiceRegistrationParameters);

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
      logger.info("Game registration Success");
    } catch (UnirestException unirestException) {
      String errorMessage = "Failed to connect to Lobby Service";

      logger.error(errorMessage);
      throw new RuntimeException(errorMessage);
    }
  }
}

