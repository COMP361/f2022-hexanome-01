package ca.mcgill.splendorserver.models.registries;

import java.util.Set;

/**
 * Has elements with ids.
 *
 * @param <T> the type of elements
 */
public interface Registry<T> {

  public Set<Integer> listIds();

  public T of(int id);

}
