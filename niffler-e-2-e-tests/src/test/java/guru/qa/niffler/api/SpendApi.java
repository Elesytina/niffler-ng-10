package guru.qa.niffler.api;

import guru.qa.niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.UUID;

public interface SpendApi {

    @GET("internal/spends/{id}")
    Call<SpendJson> getSpend(@Path("id") UUID id, @Query("username") String userName);

    @GET("internal/spends/all")
    Call<List<SpendJson>> getAllSpends(@Query("username") String user);

    @POST("internal/spends/add")
    Call<SpendJson> createSpend(@Body SpendJson spend);

    @PATCH("internal/spends/edit")
    Call<SpendJson> editSpend(@Body SpendJson spend);

    @DELETE("internal/spends/remove")
    Call<SpendJson> removeSpend(@Body SpendJson spend);

}
