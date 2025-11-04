package guru.qa.niffler.service.spend;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DateFilterValues;
import guru.qa.niffler.model.SpendJson;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class SpendDbClient implements SpendClient {

    private final SpendDao spendDao = new SpendDaoJdbc();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();

    @Override
    public SpendJson getSpendById(UUID id) {
        Optional<SpendEntity> spendEntity = spendDao.findSpendById(id);

        if (spendEntity.isPresent()) {

            return SpendJson.fromEntity(spendEntity.get());
        }
        throw new RuntimeException("Failed to find spend");
    }

    @Override
    public List<SpendJson> getAllSpendsByFiltersAndUsername(CurrencyValues currencyFilter, DateFilterValues dateFilterValues, String userName) {
        List<SpendEntity> spendEntities = spendDao.findAllSpendsByFiltersAndUsername(currencyFilter, dateFilterValues, userName);

        return spendEntities.stream()
                .map(SpendJson::fromEntity)
                .toList();
    }

    @Override
    public List<SpendJson> getAllSpendsByUsername(String userName) {
        List<SpendEntity> spendEntities = spendDao.findAllSpendsByUsername(userName);

        return spendEntities.stream()
                .map(SpendJson::fromEntity)
                .toList();
    }

    @Override
    public SpendJson createSpend(SpendJson spend) {
        SpendEntity spendEntity = SpendEntity.fromJson(spend);

        if (spendEntity.getCategory().getId() == null) {
            CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
            spendEntity.setCategory(categoryEntity);
        }

        return SpendJson.fromEntity(spendDao.create(spendEntity));
    }

    @Override
    public SpendJson updateSpend(SpendJson spendJson) {
        SpendEntity spendEntity = SpendEntity.fromJson(spendJson);
        SpendEntity updated = spendDao.update(spendEntity);

        return SpendJson.fromEntity(updated);
    }

    @Override
    public void deleteSpends(List<UUID> ids, String userName) {
        boolean isSuccess = spendDao.deleteSpends(ids, userName);

        if (!isSuccess) {
            throw new RuntimeException("Failed to delete spends");
        }
    }

    @Override
    public void deleteSpend(SpendJson spend) {
        SpendEntity spendEntity = SpendEntity.fromJson(spend);
        boolean isSuccess = spendDao.deleteSpend(spendEntity);

        if (!isSuccess) {
            throw new RuntimeException("Failed to delete spends");
        }
    }

}
