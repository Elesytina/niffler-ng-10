package guru.qa.niffler.test.rest;

import guru.qa.niffler.model.auth.AuthorityJson;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.service.UserDbClient;
import guru.qa.niffler.service.category.CategoryClient;
import guru.qa.niffler.service.category.CategoryDbClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static guru.qa.niffler.utils.RandomDataUtils.*;

@Slf4j
public class JdbcTest {
    private final CategoryClient dbClient = new CategoryDbClient();
    private final UserDbClient authDbClient = new UserDbClient();

    @Test
    void shouldRegisterNewUser() {
        UserJson userJson = new UserJson(null,
                randomUsername(),
                randomCurrency(),
                randomName(),
                randomSurname(),
                randomFullName(),
                null,
                null);
        UserJson created = authDbClient.register(userJson, "123");
        Assertions.assertNotNull(created.id());
        List<AuthorityJson> authorityJsonList = authDbClient.getAuthoritiesById(created.id());
        Assertions.assertEquals(2, authorityJsonList.size(), "Expected 2 authorities but got " + authorityJsonList.size());
    }

}
