package guru.qa.niffler.service.category;

import guru.qa.niffler.model.CategoryJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryClient {

    CategoryJson createCategory(CategoryJson category);

    CategoryJson updateCategory(CategoryJson categoryJson);

    Optional<CategoryJson> findCategoryByNameAndUsername(String categoryName, String username);

    List<CategoryJson> getAllCategories(String username);

    Optional<CategoryJson> findCategoryById(UUID categoryId);

    Optional<CategoryJson> findCategoryByName(String categoryName, String username);
}
