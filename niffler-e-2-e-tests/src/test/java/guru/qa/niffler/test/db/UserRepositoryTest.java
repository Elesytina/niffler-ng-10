package guru.qa.niffler.test.db;

import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.service.user.UserDbClientHibernate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

public class UserRepositoryTest {
    UserDbClientHibernate userDbClient = new UserDbClientHibernate();

    @ParameterizedTest
    @ValueSource(strings = {"petr1", "petr2", "petr3"})
    void shouldRegisterNewUserWithSpringJdbc(String username) {
        UserJson created = userDbClient.createUser(username, "123");

        UserJson foundUser = userDbClient.getUserById(created.id());

        Assertions.assertNotNull(foundUser.id(), "User should have been found");
    }


    @Test
    void shouldAddFriend() {
        UserJson requester = userDbClient.getUserById(UUID.fromString("f3fd3380-d150-11f0-b64b-fe82b4c2a29a"));
        UserJson addressee = userDbClient.getUserById(UUID.fromString("b7f7bbde-d223-11f0-8a62-5ac75f2af29f"));

        userDbClient.addFriend(requester, addressee);
    }

}
