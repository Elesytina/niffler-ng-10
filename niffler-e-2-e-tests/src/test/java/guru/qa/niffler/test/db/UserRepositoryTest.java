package guru.qa.niffler.test.db;

import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.service.user.UserDbClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static guru.qa.niffler.utils.RandomDataUtils.*;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

public class UserRepositoryTest {
    UserDbClient userDbClient = new UserDbClient();

    @Test
    void shouldRegisterNewUserWithSpringJdbc() {
        var username = "Ivan_Vasilievitch6-%s".formatted(randomNumeric(2));

        UserJson userJson = new UserJson(null,
                username,
                randomCurrency(),
                randomName(),
                randomSurname(),
                randomFullName(),
                null,
                null);

        UserJson created = userDbClient.createUserSpringJdbc(userJson, "123");

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
