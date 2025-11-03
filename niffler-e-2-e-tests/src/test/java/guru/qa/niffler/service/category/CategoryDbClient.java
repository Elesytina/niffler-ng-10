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
    public Optional<CategoryJson> findCategoryByNameAndUsername(String categoryName, String username) {
        Optional<CategoryEntity> entity = categoryDao.findCategoryByName(categoryName, username);

        if (entity.isPresent()) {
            CategoryJson categoryJson = CategoryJson.fromEntity(entity.get());
            return Optional.of(categoryJson);
        }

        return Optional.empty();
    }

    @Override
    public List<CategoryJson> getAllCategories(String username) {
        List<CategoryEntity> categoryEntities = categoryDao.findAllCategories(username);

        return categoryEntities.stream()
                .map(CategoryJson::fromEntity)
                .toList();
    }

    @Override
    public Optional<CategoryJson> findCategoryById(UUID categoryId) {
        Optional<CategoryEntity> categoryEntity = categoryDao.findCategoryById(categoryId);

        if (categoryEntity.isPresent()) {
            CategoryJson categoryJson = CategoryJson.fromEntity(categoryEntity.get());
            return Optional.of(categoryJson);
        }

        return Optional.empty();
    }

    @Override
    public Optional<CategoryJson> findCategoryByName(String categoryName, String username) {
        Optional<CategoryEntity> categoryEntity = categoryDao.findCategoryByName(categoryName, username);

        if (categoryEntity.isPresent()) {
            CategoryJson categoryJson = CategoryJson.fromEntity(categoryEntity.get());
            return Optional.of(categoryJson);
        }

        return Optional.empty();
    }

}
