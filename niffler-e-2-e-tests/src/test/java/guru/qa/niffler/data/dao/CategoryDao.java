package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.CategoryEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryDao {

    Optional<CategoryEntity> findCategoryById(UUID id);

    Optional<CategoryEntity> findCategoryByName(String name, String username);

    CategoryEntity create(CategoryEntity category);

    CategoryEntity update(CategoryEntity entity);

    List<CategoryEntity> findAllCategories(String username);

}
