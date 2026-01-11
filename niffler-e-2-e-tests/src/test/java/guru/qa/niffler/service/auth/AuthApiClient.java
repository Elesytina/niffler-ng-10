package guru.qa.niffler.service.auth;

import guru.qa.niffler.api.AuthApi;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.service.RestClient;
import org.junit.jupiter.api.Assertions;
import retrofit2.Response;

import java.io.IOException;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;

public class AuthApiClient extends RestClient {

    private final AuthApi authApi;

    public AuthApiClient() {
        super(CFG.authUrl(), true);
        this.authApi = create(AuthApi.class);
    }

    public void register(String username, String password) {
        try {
            authApi.requestRegisterForm().execute();
            Response<Void> response = authApi.register(
                    username,
                    password,
                    password,
                    ThreadSafeCookieStore.INSTANCE.xsrfCookie()
            ).execute();

            Assertions.assertEquals(201, response.code(), "Unexpected response code");
        } catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }
}
