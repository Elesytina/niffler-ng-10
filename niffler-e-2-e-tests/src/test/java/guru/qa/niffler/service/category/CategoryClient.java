package guru.qa.niffler.service.category;

import guru.qa.niffler.model.CategoryJson;

import java.util.List;
import java.util.UUID;

public interface CategoryClient {

    CategoryJson getCategoryById(UUID categoryId);

    CategoryJson getCategoryByNameAndUsername(String categoryName, String username);

    List<CategoryJson> getAllCategoriesByUsername(String username);

    CategoryJson createCategory(CategoryJson category);

    CategoryJson updateCategory(CategoryJson categoryJson);

    void deleteCategory(CategoryJson categoryJson);

}
