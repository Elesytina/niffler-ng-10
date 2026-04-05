package guru.qa.niffler.service.auth;

import guru.qa.niffler.api.AuthApi;
import guru.qa.niffler.model.auth.TokenResponse;
import guru.qa.niffler.service.RestClient;
import guru.qa.niffler.utils.OauthUtils;
import okhttp3.HttpUrl;
import org.junit.jupiter.api.Assertions;
import retrofit2.Response;

import java.io.IOException;

import static guru.qa.niffler.api.core.ThreadSafeCookieStore.INSTANCE;
import static guru.qa.niffler.helper.TestConstantHolder.CFG;

public class AuthApiClient extends RestClient {

    private final AuthApi authApi;

    private static final String REDIRECT_URI = "http://localhost:3000/authorized";
    private static final String CLIENT_ID = "client";

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
                    INSTANCE.xsrfCookie()
            ).execute();

            Assertions.assertEquals(201, response.code(), "Unexpected response code");
        } catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }

    public String login(String username, String password) {
        Response<Void> response = Assertions.assertDoesNotThrow(
                () -> authApi.login(
                                username,
                                password,
                                INSTANCE.xsrfCookie()
                        )
                        .execute(), "Couldn't login"
        );
        Assertions.assertEquals(200, response.code(), "Unexpected response code");

        HttpUrl finalUrl = response.raw().request().url();
        String code = finalUrl.queryParameter("code");
        Assertions.assertNotNull(code, "Couldn't find code");

        return code;
    }

    public TokenResponse token(String codeVerifier, String code) {
        Response<TokenResponse> responseToken = Assertions.assertDoesNotThrow(
                () -> authApi.token(
                        code,
                        REDIRECT_URI,
                        codeVerifier,
                        "authorization_code",
                        CLIENT_ID
                ).execute(), "Couldn't get token");
        Assertions.assertEquals(200, responseToken.code(), "Unexpected response code");

        return responseToken.body();
    }

    public String preRequest() {
        String codeVerifier = OauthUtils.generateCodeVerifier();
        String codeChallenge = OauthUtils.generateCodeChallenge(codeVerifier);

        Response<Void> response = Assertions.assertDoesNotThrow(
                () -> authApi.authorize(
                        "code",
                        CLIENT_ID,
                        "openid",
                        REDIRECT_URI,
                        codeChallenge,
                        "S256"
                ).execute(), "Couldn't get token");
        Assertions.assertEquals(200, response.code(), "Unexpected response code");

        return codeVerifier;
    }
}
