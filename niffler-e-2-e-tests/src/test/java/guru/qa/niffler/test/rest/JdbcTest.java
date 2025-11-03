package guru.qa.niffler.test.rest;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.category.CategoryDbClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@Slf4j
public class JdbcTest {

    private final CategoryDbClient dbClient = new CategoryDbClient();

    @Test
    void shouldFindCategoryById() {
        Optional<CategoryJson> categoryJson = dbClient.findCategoryById(UUID.fromString("3bd3ce14-f496-4d01-9f44-cd8bf80001f8"));
        log.info(categoryJson.get().toString());
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
}
