package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.auth.TokenResponse;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.service.auth.AuthApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AuthTest {

    private final AuthApiClient authApiClient = new AuthApiClient();

    @User
    @Test
    void oauthTest(UserJson user) {
        String codeVerifier = authApiClient.preRequest();
        String code = authApiClient.login(user.username(), user.testData().password());
        TokenResponse responseToken = authApiClient.token(codeVerifier, code);

        Assertions.assertNotNull(responseToken, "Token is null");
        Assertions.assertNotNull(responseToken.getAccessToken(), "Access token is null");
    }

}
