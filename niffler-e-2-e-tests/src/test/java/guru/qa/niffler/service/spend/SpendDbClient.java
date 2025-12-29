package guru.qa.niffler.service.spend;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.spend.SpendRepository;
import guru.qa.niffler.data.repository.spend.impl.SpendRepositoryHiberImpl;
import guru.qa.niffler.data.repository.spend.impl.SpendRepositoryJdbcImpl;
import guru.qa.niffler.data.repository.spend.impl.SpendRepositorySpringImpl;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;

@Slf4j
public class SpendDbClient implements SpendClient {

    private final SpendRepository repository = new SpendRepositoryJdbcImpl();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl());

    @Override
    public SpendJson findById(UUID id) {
        Optional<SpendEntity> entity = repository.findById(id);

        return entity.map(SpendJson::fromEntity)
                .orElseThrow(() -> new RuntimeException("Spend not found"));
    }

    public SpendJson findByUsernameAndSpendDescription(String username, String spendDescription) {
        Optional<SpendEntity> entity = repository.findByUsernameAndSpendDescription(username, spendDescription);

        return entity.map(SpendJson::fromEntity)
                .orElseThrow(() -> new RuntimeException("Spend not found"));
    }

    public CategoryJson findCategoryById(UUID id) {
        Optional<CategoryEntity> entity = repository.findCategoryById(id);

        return entity.map(CategoryJson::fromEntity)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public CategoryJson findCategoryByUsernameAndCategoryName(String userName, String categoryName) {
        Optional<CategoryEntity> category = repository.findCategoryByUsernameAndCategoryName(userName, categoryName);

        if (category.isPresent()) {
            return CategoryJson.fromEntity(category.get());
        } else {
            throw new RuntimeException("Category not found");
        }
    }

    public SpendJson create(SpendJson spend) {
        return xaTransactionTemplate.execute(() -> {
                    SpendEntity newSpend = SpendEntity.fromJson(spend);
                    if (spend.category() != null) {
                        var categoryId = spend.category().id();
                        Optional<CategoryEntity> categoryEntity = repository.findCategoryById(categoryId);
                        if (categoryEntity.isPresent()) {
                            newSpend.setCategory(categoryEntity.get());
                        } else {
                            throw new RuntimeException("Category not found");
                        }
                    }

                    SpendEntity entity = repository.create(newSpend);

                    return SpendJson.fromEntity(entity);
                }
        );
    }

    public SpendJson update(SpendJson spendJson) {
        return xaTransactionTemplate.execute(() -> {
            Optional<SpendEntity> spendEntity = repository.findById(spendJson.id());
            if (spendEntity.isPresent()) {
                SpendEntity entity = repository.update(SpendEntity.fromJson(spendJson));

                return SpendJson.fromEntity(entity);
            } else {
                throw new RuntimeException("Spend not found");
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
                throw new RuntimeException("Spend not found");
            }
        });
    }

    public CategoryJson createCategory(CategoryJson category) {
        return xaTransactionTemplate.execute(() -> {
            CategoryEntity entity = repository.createCategory(CategoryEntity.fromJson(category));

            return CategoryJson.fromEntity(entity);
        });
    }

    public void removeCategory(CategoryJson category) {
        xaTransactionTemplate.execute(() -> {
            Optional<CategoryEntity> categoryEntity = repository.findCategoryById(category.id());
            if (categoryEntity.isPresent()) {
                repository.removeCategory(categoryEntity.get());

                return null;
            } else {
                throw new RuntimeException("Category not found");
            }
        });
    }

}
