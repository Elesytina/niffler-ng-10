package guru.qa.niffler.api.client;

import guru.qa.niffler.model.CategoryJson;

import java.util.List;
import java.util.Optional;

public interface CategoryClient {

    CategoryJson createCategory(CategoryJson category);

    CategoryJson updateCategory(CategoryJson categoryJson);

    Optional<CategoryJson> findCategoryByNameAndUsername(String categoryName, String username);

    List<CategoryJson> getAllCategories(String username);
}
