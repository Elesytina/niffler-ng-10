package guru.qa.niffler.test.rest;

import guru.qa.niffler.model.auth.AuthUserJson;
import guru.qa.niffler.model.auth.AuthorityJson;
import guru.qa.niffler.model.enums.CurrencyValues;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.service.UserDbClient;
import guru.qa.niffler.service.category.CategoryDbClient;
import guru.qa.niffler.service.spend.SpendDbClient;
import guru.qa.niffler.service.userdata.UserDataUserClient;
import guru.qa.niffler.service.userdata.UserDataUserDbClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static guru.qa.niffler.utils.RandomDataUtils.*;
import static java.time.Instant.now;

@Slf4j
public class JdbcTest {
    private final UserDbClient userDbClient = new UserDbClient();
    private final UserDataUserClient userDataUserClient = new UserDataUserDbClient();
    private final CategoryDbClient categoryClient = new CategoryDbClient();
    private final SpendDbClient spendDbClient = new SpendDbClient();

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

    @Test
    void shouldRegisterNewUserWithSpringJdbc() {
        var username = randomUsername();
        UserJson userJson = new UserJson(null,
                username,
                randomCurrency().name(),
                randomName(),
                randomSurname(),
                randomFullName(),
                null,
                null);
        userDbClient.createUserSpringJdbc(userJson, "123");
        UserJson userJsonByName = userDataUserClient.getUserByUsername(username);
        Assertions.assertNotNull(userJsonByName, "User not found");

        AuthUserJson authUserJson = userDbClient.getAuthUserByName(username);
        Assertions.assertNotNull(authUserJson, "User not found");

        List<AuthorityJson> authorityJsonList = userDbClient.getAuthoritiesByUserId(authUserJson.id());
        Assertions.assertEquals(2, authorityJsonList.size(), "Authorities list should be 2");
    }


    @Test
    void shouldGetAllCategoriesWithSpringJdbc() {
        List<CategoryJson> categoryJsons = categoryClient.getAllCategories();
        log.info(categoryJsons.toString());

        Assertions.assertFalse(categoryJsons.isEmpty(), "Category list should not be empty");
    }

    @Test
    void shouldCreateCategoryWithSpringJdbc() {
        var created = categoryClient.createCategory(
                new CategoryJson(
                        null,
                        randomCategoryName(),
                        "fishka",
                        true
                ));

        log.info(created.toString());
        Assertions.assertNotNull(created, "Category should not be null");
    }

    @Test
    void shouldGetAllSpendsWithSpringJdbc() {
        List<SpendJson> spendJsons = spendDbClient.getAllSpends();
        log.info(spendJsons.toString());

        Assertions.assertFalse(spendJsons.isEmpty(), "Spend list should not be empty");
    }

    @Test
    void shouldCreateSpendWithSpringJdbc() {
        CategoryJson categoryJson = categoryClient.getCategoryById(UUID.fromString("ee6bad98-f842-462c-98d7-848e9c4334c8"));

        var created = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        Date.from(now()),
                        categoryJson,
                        CurrencyValues.RUB,
                        1234.1234,
                        randomSentence(3),
                        "fishka"
                ));

        log.info(created.toString());
        Assertions.assertNotNull(created, "Spend should not be null");
    }
}
