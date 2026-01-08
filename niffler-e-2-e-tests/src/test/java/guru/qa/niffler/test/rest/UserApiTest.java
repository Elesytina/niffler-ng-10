package guru.qa.niffler.test.rest;

import guru.qa.niffler.service.auth.AuthApiClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

public class UserApiTest {

    @Test
    void shouldRegisterNewUser() {
        AuthApiClient authApiClient = new AuthApiClient();
        authApiClient.register(RandomDataUtils.randomUsername(), "123");
    }
}
