package guru.qa.niffler.test.db;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.service.user.UserDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@Slf4j
public class UsersDbTest {

    private final UserDbClient userDbClient = new UserDbClient();

    @Test
    void shouldRegisterNewUser() {
        var username = randomUsername();
        UserJson created = userDbClient.create(username, "123");

        UserJson foundUser = userDbClient.findById(created.id());
        log.info(foundUser.toString());

        Assertions.assertNotNull(foundUser.id(), "User should have been found");
    }

    @Test
    void shouldUpdateUser() {
        var username = randomUsername();
        UserJson created = userDbClient.create(username, "123");
        var newFirstName = RandomDataUtils.randomName();
        var newFullName = RandomDataUtils.randomFullName();
        var newCurrency = RandomDataUtils.randomCurrency();
        UserJson forUpdate = new UserJson(created.id(),
                created.username(),
                newCurrency,
                newFirstName,
                created.surname(),
                newFullName,
                created.photo(),
                created.photoSmall(),
                null);

        userDbClient.update(forUpdate);

        UserJson foundUser = userDbClient.findById(created.id());
        log.info(foundUser.toString());

        Assertions.assertNotNull(foundUser.id(), "User should have been found");
        Assertions.assertEquals(foundUser.firstName(), newFirstName, "First name should be the same");
        Assertions.assertEquals(foundUser.fullName(), newFullName, "Full name should be the same");
        Assertions.assertEquals(foundUser.currency(), newCurrency, "Currency should be the same");
    }

    @Test
    @User
    void shouldAddFriends(UserJson user) {
        List<UserJson> friends = userDbClient.addFriends(user, 2);

        Assertions.assertEquals(2, friends.size(), "Friends should have been added");
    }

    @Test
    @User
    void shouldSendIncomeInvitations(UserJson userJson) {
        UserJson requester = userJson.testData().incomeInvitations().getFirst();

        List<UserJson> requesters = userDbClient.addIncomeInvitations(requester, 2);

        Assertions.assertEquals(2, requesters.size(), "Friends should have been added");
    }

    @Test
    @User
    void shouldSendOutcomeInvitations(UserJson userJson) {
        List<UserJson> addressees = userDbClient.addOutcomeInvitations(userJson, 2);

        Assertions.assertEquals(2, addressees.size(), "Friends should have been added");
    }

    @Test
    @User
    void shouldDeleteUser(UserJson user) {
        userDbClient.delete(user);

        Assertions.assertThrows(RuntimeException.class, () -> userDbClient.findById(user.id()));
    }

}
