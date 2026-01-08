package guru.qa.niffler.service;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.GhApi;
import org.junit.jupiter.api.Assertions;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.Objects;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;

@ParametersAreNonnullByDefault
public class GhApiClient extends RestClient {

    private static final String GH_TOKEN_ENV = "GITHUB_TOKEN";

    private final GhApi ghApi;

    public GhApiClient() {
        super(CFG.githubUrl());
        this.ghApi = create(GhApi.class);
    }

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
