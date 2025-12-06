package guru.qa.niffler.data.dao.spend;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryDao {

    Optional<CategoryEntity> findById(UUID id);

    Optional<CategoryEntity> findByNameAndUsername(String name, String username);

    List<CategoryEntity> findAllByUsername(String username);

    List<CategoryEntity> findAll();

    CategoryEntity create(CategoryEntity category);

    CategoryEntity update(CategoryEntity entity);

    boolean delete(CategoryEntity entity);

}
