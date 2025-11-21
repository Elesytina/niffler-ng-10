package guru.qa.niffler.service.spend;

import guru.qa.niffler.data.dao.spend.impl.SpendDaoSpringJdbc;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.enums.CurrencyValues;
import guru.qa.niffler.model.enums.DateFilterValues;
import guru.qa.niffler.model.spend.SpendJson;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class SpendDbClient implements SpendClient {

    private final SpendDaoSpringJdbc dao = new SpendDaoSpringJdbc();

    @Override
    public SpendJson getSpendById(UUID id) {
        Optional<SpendEntity> entity = dao.findById(id);

        return entity.map(SpendJson::fromEntity)
                .orElseThrow(() -> new RuntimeException("Spend not found"));
    }

    @Override
    public List<SpendJson> getAllSpendsByFiltersAndUsername(CurrencyValues currencyFilter, DateFilterValues dateFilterValues, String userName) {
        List<SpendEntity> entities = dao.findAllByFiltersAndUsername(currencyFilter, dateFilterValues, userName);

        return entities.stream()
                .map(SpendJson::fromEntity)
                .toList();
    }

    @Override
    public List<SpendJson> getAllSpendsByUsername(String userName) {
        List<SpendEntity> entities = dao.findByUsername(userName);

        return entities.stream()
                .map(SpendJson::fromEntity)
                .toList();
    }

    @Override
    public SpendJson createSpend(SpendJson spend) {
        SpendEntity entity = dao.create(SpendEntity.fromJson(spend));

        return SpendJson.fromEntity(entity);
    }

    @Override
    public SpendJson updateSpend(SpendJson spendJson) {
        SpendEntity entity = dao.update(SpendEntity.fromJson(spendJson));

        return SpendJson.fromEntity(entity);
    }

    @Override
    public void deleteSpend(SpendJson spend) {
        boolean isDeleted = dao.delete(SpendEntity.fromJson(spend));

        if (!isDeleted) {
            throw new RuntimeException("Spend not found");
        }
    }

    public List<SpendJson> getAllSpends() {
        List<SpendEntity> spendEntities = new SpendDaoSpringJdbc().findAll();

        return spendEntities.stream()
                .map(SpendJson::fromEntity)
                .toList();
    }
}
