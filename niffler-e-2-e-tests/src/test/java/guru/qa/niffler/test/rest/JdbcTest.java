package guru.qa.niffler.test.rest;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.category.CategoryDbClient;
import guru.qa.niffler.service.spend.SpendDbClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@Slf4j
public class JdbcTest {

    private final CategoryDbClient dbClient = new CategoryDbClient();
    private final SpendDbClient spendDbClient = new SpendDbClient();

    @Test
    void shouldFindCategoryById() {
        Optional<CategoryJson> categoryJson = dbClient.findCategoryById(UUID.fromString("3bd3ce14-f496-4d01-9f44-cd8bf80001f8"));
        categoryJson.ifPresent(json -> log.info(json.toString()));
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
        var categories = dbClient.getAllCategories("fishka");
        log.info(categories.toString());
    }

    @Test
    void shouldGetCategoryByName() {
        var cat = dbClient.findCategoryByName("The Proper Study963", "fishka");
        log.info(cat.toString());
    }

    @Test
    void shouldGetCategoryById() {
        var cat = dbClient.findCategoryById(UUID.fromString("cc7c0b3b-2b17-421e-b0bb-3d0c23976cf1"));
        log.info(cat.toString());
    }


    @Test
    void shouldDeleteSpend() {
        spendDbClient.deleteSpends(List.of("360ddcf2-b95f-11f0-b325-e2532453d952"), "fishka");
    }

    @Test
    void shouldGetAllSpendByFilter() {
        List<SpendJson> spends = spendDbClient.getSpends(CurrencyValues.EUR, null, "duck");
        log.info(spends.toString());
    }

    @Test
    void shouldGetSpendById() {
        SpendJson spend = spendDbClient.getSpend("a4d41184-9776-46d1-93bd-b3ee3bbc4d75");
        log.info(spend.toString());
    }

    @Test
    void shouldCreteSpendWithCategory() {
        Optional<CategoryJson> categoryJson = dbClient.findCategoryById(UUID.fromString("3a6bde3b-2ec7-460b-9864-8745b3d0a333"));
        CategoryJson category = null;
        if (categoryJson.isPresent()) {
            category = categoryJson.get();
        }
        SpendJson spend = new SpendJson(null,
                Date.from(Instant.now()),
                category,
                CurrencyValues.EUR.name(),
                10000.1,
                "restorant",
                "fishka");

        SpendJson created = spendDbClient.createSpend(spend);
        log.info(created.toString());
    }

    @Test
    void shouldUpdateSpend() {
        Optional<CategoryJson> categoryJson = dbClient.findCategoryById(UUID.fromString("63ec3a3b-f193-4e97-9588-883d3179ba55"));

        CategoryJson category = null;
        if (categoryJson.isPresent()) {
            category = categoryJson.get();
        }

        SpendJson spend = new SpendJson(UUID.fromString("67768f42-b95e-11f0-8395-e2532453d952"),
                Date.from(Instant.now().minusSeconds(100)),
                category,
                CurrencyValues.KZT.name(),
                10006.1,
                "dinner2",
                "fishka");

        SpendJson updated = spendDbClient.updateSpend(spend);
        log.info(updated.toString());
    }


}
