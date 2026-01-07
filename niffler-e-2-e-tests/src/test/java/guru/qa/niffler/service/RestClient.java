package guru.qa.niffler.service;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.net.CookieManager;
import java.net.CookiePolicy;

public abstract class RestClient {

    private final OkHttpClient client;
    private final Retrofit retrofit;
    private static final CookieManager cm = new CookieManager(null, CookiePolicy.ACCEPT_ALL);

    public RestClient(String baseUrl, Converter.Factory converterFactory, boolean followRedirects) {
        final HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .followRedirects(followRedirects)
                .cookieJar(
                        new JavaNetCookieJar(cm)
                )
                .build();
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(converterFactory)
                .build();
    }

    public RestClient(String baseUrl) {
        this(baseUrl, JacksonConverterFactory.create(), false);
    }

    public RestClient(String baseUrl, boolean followRedirects) {
        this(baseUrl, JacksonConverterFactory.create(), followRedirects);
    }

    protected <T> T create(Class<T> service) {

        return retrofit.create(service);
    }
}
