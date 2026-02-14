package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.service.user.UserApiClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

@Slf4j
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Order(1)
public class GetUsersApiTest {

    private final UserApiClient userApiClient = new UserApiClient();


    @User
    @Test
    void shouldGetNoUsers(UserJson userJson) {
        List<UserJson> found = userApiClient.findAllByUsername(userJson.username(), "");

        Assertions.assertNotNull(found, "User not found");
        Assertions.assertFalse(found.isEmpty(), "users are not found");
    }

}
