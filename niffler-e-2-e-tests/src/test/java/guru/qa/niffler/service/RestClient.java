package guru.qa.niffler.service;

import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import io.qameta.allure.okhttp3.AllureOkHttp3;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.net.CookieManager;
import java.net.CookiePolicy;

public abstract class RestClient {

    private final Retrofit retrofit;

    public RestClient(String baseUrl, Converter.Factory converterFactory, boolean followRedirects, Interceptor... interceptors) {
        final OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .followRedirects(followRedirects)
                .cookieJar(
                        new JavaNetCookieJar(
                                new CookieManager(ThreadSafeCookieStore.INSTANCE,
                                        CookiePolicy.ACCEPT_ALL)
                        )
                );

        for (Interceptor interceptor : interceptors) {
            clientBuilder.addInterceptor(interceptor);
        }
        clientBuilder.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        clientBuilder.addNetworkInterceptor(new AllureOkHttp3());
        OkHttpClient client = clientBuilder.build();

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
