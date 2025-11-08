package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DateFilterValues;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendDao {

    Optional<SpendEntity> findById(UUID id);

    List<SpendEntity> findByUsername(String userName);

    List<SpendEntity> findAllByFiltersAndUsername(CurrencyValues currencyFilter, DateFilterValues dateFilterValues, String userName);

    SpendEntity create(SpendEntity entity);

    SpendEntity update(SpendEntity spendEntity);

    boolean delete(List<UUID> ids);

    boolean delete(SpendEntity spendEntity);

}
