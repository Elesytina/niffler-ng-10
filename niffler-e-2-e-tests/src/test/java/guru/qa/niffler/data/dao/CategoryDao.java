package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.CategoryEntity;

import java.util.Optional;
import java.util.UUID;

public interface CategoryDao {

    Optional<CategoryEntity> findCategoryById(UUID id);

    CategoryEntity create(CategoryEntity category);

}
