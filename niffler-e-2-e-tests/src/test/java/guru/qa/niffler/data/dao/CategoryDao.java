package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.CategoryEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryDao {

    Optional<CategoryEntity> findById(UUID id);

    Optional<CategoryEntity> findByNameAndUsername(String name, String username);

    List<CategoryEntity> findAllByUsername(String username);

    CategoryEntity create(CategoryEntity category);

    CategoryEntity update(CategoryEntity entity);

    boolean delete(CategoryEntity entity);

}
