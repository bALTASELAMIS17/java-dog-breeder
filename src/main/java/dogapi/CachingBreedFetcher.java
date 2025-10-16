package dogapi;

import java.util.*;

/**
 * A caching decorator around a BreedFetcher that stores successful results
 * to avoid redundant API calls.
 */
public class CachingBreedFetcher implements BreedFetcher {

    private final BreedFetcher underlyingFetcher;
    private final Map<String, List<String>> cache = new HashMap<>();
    private int callsMade = 0;

    /**
     * Create a caching wrapper around another BreedFetcher.
     *
     * @param fetcher the underlying BreedFetcher used for uncached calls
     */
    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.underlyingFetcher = fetcher;
    }

    /**
     * Return the list of sub-breeds for the given breed.
     * Cached results are reused to avoid duplicate calls.
     *
     * @param breed the breed name
     * @return list of sub-breeds
     * @throws BreedNotFoundException if the underlying fetcher throws it
     */
    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        // Normalize key for consistent lookup
        final String key = breed.toLowerCase(Locale.ROOT);

        // If already cached, return a defensive copy
        if (cache.containsKey(key)) {
            return new ArrayList<>(cache.get(key));
        }

        // Otherwise, fetch from underlying fetcher
        try {
            List<String> subBreeds = underlyingFetcher.getSubBreeds(breed);
            callsMade++;

            // Cache the result (copy for immutability safety)
            cache.put(key, new ArrayList<>(subBreeds));
            return new ArrayList<>(subBreeds);
        } catch (BreedNotFoundException e) {
            // Still count this as a call, but do not cache it
            callsMade++;
            throw e;
        }
    }

    /**
     * Return the number of calls made to the underlying fetcher.
     */
    public int getCallsMade() {
        return callsMade;
    }
}
