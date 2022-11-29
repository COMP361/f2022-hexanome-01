package ca.mcgill.splendorserver.models;

public class LobbyPlayer {
  private String username;
  private String accessToken;
  private String refreshToken;
  private String expiresIn;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getaccessToken() {
    return accessToken;
  }

  public void setaccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getrefreshToken() {
    return refreshToken;
  }

  public void setrefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public String getexpiresIn() {
    return expiresIn;
  }

  public void setexpiresIn(String expiresIn) {
    this.expiresIn = expiresIn;
  }
}
