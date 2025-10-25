package guru.qa.niffler.service.category;

import guru.qa.niffler.model.CategoryJson;

import java.util.List;
import java.util.Optional;


public class CategoryDbClient implements CategoryClient {

    @Override
    public CategoryJson createCategory(CategoryJson category) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CategoryJson updateCategory(CategoryJson categoryJson) {
        throw new UnsupportedOperationException("Not implemented :(");
    }

    @Override
    public Optional<CategoryJson> findCategoryByNameAndUsername(String categoryName, String username) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<CategoryJson> getAllCategories(String username) {
        throw new UnsupportedOperationException("Not implemented :(");
    }
}
