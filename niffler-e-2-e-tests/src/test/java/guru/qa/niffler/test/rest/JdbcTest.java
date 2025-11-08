package guru.qa.niffler.test.rest;

import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.enums.CurrencyValues;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.service.category.CategoryClient;
import guru.qa.niffler.service.category.CategoryDbClient;
import guru.qa.niffler.service.spend.SpendClient;
import guru.qa.niffler.service.spend.SpendDbClient;
import guru.qa.niffler.service.userdata.UserDataUserClient;
import guru.qa.niffler.service.userdata.UserDataUserDbClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@Slf4j
public class JdbcTest {

    private final CategoryClient dbClient = new CategoryDbClient();
    private final SpendClient spendDbClient = new SpendDbClient();
    private final UserDataUserClient userClient = new UserDataUserDbClient();

    @Test
    void shouldFindCategoryById() {
        CategoryJson categoryJson = dbClient.getCategoryById(UUID.fromString("3bd3ce14-f496-4d01-9f44-cd8bf80001f8"));
        log.info(categoryJson.toString());
    }

    @Test
    void shouldCreateCategory() {
        CategoryJson categoryJson = new CategoryJson(
                null,
                "category " + randomAlphanumeric(3),
                "earnest.kerlukeWzdYy",
                true
        );
        CategoryJson categoryJson1 = dbClient.createCategory(categoryJson);
        log.info(categoryJson1.toString());
    }

    @Test
    void shouldUpdateCategory() {
        CategoryJson categoryJson = new CategoryJson(
                UUID.fromString("cc7c0b3b-2b17-421e-b0bb-3d0c23976cf1"),
                "updated category " + randomAlphanumeric(3),
                "fishka",
                true
        );
        CategoryJson categoryJson1 = dbClient.updateCategory(categoryJson);
        log.info(categoryJson1.toString());
    }

    @Test
    void shouldGetAllCategories() {
        var categories = dbClient.getAllCategoriesByUsername("fishka");
        log.info(categories.toString());
    }

    @Test
    void shouldGetCategoryByName() {
        var cat = dbClient.getCategoryByNameAndUsername("The Proper Study963", "fishka");
        log.info(cat.toString());
    }

    @Test
    void shouldGetCategoryById() {
        var cat = dbClient.getCategoryById(UUID.fromString("cc7c0b3b-2b17-421e-b0bb-3d0c23976cf1"));
        log.info(cat.toString());
    }

    @Test
    void shouldDeleteCategory() {
        var cat = dbClient.getCategoryById(UUID.fromString("47022962-9698-4b88-9892-f14bf4aeab5a"));
        dbClient.deleteCategory(cat);
    }

    @Test
    void shouldDeleteSpend() {
        SpendJson spendJson = spendDbClient.getSpendById(UUID.fromString("67768f42-b95e-11f0-8395-e2532453d952"));
        spendDbClient.deleteSpend(spendJson);
    }

    @Test
    void shouldGetAllSpendByFilter() {
        List<SpendJson> spends = spendDbClient.getAllSpendsByFiltersAndUsername(CurrencyValues.EUR, null, "duck");
        log.info(spends.toString());
    }

    @Test
    void shouldGetAllSpendByUser() {
        List<SpendJson> spends = spendDbClient.getAllSpendsByUsername("duck");
        log.info(spends.toString());
    }

    @Test
    void shouldGetSpendById() {
        SpendJson spend = spendDbClient.getSpendById(UUID.fromString("a4d41184-9776-46d1-93bd-b3ee3bbc4d75"));
        log.info(spend.toString());
    }

    @Test
    void shouldCreteSpendWithCategory() {
        CategoryJson categoryJson = dbClient.createCategory(new CategoryJson(
                null,
                "new category "+randomAlphanumeric(2),
                "fishka",
                false
        ));

        SpendJson spend = new SpendJson(null,
                Date.from(Instant.now()),
                categoryJson,
                CurrencyValues.EUR.name(),
                100.1,
                "restorant 3",
                null);

        SpendJson created = spendDbClient.createSpend(spend);
        log.info(created.toString());
    }

    @Test
    void shouldUpdateSpend() {
        CategoryJson categoryJson = dbClient.getCategoryById(UUID.fromString("63ec3a3b-f193-4e97-9588-883d3179ba55"));

        SpendJson spend = new SpendJson(UUID.fromString("67768f42-b95e-11f0-8395-e2532453d952"),
                Date.from(Instant.now().minusSeconds(100)),
                categoryJson,
                CurrencyValues.KZT.name(),
                3000.1,
                "dinner",
                "fishka");

        SpendJson updated = spendDbClient.updateSpend(spend);
        log.info(updated.toString());
    }

    @Test
    void shouldCreteUser() {
        UserJson userJson = new UserJson(null,
                "user" + randomAlphanumeric(4),
                CurrencyValues.EUR.name(),
                "Olga",
                "Petrova",
                "Petrova Olga Ivanovna",
                null,
                null);

        UserJson created = userClient.createUser(userJson);
        log.info(created.toString());
    }

    @Test
    void shouldDeleteUser() {
        UserJson userJson = userClient.getUserByUsername("user111");

        userClient.deleteUser(userJson);
    }

    @Test
    void shouldGetUserById() {
        UserJson userJson = userClient.getUserById(UUID.fromString("485ac3c4-343a-4f81-8bfd-950cf40d22d8"));

        log.info(userJson.toString());
    }

    @Test
    void shouldGetUserByName() {
        UserJson userJson = userClient.getUserByUsername("bonnie.ziemann");

        log.info(userJson.toString());
    }


}
