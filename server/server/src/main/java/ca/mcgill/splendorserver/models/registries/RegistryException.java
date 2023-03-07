package ca.mcgill.splendorserver.models.registries;

/**
 * Exception for registries that can't be loaded.
 */
public class RegistryException extends RuntimeException {

  public RegistryException(String message, Throwable error) {
    super("Registry could not load data for " + message, error);
  }
}
