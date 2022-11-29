package ca.mcgill.splendorserver.models;

/**
 * Stores and manages player data for authentication.
 */
public class LobbyPlayer {
  private String username;
  private String accessToken;
  private String refreshToken;
  private String expiresIn;

  /**
   * Getter for username.
   *
   * @return username string
   */
  public String getUsername() {
    return username;
  }

  /**
   * Setter for username.
   *
   * @param username username string
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Getter for access token.
   *
   * @return access token string
   */
  public String getAccessToken() {
    return accessToken;
  }

  /**
   * Setter access token.
   *
   * @param accessToken access token string
   */
  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  /**
   * Getter refresh token.
   *
   * @return refresh token string
   */
  public String getRefreshToken() {
    return refreshToken;
  }

  /**
   * Setter for refresh token.
   *
   * @param refreshToken refresh token string
   */
  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  /**
   * Getter for expires in.
   *
   * @return date and time of expiry as string
   */
  public String getExpiresIn() {
    return expiresIn;
  }

  /**
   * Setter for expires in.
   *
   * @param expiresIn date and time of expiry as string
   */
  public void setExpiresIn(String expiresIn) {
    this.expiresIn = expiresIn;
  }
}
