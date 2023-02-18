package ca.mcgill.splendorserver.models.registries;

import java.util.Set;

public interface Registry<T> {
	
	public Set<Integer> listIds();
	public T of(int id);
	
}
