package guru.qa.niffler.service.category;

import guru.qa.niffler.data.dao.spend.impl.CategoryDaoSpringJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.model.spend.CategoryJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CategorySpringJdbcClient implements CategoryClient {

    private final CategoryDaoSpringJdbc dao = new CategoryDaoSpringJdbc();

    @Override
    public CategoryJson getCategoryById(UUID categoryId) {
        Optional<CategoryEntity> categoryEntity = dao.findById(categoryId);

        return CategoryJson.fromEntity(categoryEntity
                .orElseThrow(() -> new RuntimeException("Category not found")));
    }

    @Override
    public CategoryJson getCategoryByNameAndUsername(String categoryName, String username) {
        Optional<CategoryEntity> categoryEntity = dao.findByNameAndUsername(categoryName, username);

        return CategoryJson.fromEntity(categoryEntity
                .orElseThrow(() -> new RuntimeException("Category not found")));
    }

    @Override
    public List<CategoryJson> getAllCategoriesByUsername(String username) {

        return dao.findAllByUsername(username)
                .stream()
                .map(CategoryJson::fromEntity)
                .toList();
    }

    @Override
    public CategoryJson createCategory(CategoryJson category) {
        CategoryEntity categoryEntity = dao.create(CategoryEntity.fromJson(category));

        return CategoryJson.fromEntity(categoryEntity);
    }

    @Override
    public CategoryJson updateCategory(CategoryJson categoryJson) {
        CategoryEntity categoryEntity = dao.update(CategoryEntity.fromJson(categoryJson));

        return CategoryJson.fromEntity(categoryEntity);
    }

    @Override
    public void deleteCategory(CategoryJson categoryJson) {
        boolean isDeleted = dao.delete(CategoryEntity.fromJson(categoryJson));

        if (!isDeleted) {
            throw new RuntimeException("Category could not be deleted!");
        }
    }

    public List<CategoryJson> getAllCategories() {

        return new CategoryDaoSpringJdbc().findAll()
                .stream()
                .map(CategoryJson::fromEntity)
                .toList();
    }
}
