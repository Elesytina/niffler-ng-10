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
        SpendEntity updated = spendDao.updateSpend(spendEntity);

        return SpendJson.fromEntity(updated);
    }

    @Override
    public void deleteSpends(List<String> ids, String userName) {
        List<UUID> uuids = ids.stream().map(UUID::fromString).toList();
        boolean isSuccess = spendDao.deleteSpends(uuids, userName);

        if (!isSuccess) {
            throw new RuntimeException("Failed to delete spends");
        }
    }

    @Override
    public SpendJson getSpend(String id) {
        Optional<SpendEntity> spendEntity = spendDao.getSpend(UUID.fromString(id));

        if (spendEntity.isPresent()) {

            return SpendJson.fromEntity(spendEntity.get());
        }
        throw new RuntimeException("Failed to find spend");
    }

    @Override
    public List<SpendJson> getSpends(CurrencyValues currencyFilter, DateFilterValues dateFilterValues, String userName) {
        List<SpendEntity> spendEntities = spendDao.getSpends(currencyFilter, dateFilterValues, userName);

        if (spendEntities.isEmpty()) {
            log.info("No spends found");
        }

        return spendEntities.stream()
                .map(SpendJson::fromEntity)
                .toList();
    }

}
