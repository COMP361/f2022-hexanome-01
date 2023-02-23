package ca.mcgill.splendorserver.models.expansion;

/**
 * Interface for abilities from TradingPosts and cities from Cities extensions.
 */
public interface Unlockable {

  public void observe();

  public void use();

  public int getId();
}
