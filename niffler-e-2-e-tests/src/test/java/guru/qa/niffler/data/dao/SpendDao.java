package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DateFilterValues;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendDao {

    Optional<SpendEntity> findSpendById(UUID id);

    List<SpendEntity> findAllSpendsByUsername(String userName);

    List<SpendEntity> findAllSpendsByFiltersAndUsername(CurrencyValues currencyFilter, DateFilterValues dateFilterValues, String userName);

    SpendEntity create(SpendEntity entity);

    SpendEntity update(SpendEntity spendEntity);

    boolean deleteSpends(List<UUID> ids, String userName);

    boolean deleteSpend(SpendEntity spendEntity);

}
