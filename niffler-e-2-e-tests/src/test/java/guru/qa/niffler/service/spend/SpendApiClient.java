package guru.qa.niffler.service.spend;

import guru.qa.niffler.api.CategoryApi;
import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.enums.CurrencyValues;
import guru.qa.niffler.model.enums.DateFilterValues;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.jupiter.api.Assertions;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class SpendApiClient implements SpendClient {

    private static final Config CFG = Config.getInstance();

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

    private final CategoryApi categoryApi = retrofit.create(CategoryApi.class);

    @Override
    public SpendJson findById(UUID id) {
        try {
            Response<SpendJson> response = spendApi.getSpend(id.toString())
                    .execute();

            Assertions.assertEquals(200, response.code(), "Unexpected response code");

            return response.body();
        } catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }

    public List<SpendJson> getAllSpendsByFiltersAndUsername(CurrencyValues currencyFilter, DateFilterValues dateFilterValues, String userName) {
        try {
            Response<List<SpendJson>> response = spendApi.getAllSpends(dateFilterValues, currencyFilter, userName)
                    .execute();

            Assertions.assertEquals(200, response.code(), "Unexpected response code");
            return response.body();
        } catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }

    @Override
    public SpendJson create(SpendJson spend) {
        try {
            var response = spendApi.createSpend(spend)
                    .execute();

            Assertions.assertEquals(200, response.code(), "Unexpected response code");
            return response.body();
        } catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }

    @Override
    public SpendJson update(SpendJson spendJson) {
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
    public void remove(SpendJson spend) {
        try {
            List<String> ids = List.of(spend.id().toString());
            var response = spendApi.deleteSpends(ids, spend.username()).execute();

            Assertions.assertEquals(200, response.code(), "Unexpected response code");
        } catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }

    @Override
    public CategoryJson createCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = categoryApi.createCategory(category)
                    .execute();
            Assertions.assertEquals(200, response.code(), "Unexpected response code");
            return response.body();
        } catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }

    public void deleteSpends(List<UUID> uuids, String userName) {
        var ids = uuids.stream().map(UUID::toString).toList();
        try {
            Response<Void> response = spendApi.deleteSpends(ids, userName)
                    .execute();

            Assertions.assertEquals(200, response.code(), "Unexpected response code");
        } catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }

}
