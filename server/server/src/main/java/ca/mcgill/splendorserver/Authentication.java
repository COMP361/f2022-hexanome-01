package ca.mcgill.splendorserver;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Provides an access token from the LobbyService.
 */
@Component
public class Authentication {

  private static final Logger logger = LoggerFactory.getLogger(Authentication.class);
  @Value("${lobbyservice.location}")
  private String lobbyLocation;
  @Value("${oauth2.name}")
  private String serviceOauthName;
  @Value("${oauth2.password}")
  private String serviceOauthPassword;

  /**
   * Signs in as an user with service role to the database to get the access token for request.
   *
   * @return the access token provided by lobby service (String)
   * @throws UnirestException throws exception if can't login or can't access lobby service
   */
  public String getAccessToken() throws UnirestException {
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
}
