package dogapi;

import java.util.List;

/**
 * Interface for the service of getting sub breeds of a given dog breed.
 */
public interface BreedFetcher {

    /**
     * Fetch the list of sub breeds for the given breed.
     *
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist
     */
    List<String> getSubBreeds(String breed) throws BreedNotFoundException;

    /**
     * Exception thrown when a requested breed does not exist.
     * This is now a checked exception (extends Exception rather than RuntimeException),
     * so any method that can throw it must declare it in its method signature.
     */
    class BreedNotFoundException extends Exception {
        public BreedNotFoundException(String breed) {
            super("Breed not found: " + breed);
        }
    }
}
