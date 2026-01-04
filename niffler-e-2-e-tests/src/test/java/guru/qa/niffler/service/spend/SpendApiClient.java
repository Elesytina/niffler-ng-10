package guru.qa.niffler.service.spend;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.jupiter.api.Assertions;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;

@ParametersAreNonnullByDefault
public class SpendApiClient implements SpendClient {

    private static final HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static final OkHttpClient client = new OkHttpClient().newBuilder()
            .addInterceptor(logging)
            .build();

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(CFG.spendUrl())
            .client(client)
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final SpendApi spendApi = retrofit.create(SpendApi.class);

    @Override
    public @Nullable SpendJson getSpend(UUID id) {
        try {
            Response<SpendJson> response = spendApi.getSpend(id.toString())
                    .execute();

            Assertions.assertEquals(200, response.code(), "Unexpected response code");

            return response.body();
        } catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }

    @Override
    public @Nullable SpendJson addSpend(SpendJson spend) {
        try {
            var response = spendApi.addSpend(spend)
                    .execute();

            Assertions.assertEquals(200, response.code(), "Unexpected response code");
            return response.body();
        } catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }

    @Override
    public @Nullable SpendJson editSpend(SpendJson spendJson) {
        try {
            var response = spendApi.editSpend(spendJson)
                    .execute();

            Assertions.assertEquals(200, response.code(), "Unexpected response code");
            return response.body();
        } catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }

    @Override
    public @Nullable CategoryJson createCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.createCategory(category)
                    .execute();
            Assertions.assertEquals(200, response.code(), "Unexpected response code");
            return response.body();
        } catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }

    @Override
    public @Nullable CategoryJson updateCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.updateCategory(category)
                    .execute();
            Assertions.assertEquals(200, response.code(), "Unexpected response code");
            return response.body();
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
