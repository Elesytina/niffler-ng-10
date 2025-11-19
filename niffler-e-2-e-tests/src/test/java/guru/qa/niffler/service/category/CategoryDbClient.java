package guru.qa.niffler.service.category;

import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.spend.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.model.spend.CategoryJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;


public class CategoryDbClient implements CategoryClient {

    @Override
    public CategoryJson getCategoryById(UUID categoryId) {
        return Databases.xaTransaction(new Databases.XaFunction<>(connect -> {
            Optional<CategoryEntity> categoryEntity = new CategoryDaoJdbc(connect).findById(categoryId);

            if (categoryEntity.isPresent()) {

                return CategoryJson.fromEntity(categoryEntity.get());
            }
            throw new RuntimeException("Failed to find category");
        }, CFG.spendJdbcUrl()));
    }

    @Override
    public CategoryJson getCategoryByNameAndUsername(String categoryName, String username) {
        return Databases.xaTransaction(new Databases.XaFunction<>(connect -> {
            Optional<CategoryEntity> categoryEntity = new CategoryDaoJdbc(connect).findByNameAndUsername(categoryName, username);

            if (categoryEntity.isPresent()) {

                return CategoryJson.fromEntity(categoryEntity.get());
            }
            throw new RuntimeException("Failed to find category");
        }, CFG.spendJdbcUrl()));
    }

    @Override
    public List<CategoryJson> getAllCategoriesByUsername(String username) {
        return Databases.xaTransaction(new Databases.XaFunction<>(connect -> {
            List<CategoryEntity> categoryEntities = new CategoryDaoJdbc(connect).findAllByUsername(username);

            return categoryEntities.stream()
                    .map(CategoryJson::fromEntity)
                    .toList();
        }, CFG.spendJdbcUrl()));
    }

    @Override
    public CategoryJson createCategory(CategoryJson category) {
        return Databases.xaTransaction(new Databases.XaFunction<>(connect -> {
            CategoryEntity entity = CategoryEntity.fromJson(category);
            CategoryEntity categoryEntity = new CategoryDaoJdbc(connect).create(entity);

            return CategoryJson.fromEntity(categoryEntity);
        }, CFG.spendJdbcUrl()));
    }

    @Override
    public CategoryJson updateCategory(CategoryJson categoryJson) {
        return Databases.xaTransaction(new Databases.XaFunction<>(connect -> {
            CategoryEntity entity = CategoryEntity.fromJson(categoryJson);
            CategoryEntity categoryEntity = new CategoryDaoJdbc(connect).update(entity);

            return CategoryJson.fromEntity(categoryEntity);
        }, CFG.spendJdbcUrl()));
    }

    @Override
    public void deleteCategory(CategoryJson categoryJson) {
        Databases.xaTransaction(new Databases.XaConsumer(connect -> {
            CategoryEntity entity = CategoryEntity.fromJson(categoryJson);
            boolean isSuccess = new CategoryDaoJdbc(connect).delete(entity);

            if (!isSuccess) {
                throw new RuntimeException("Failed to delete category");
            }
        }, CFG.spendJdbcUrl()));
    }

    public List<CategoryJson> findAll() {
        return Databases.transaction(connect -> {
            List<CategoryEntity> categoryEntities = new CategoryDaoJdbc(connect).findAll();

            return categoryEntities.stream()
                    .map(CategoryJson::fromEntity)
                    .toList();
        }, CFG.spendJdbcUrl());
    }

}
