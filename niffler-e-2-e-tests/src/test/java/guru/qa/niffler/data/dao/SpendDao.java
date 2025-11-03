package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DateFilterValues;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendDao {

    SpendEntity create(SpendEntity entity);

    SpendEntity updateSpend(SpendEntity spendEntity);

    boolean deleteSpends(List<UUID> ids, String userName);

    Optional<SpendEntity> getSpend(UUID id);

    List<SpendEntity> getSpends(CurrencyValues currencyFilter, DateFilterValues dateFilterValues, String userName);

}
