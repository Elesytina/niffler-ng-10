package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.UUID;

public interface SpendApi {

    @GET("internal/spends/{id}")
    Call<SpendJson> getSpend(@Path("id") UUID id);

    @GET("internal/spends/all")
    Call<List<SpendJson>> getAllSpends();

    @POST("internal/spends/add")
    Call<SpendJson> createSpend(@Body SpendJson spend);

    @PATCH("internal/spends/edit")
    Call<SpendJson> editSpend(@Body SpendJson spend);

    @DELETE("internal/spends/remove")
    Call<SpendJson> removeSpend(@Body SpendJson spend);

    @POST("internal/categories/add")
    Call<CategoryJson> createCategory(@Body CategoryJson category);

    @PATCH("internal/categories/edit")
    Call<CategoryJson> editCategory(@Body CategoryJson category);

    @GET("internal/categories/all")
    Call<List<CategoryJson>> getAllCategories();

}
