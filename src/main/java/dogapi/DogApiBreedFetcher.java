package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) {
        // Defensive checks + normalization as the API expects lower-case breed names.
        if (breed == null) {
            throw new BreedNotFoundException("Breed cannot be null.");
        }
        final String normalized = breed.trim().toLowerCase(Locale.ROOT);
        if (normalized.isEmpty()) {
            throw new BreedNotFoundException("Breed cannot be empty.");
        }

        final String url = "https://dog.ceo/api/breed/" + normalized + "/list";
        final Request request = new Request.Builder().url(url).get().build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() == null) {
                throw new BreedNotFoundException("Empty response from dog.ceo API.");
            }

            final String body = response.body().string();
            final JSONObject json = new JSONObject(body);
            final String status = json.optString("status", "");

            // The API indicates errors via either HTTP status or {"status":"error", ...}.
            if (!response.isSuccessful() || !"success".equalsIgnoreCase(status)) {
                final String apiMessage = json.optString(
                        "message",
                        "Breed not found (main breed does not exist)"
                );
                throw new BreedNotFoundException(apiMessage);
            }

            final JSONArray arr = json.getJSONArray("message");
            final List<String> subs = new ArrayList<>(arr.length());
            for (int i = 0; i < arr.length(); i++) {
                subs.add(arr.getString(i));
            }
            return subs;
        } catch (IOException | org.json.JSONException e) {
            // Per assignment spec, surface all failures as BreedNotFoundException.
            throw new BreedNotFoundException(
                    "Failed to fetch sub-breeds for '" + breed + "': " + e.getMessage()
            );
        }
    }

}