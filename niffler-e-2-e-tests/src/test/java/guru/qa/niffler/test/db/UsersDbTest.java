package guru.qa.niffler.test.db;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.service.user.UserDbClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static guru.qa.niffler.helper.TestConstantHolder.DEFAULT_PASSWORD;
import static guru.qa.niffler.utils.RandomDataUtils.randomCurrency;
import static guru.qa.niffler.utils.RandomDataUtils.randomFullName;
import static guru.qa.niffler.utils.RandomDataUtils.randomName;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@Slf4j
public class UsersDbTest {

    UserDbClient userDbClient = new UserDbClient();

    @Test
    void shouldRegisterNewUser() {
        var username = randomUsername();
        UserJson created = userDbClient.create(username, DEFAULT_PASSWORD);

        UserJson foundUser = userDbClient.findById(created.id());
        log.info(foundUser.toString());

        Assertions.assertNotNull(foundUser.id(), "User should have been found");
    }

    @Test
    @User
    void shouldUpdateUser(UserJson user) {
        var newFirstName = randomName();
        var newFullName = randomFullName();
        var newCurrency = randomCurrency();
        UserJson forUpdate = new UserJson(user.id(),
                user.username(),
                newCurrency,
                newFirstName,
                user.surname(),
                newFullName,
                user.photo(),
                user.photoSmall(),
                null);

        userDbClient.update(forUpdate);
        UserJson foundUser = userDbClient.findById(user.id());

        Assertions.assertNotNull(foundUser.id(), "User should have been found");
        Assertions.assertEquals(foundUser.firstName(), newFirstName, "First name should be the same");
        Assertions.assertEquals(foundUser.fullName(), newFullName, "Full name should be the same");
        Assertions.assertEquals(foundUser.currency(), newCurrency, "Currency should be the same");
    }

    @Test
    @User
    void shouldAddFriends(UserJson user) {
        List<UserJson> friends = userDbClient.addFriends(user, 2);

        UserJson found1 = userDbClient.findByUsername(user.username());

        Assertions.assertEquals(user.username(), found1.username());
        Assertions.assertEquals(2, friends.size(), "Friends should have been added");
    }

    @Test
    @User
    void shouldSendIncomeInvitations(UserJson user) {
        List<UserJson> requesters = userDbClient.addIncomeInvitations(user, 2);

        userDbClient.findByUsername(user.username());

        Assertions.assertEquals(2, requesters.size(), "Friends should have been added");
    }

    @Test
    @User
    void shouldSendOutcomeInvitations(UserJson user) {
        List<UserJson> addressees = userDbClient.addOutcomeInvitations(user, 2);

        Assertions.assertEquals(2, addressees.size(), "Friends should have been added");
    }

    @Test
    @User
    void shouldDeleteUser(UserJson user) {
        userDbClient.delete(user);

        Assertions.assertThrows(RuntimeException.class, () -> userDbClient.findById(user.id()));
        Assertions.assertThrows(RuntimeException.class, () -> userDbClient.findByUsername(user.username()));
    }

}
