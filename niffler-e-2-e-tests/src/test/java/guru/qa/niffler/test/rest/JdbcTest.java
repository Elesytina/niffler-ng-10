package guru.qa.niffler.test.rest;

import guru.qa.niffler.model.auth.AuthUserJson;
import guru.qa.niffler.model.auth.AuthorityJson;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.service.UserDbClient;
import guru.qa.niffler.service.userdata.UserDataUserClient;
import guru.qa.niffler.service.userdata.UserDataUserDbClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static guru.qa.niffler.utils.RandomDataUtils.*;

@Slf4j
public class JdbcTest {
    private final UserDbClient userDbClient = new UserDbClient();
    private final UserDataUserClient userDataUserClient = new UserDataUserDbClient();

    @Test
    void shouldRegisterNewUser() {
        var username = randomUsername();
        UserJson userJson = new UserJson(null,
                username,
                randomCurrency().name(),
                randomName(),
                randomSurname(),
                randomFullName(),
                null,
                null);
        userDbClient.register(userJson, "123");
        UserJson userJsonByName = userDataUserClient.getUserByUsername(username);
        Assertions.assertNotNull(userJsonByName, "User not found");

        AuthUserJson authUserJson = userDbClient.getAuthUserByName(username);
        Assertions.assertNotNull(authUserJson, "User not found");

        List<AuthorityJson> authorityJsonList = userDbClient.getAuthoritiesByUserId(authUserJson.id());
        Assertions.assertEquals(2, authorityJsonList.size(), "Authorities list should be 2");
    }

}
