package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.service.user.UserApiClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.parallel.Isolated;

import java.util.List;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@Slf4j
@Isolated
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetUsersApiTest {

    private final UserApiClient userApiClient = new UserApiClient();

    @Order(2)
    @User(incomeInvitationsCount = 1)
    @Test
    void shouldGelAllUsers(UserJson user) {
        var username = user.username();
        var friend = user.testData().incomeInvitations().getFirst();

        List<UserJson> found = userApiClient.findAllByUsername(username, friend.username());

        Assertions.assertNotNull(found, "User not found");
        Assertions.assertFalse(found.isEmpty(), "users are not found");
        Assertions.assertTrue(found.stream().anyMatch(u -> u.id().equals(friend.id())), "id should be in results");
    }

    @Order(1)
    @User
    @Test
    void shouldGelNoUsers(UserJson user) {
        var username = user.username();

        List<UserJson> found = userApiClient.findAllByUsername(username, randomAlphanumeric(25));

        Assertions.assertNotNull(found, "User not found");
        Assertions.assertTrue(found.isEmpty(), "user should not be found");
    }
}
