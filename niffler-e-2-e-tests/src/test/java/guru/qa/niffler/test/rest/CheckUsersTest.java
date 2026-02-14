package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.service.user.UserApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Isolated;

import java.util.List;

@Isolated
public class CheckUsersTest {

    private final UserApiClient userApiClient = new UserApiClient();

    @User(incomeInvitationsCount = 1)
    @Test
    void shouldGetAllUsers(UserJson user) {
        var username = user.username();
        var friend = user.testData().incomeInvitations().getFirst();

        List<UserJson> found = userApiClient.findAllByUsername(username, friend.username());

        Assertions.assertNotNull(found, "User not found");
        Assertions.assertFalse(found.isEmpty(), "users are not found");
        Assertions.assertTrue(found.stream().anyMatch(u -> u.id().equals(friend.id())), "id should be in results");
    }
}
