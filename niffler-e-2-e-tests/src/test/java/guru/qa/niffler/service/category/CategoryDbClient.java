package guru.qa.niffler.service.category;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.model.CategoryJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class CategoryDbClient implements CategoryClient {

    private final CategoryDao categoryDao = new CategoryDaoJdbc();

    @Override
    public CategoryJson getCategoryById(UUID categoryId) {
        Optional<CategoryEntity> categoryEntity = categoryDao.findCategoryById(categoryId);

        if (categoryEntity.isPresent()) {

            return CategoryJson.fromEntity(categoryEntity.get());
        }
        throw new RuntimeException("Failed to find spend");
    }

    @Override
    public CategoryJson getCategoryByNameAndUsername(String categoryName, String username) {
        Optional<CategoryEntity> categoryEntity = categoryDao.findCategoryByNameAndUsername(categoryName, username);

        if (categoryEntity.isPresent()) {

            return CategoryJson.fromEntity(categoryEntity.get());
        }
        throw new RuntimeException("Failed to find spend");
    }

    @Override
    public List<CategoryJson> getAllCategoriesByUsername(String username) {
        List<CategoryEntity> categoryEntities = categoryDao.findAllCategoriesByUsername(username);

        return categoryEntities.stream()
                .map(CategoryJson::fromEntity)
                .toList();
    }

    @Override
    public CategoryJson createCategory(CategoryJson category) {
        CategoryEntity entity = CategoryEntity.fromJson(category);
        CategoryEntity categoryEntity = categoryDao.create(entity);

        return CategoryJson.fromEntity(categoryEntity);
    }

    @Override
    public CategoryJson updateCategory(CategoryJson categoryJson) {
        CategoryEntity entity = CategoryEntity.fromJson(categoryJson);
        CategoryEntity categoryEntity = categoryDao.update(entity);

        return CategoryJson.fromEntity(categoryEntity);
    }

    @Override
    public void deleteCategory(CategoryJson categoryJson) {
        CategoryEntity entity = CategoryEntity.fromJson(categoryJson);
        boolean isSuccess = categoryDao.delete(entity);

        if (!isSuccess) {
            throw new RuntimeException("Failed to delete category");
        }
    }

}
