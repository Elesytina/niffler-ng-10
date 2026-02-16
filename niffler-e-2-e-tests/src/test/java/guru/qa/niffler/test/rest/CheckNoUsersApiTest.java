package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.service.user.UserApiClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;

import java.util.List;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@Slf4j
@Order(1)
public class CheckNoUsersApiTest {

    private final UserApiClient userApiClient = new UserApiClient();

    @User
    @Test
    void shouldGetNoUsers(UserJson user) {
        List<UserJson> found = userApiClient.findAllByUsername(user.username(), randomAlphanumeric(5));

        Assertions.assertTrue(found.isEmpty(), "users are found");
    }

}
