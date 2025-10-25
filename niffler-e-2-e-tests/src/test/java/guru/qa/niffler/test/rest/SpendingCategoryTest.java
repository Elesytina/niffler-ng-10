package guru.qa.niffler.test.rest;


import guru.qa.niffler.jupiter.annotation.SpendingCategory;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.category.CategoryApiClient;
import io.qameta.allure.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SpendingCategoryTest {

    private final CategoryApiClient categoryApiClient = new CategoryApiClient();
    private final String username = "fishka";

    @SpendingCategory(
            username = username,
            archived = false)
    @Test
    @Description("active Category Should Be Present In Profile")
    void activeCategoryShouldBePresentInProfilePositiveTest() {
        List<CategoryJson> allCategories = categoryApiClient.getAllCategories(username);

        var activeCategories = allCategories.stream().filter(cat -> !cat.archived()).toList();

        Assertions.assertFalse(activeCategories.isEmpty(), "There are no active categories");
    }

    @SpendingCategory(
            username = username,
            archived = true)
    @Test
    @Description("archived Category Should Be Present In Profile")
    void test_archivedCategoryShouldBePresentInProfilePositiveTest() {
        List<CategoryJson> allCategories = categoryApiClient.getAllCategories(username);

        var archiveCategories = allCategories.stream().filter(CategoryJson::archived).toList();

        Assertions.assertFalse(archiveCategories.isEmpty(), "There are no archive categories");
    }

}
