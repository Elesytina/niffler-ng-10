package guru.qa.niffler.test.rest;

import guru.qa.niffler.api.client.AuthApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import static guru.qa.niffler.test.TestConstantHolder.FAKER;

public class RegisterTest {

    private final AuthApiClient authApiClient = new AuthApiClient();

    @Test
    void newUserShouldRegisteredByApiCall() {
        var userName = FAKER.name().username();
        var password = "password";
        final Response<Void> response = authApiClient.register(userName, password);
        Assertions.assertEquals(201, response.code());
    }

    @Test
    void userShouldLoginByApiCall() {
        final Response<Void> response = authApiClient.login("fishka", "Querty67");

        Assertions.assertTrue(response.isSuccessful());
    }
}
