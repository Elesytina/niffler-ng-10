package guru.qa.niffler.data.dao.spend;

import guru.qa.niffler.model.enums.CurrencyValues;
import guru.qa.niffler.model.enums.DateFilterValues;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendDao {

    Optional<SpendEntity> findById(UUID id);

    List<SpendEntity> findByUsername(String userName);

    List<SpendEntity> findAllByFiltersAndUsername(CurrencyValues currencyFilter, DateFilterValues dateFilterValues, String userName);

    List<SpendEntity> findAll();

    SpendEntity create(SpendEntity entity);

    SpendEntity update(SpendEntity spendEntity);

    boolean delete(List<UUID> ids);

    boolean delete(SpendEntity spendEntity);

}
