package dogapi;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        String breed = "hound";
        BreedFetcher breedFetcher = new CachingBreedFetcher(new BreedFetcherForLocalTesting());
        int result = getNumberOfSubBreeds(breed, breedFetcher);
        System.out.println(breed + " has " + result + " sub breeds");

        breed = "cat";
        result = getNumberOfSubBreeds(breed, breedFetcher);
        System.out.println(breed + " has " + result + " sub breeds");
    }

    /**
     * Return the number of sub breeds that the given dog breed has according to the
     * provided fetcher.
     *
     * @param breed the name of the dog breed
     * @param breedFetcher the breedFetcher to use
     * @return the number of sub breeds. Zero should be returned if there are no sub breeds
     *         returned by the fetcher, and zero should also be returned if the breed
     *         does not exist (BreedNotFoundException thrown).
     */
    public static int getNumberOfSubBreeds(String breed, BreedFetcher breedFetcher) {
        try {
            List<String> subBreeds = breedFetcher.getSubBreeds(breed);
            if (subBreeds == null) {
                return 0;
            }
            return subBreeds.size();
        } catch (BreedFetcher.BreedNotFoundException e) {
            // Invalid breed — consistent with assignment: return 0
            return 0;
        }
    }
}
