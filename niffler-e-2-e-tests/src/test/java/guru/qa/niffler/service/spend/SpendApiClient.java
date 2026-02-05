package guru.qa.niffler.service.spend;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.service.RestClient;
import org.junit.jupiter.api.Assertions;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;

@ParametersAreNonnullByDefault
public class SpendApiClient extends RestClient implements SpendClient {

    private final SpendApi spendApi;

    public SpendApiClient() {
        super(CFG.spendUrl());
        this.spendApi = create(SpendApi.class);
    }

    @Override
    public @Nonnull SpendJson getSpend(UUID id) {
        try {
            Response<SpendJson> response = spendApi.getSpend(id.toString())
                    .execute();
            Assertions.assertEquals(200, response.code(), "Unexpected response code");

            return Objects.requireNonNull(response.body());
        } catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }

    @Override
    public @Nonnull SpendJson addSpend(SpendJson spend) {
        try {
            var response = spendApi.addSpend(spend)
                    .execute();
            Assertions.assertEquals(200, response.code(), "Unexpected response code");

            return Objects.requireNonNull(response.body());
        } catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }

    @Override
    public @Nonnull SpendJson editSpend(SpendJson spendJson) {
        try {
            var response = spendApi.editSpend(spendJson)
                    .execute();
            Assertions.assertEquals(200, response.code(), "Unexpected response code");

            return Objects.requireNonNull(response.body());
        } catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }

    @Override
    public @Nonnull CategoryJson createCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.createCategory(category)
                    .execute();
            Assertions.assertEquals(200, response.code(), "Unexpected response code");

            return Objects.requireNonNull(response.body());
        } catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }

    @Override
    public @Nonnull CategoryJson updateCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.updateCategory(category)
                    .execute();
            Assertions.assertEquals(200, response.code(), "Unexpected response code");

            return Objects.requireNonNull(response.body());
        } catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }

    @Override
    public @Nonnull List<CategoryJson> getCategories(String username) {
        try {
            Response<List<CategoryJson>> response = spendApi.getCategories(username, false)
                    .execute();

            Assertions.assertEquals(200, response.code(), "Unexpected response code");

            return response.body() == null
                    ? Collections.emptyList()
                    : response.body();
        } catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }

    @Override
    public void deleteSpends(List<UUID> uuids, String username) {
        var ids = uuids.stream().map(UUID::toString).toList();
        try {
            Response<Void> response = spendApi.deleteSpends(ids, username)
                    .execute();

            Assertions.assertEquals(200, response.code(), "Unexpected response code");
        } catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }
}
