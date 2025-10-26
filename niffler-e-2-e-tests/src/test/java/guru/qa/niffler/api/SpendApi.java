package guru.qa.niffler.api;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DateFilterValues;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface SpendApi {
    @POST("internal/spends/add")
    Call<SpendJson> createSpend(@Body SpendJson spend);

    @PATCH("internal/spends/edit")
    Call<SpendJson> editSpend(@Body SpendJson spend);

    @DELETE("internal/spends/remove")
    Call<Void> deleteSpends(@Query("ids") List<String> ids,
                            @Query("username") String username);

    @GET("internal/spends/{id}")
    Call<SpendJson> getSpend(@Path("id") String id);

    @GET("internal/spends/all")
    Call<List<SpendJson>> getAllSpends(@Query("filterPeriod") DateFilterValues filterPeriod,
                                       @Query("filterCurrency") CurrencyValues filterCurrency,
                                       @Query("username") String user);
}
