package guru.qa.niffler.service.category;

import guru.qa.niffler.api.CategoryApi;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.spend.CategoryJson;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.jupiter.api.Assertions;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class CategoryApiClient implements CategoryClient {

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

    private final CategoryApi categoryApi = retrofit.create(CategoryApi.class);

    @Override
    public CategoryJson getCategoryById(UUID categoryId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public CategoryJson getCategoryByNameAndUsername(String categoryName, String username) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<CategoryJson> getAllCategoriesByUsername(String username) {
        final Response<List<CategoryJson>> response;
        try {
            response = categoryApi.getAllCategories(username)
                    .execute();
            Assertions.assertEquals(200, response.code(), "Unexpected response code");
            return response.body();
        } catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }

    @Override
    public CategoryJson createCategory(CategoryJson categoryJson) {
        final Response<CategoryJson> response;
        try {
            response = categoryApi.createCategory(categoryJson)
                    .execute();
            Assertions.assertEquals(200, response.code(), "Unexpected response code");
            return response.body();
        } catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }

    @Override
    public CategoryJson updateCategory(CategoryJson categoryJson) {
        final Response<CategoryJson> response;
        try {
            response = categoryApi.updateCategory(categoryJson)
                    .execute();
            Assertions.assertEquals(200, response.code(), "Unexpected response code");
            return response.body();
        } catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }

    @Override
    public void deleteCategory(CategoryJson categoryJson) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
