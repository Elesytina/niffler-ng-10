package guru.qa.niffler.service.spend;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DateFilterValues;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.Assertions;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class SpendApiClient implements SpendClient {

    private static final Config CFG = Config.getInstance();

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(CFG.spendUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final SpendApi spendApi = retrofit.create(SpendApi.class);

    @Override
    public SpendJson getSpendById(UUID id) {
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
    public List<SpendJson> getAllSpendsByUsername(String userName) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public SpendJson createSpend(SpendJson spend) {
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
    public SpendJson updateSpend(SpendJson spendJson) {
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

    @Override
    public void deleteSpend(SpendJson spend) {
        throw new RuntimeException("Not implemented yet");
    }

}
