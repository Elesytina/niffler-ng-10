package guru.qa.niffler.data.repository.spend;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.spend.impl.SpendRepositoryHiberImpl;
import guru.qa.niffler.data.repository.spend.impl.SpendRepositoryJdbcImpl;
import guru.qa.niffler.data.repository.spend.impl.SpendRepositorySpringImpl;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface SpendRepository {

    @Nonnull
    static SpendRepository getInstance() {
        return switch (System.getProperty("repository", "jpa")) {
            case "jpa" -> new SpendRepositoryHiberImpl();
            case "jdbc" -> new SpendRepositoryJdbcImpl();
            case "sjdbc" -> new SpendRepositorySpringImpl();
            default -> throw new IllegalStateException("Unsupported repository: " + System.getProperty("repository"));
        };
    }

    SpendEntity create(SpendEntity spend);

    SpendEntity update(SpendEntity spend);

    CategoryEntity createCategory(CategoryEntity category);

    CategoryEntity updateCategory(CategoryEntity category);

    Optional<CategoryEntity> findCategoryById(UUID id);

    Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String spendName);

    List<CategoryEntity> findCategoriesByUsername(String username);

    Optional<SpendEntity> findById(UUID id);

    Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String spendDescription);

    void removeSpend(SpendEntity spend);

    void removeCategory(CategoryEntity category);

}
