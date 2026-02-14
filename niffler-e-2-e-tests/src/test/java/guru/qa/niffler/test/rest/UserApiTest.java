package guru.qa.niffler.test.rest;

import guru.qa.niffler.service.user.UserApiClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.helper.TestConstantHolder.DEFAULT_PASSWORD;
import static guru.qa.niffler.service.await.AwaitConditionCommonService.awaitForCondition;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@Slf4j
@Order(2)
public class UserApiTest {

    private final UserApiClient userApiClient = new UserApiClient();

    @Test
    void shouldRegisterNewUser() {
        var username = randomUsername();
        log.info("Registering new user {}", username);

        userApiClient.create(username, DEFAULT_PASSWORD);

        awaitForCondition(() -> userApiClient.findByUsername(username) != null);
    }
}
