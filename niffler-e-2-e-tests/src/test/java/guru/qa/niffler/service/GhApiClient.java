package guru.qa.niffler.service;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.GhApi;
import guru.qa.niffler.config.Config;
import org.junit.jupiter.api.Assertions;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class GhApiClient {

    private static final Config CFG = Config.getInstance();
    private static final String GH_TOKEN_ENV = "GITHUB_TOKEN";

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(CFG.githubUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final GhApi ghApi = retrofit.create(GhApi.class);

    public @Nonnull String issueState(String issueNumber) {
        try {
            Response<JsonNode> response = ghApi.issue("Bearer " + System.getenv(GH_TOKEN_ENV), issueNumber)
                    .execute();
            Assertions.assertEquals(200, response.code(), "Unexpected response code");

            return Objects.requireNonNull(response.body()).get("state").asText();
        } catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }
}
