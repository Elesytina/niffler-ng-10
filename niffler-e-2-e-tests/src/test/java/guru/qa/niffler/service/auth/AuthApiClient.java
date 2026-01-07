package guru.qa.niffler.service.auth;

import guru.qa.niffler.api.AuthApi;
import guru.qa.niffler.service.RestClient;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;

public class AuthApiClient extends RestClient {

    private final AuthApi authApi;

    public AuthApiClient() {
        super("https://auth.niffler-stage.qa.guru/", true);
        this.authApi = create(AuthApi.class);
    }

    public Response<Void> register(String username, String password) throws IOException {
        authApi.requestRegisterForm().execute();
        return authApi.register(
                username,
                password,
                password,
                cm.getCookieStore().getCookies()
                        .stream()
                        .filter(c -> c.getName().equals("XSRF-TOKEN"))
                        .findFirst()
                        .get()
                        .getValue()
        ).execute();
    }
}
