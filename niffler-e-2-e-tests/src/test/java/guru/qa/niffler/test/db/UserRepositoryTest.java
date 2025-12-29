package guru.qa.niffler.test.db;

import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.service.user.UserDbClientHibernate;
import guru.qa.niffler.utils.RandomDataUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;
@Slf4j
public class UserRepositoryTest {

    UserDbClientHibernate userDbClient = new UserDbClientHibernate();

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
                created.photoSmall());

        userDbClient.update(forUpdate);

        UserJson foundUser = userDbClient.findById(created.id());
        log.info(foundUser.toString());

        Assertions.assertNotNull(foundUser.id(), "User should have been found");
        Assertions.assertEquals(foundUser.firstName(), newFirstName, "First name should be the same");
        Assertions.assertEquals(foundUser.fullName(), newFullName, "Full name should be the same");
        Assertions.assertEquals(foundUser.currency(), newCurrency, "Currency should be the same");
    }


    @Test
    void shouldAddFriends() {
        UUID userId1 = UUID.fromString("cc846c04-21fb-49be-9c87-fb68efcd62b3");
        UserJson requester = userDbClient.findById(userId1);

        userDbClient.addFriends(requester, 2);

        UserJson found1 = userDbClient.findByUsername(requester.username());

        Assertions.assertEquals(requester.username(), found1.username());
    }

    @Test
    void shouldSendIncomeInvitations() {
        UUID userId1 = UUID.fromString("d8f486fb-2636-43e3-a160-42c97fe13c34");
        UserJson requester = userDbClient.findById(userId1);

        userDbClient.addIncomeInvitations(requester, 2);

        userDbClient.findByUsername(requester.username());
    }

    @Test
    void shouldSendOutcomeInvitations() {
        UUID userId1 = UUID.fromString("d8f486fb-2636-43e3-a160-42c97fe13c34");
        UserJson requester = userDbClient.findById(userId1);

        userDbClient.addOutcomeInvitations(requester, 2);

        userDbClient.findByUsername(requester.username());
    }

    @Test
    void shouldDeleteUser() {
        UUID userId = UUID.fromString("d1150710-e4cb-11f0-9e8d-b67362032555");
        UserJson userJson = userDbClient.findById(userId);
        log.info(userJson.toString());
        userDbClient.delete(userJson);
        Assertions.assertThrows(RuntimeException.class, () -> userDbClient.findById(userId));
    }

}
