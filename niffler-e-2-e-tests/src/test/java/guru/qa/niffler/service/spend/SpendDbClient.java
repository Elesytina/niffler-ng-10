package guru.qa.niffler.service.spend;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.spend.SpendRepository;
import guru.qa.niffler.data.repository.spend.impl.SpendRepositoryHiberImpl;
import guru.qa.niffler.data.repository.spend.impl.SpendRepositoryJdbcImpl;
import guru.qa.niffler.data.repository.spend.impl.SpendRepositorySpringImpl;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.enums.RepositoryImplType;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;

@Slf4j
@ParametersAreNonnullByDefault
public class SpendDbClient implements SpendClient {

    private final SpendRepository repository;

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.userdataJdbcUrl());

    public SpendDbClient(RepositoryImplType type) {
        repository = switch (type) {
            case JDBC -> new SpendRepositoryJdbcImpl();
            case SPRING_JDBC -> new SpendRepositorySpringImpl();
            case HIBERNATE -> new SpendRepositoryHiberImpl();
        };
    }

    @Override
    public @Nonnull SpendJson getSpend(UUID id) {
        Optional<SpendEntity> entity = repository.findById(id);

        return entity.map(SpendJson::fromEntity)
                .orElseThrow(() -> new RuntimeException("Spend not found"));
    }

    public @Nonnull SpendJson findByUsernameAndSpendDescription(String username, String spendDescription) {
        Optional<SpendEntity> entity = repository.findByUsernameAndSpendDescription(username, spendDescription);

        return entity.map(SpendJson::fromEntity)
                .orElseThrow(() -> new RuntimeException("Spend not found"));
    }

    public @Nonnull CategoryJson findCategoryById(UUID id) {
        Optional<CategoryEntity> entity = repository.findCategoryById(id);

        return entity.map(CategoryJson::fromEntity)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public @Nonnull CategoryJson findCategoryByUsernameAndCategoryName(String userName, String categoryName) {
        Optional<CategoryEntity> category = repository.findCategoryByUsernameAndCategoryName(userName, categoryName);

        if (category.isPresent()) {
            return CategoryJson.fromEntity(category.get());
        } else {
            throw new RuntimeException("Category not found");
        }
    }

    @Override
    public @Nullable SpendJson addSpend(SpendJson spend) {
        return xaTransactionTemplate.execute(() -> {
            SpendEntity newSpend = SpendEntity.fromJson(spend);

            SpendEntity entity = repository.create(newSpend);

            return SpendJson.fromEntity(entity);
        });
    }

    @Override
    public @Nullable SpendJson editSpend(SpendJson spendJson) {
        return xaTransactionTemplate.execute(() -> {
            Optional<SpendEntity> spendEntity = repository.findById(spendJson.id());

            if (spendEntity.isPresent()) {
                SpendEntity entity = repository.update(SpendEntity.fromJson(spendJson));

                return SpendJson.fromEntity(entity);
            } else {
                throw new RuntimeException("Spend with id %s not found".formatted(spendJson.id()));
            }
        });
    }

    public void remove(SpendJson spend) {
        xaTransactionTemplate.execute(() -> {
            Optional<SpendEntity> spendEntity = repository.findById(spend.id());

            if (spendEntity.isPresent()) {
                repository.removeSpend(spendEntity.get());

                return null;
            } else {
                throw new RuntimeException("Spend with id %s not found".formatted(spend.id()));
            }
        });
    }

    @Override
    public void deleteSpends(List<UUID> uuids, String username) {
        xaTransactionTemplate.execute(() -> {
            for (UUID uuid : uuids) {
                Optional<SpendEntity> spendEntity = repository.findById(uuid);

                if (spendEntity.isPresent()) {
                    repository.removeSpend(spendEntity.get());

                    return null;
                } else {
                    throw new RuntimeException("Spend with id %s not found".formatted(uuid));
                }
            }
            return null;
        });
    }

    @Override
    @Nullable
    public CategoryJson createCategory(CategoryJson category) {
        return xaTransactionTemplate.execute(() -> {
            CategoryEntity entity = repository.createCategory(CategoryEntity.fromJson(category));

            return CategoryJson.fromEntity(entity);
        });
    }

    @Override
    @Nullable
    public CategoryJson updateCategory(CategoryJson category) {
        return xaTransactionTemplate.execute(() -> {
            CategoryEntity entity = repository.updateCategory(CategoryEntity.fromJson(category));

            return CategoryJson.fromEntity(entity);
        });
    }

    @Override
    public List<CategoryJson> getCategories(String username) {
        List<CategoryEntity> categories = repository.findCategoriesByUsername(username);

        return categories.stream().map(CategoryJson::fromEntity).toList();
    }

    public void removeCategory(CategoryJson category) {
        xaTransactionTemplate.execute(() -> {
            Optional<CategoryEntity> categoryEntity = repository.findCategoryById(category.id());

            if (categoryEntity.isPresent()) {
                repository.removeCategory(categoryEntity.get());

                return null;
            } else {
                throw new RuntimeException("Category with id %s not found".formatted(category.id()));
            }
        });
    }

}
