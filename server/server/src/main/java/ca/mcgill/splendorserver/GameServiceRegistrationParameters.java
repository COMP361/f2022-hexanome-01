package ca.mcgill.splendorserver;

/**
 * Class to setup parameters to send request to lobby service to register the splendor gameService.
 *
 */
public class GameServiceRegistrationParameters {
  private final String name;
  private final String displayName;
  private final String location;
  private final int maxSessionPlayers = 4;
  private final int minSessionPlayers = 2;
  private final boolean webSupport = false;

  public GameServiceRegistrationParameters(String name, String displayName, String location) {
    this.name = name;
    this.displayName = displayName;
    this.location = location;
  }
}
