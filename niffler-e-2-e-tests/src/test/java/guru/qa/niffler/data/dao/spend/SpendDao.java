package guru.qa.niffler.data.dao.spend;

import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.enums.CurrencyValues;
import guru.qa.niffler.model.enums.DateFilterValues;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface SpendDao {

    Optional<SpendEntity> findById(UUID id);

    List<SpendEntity> findByUsername(String userName);

    Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String spendDescription);

    List<SpendEntity> findAllByFiltersAndUsername(@Nullable CurrencyValues currencyFilter,
                                                  @Nullable DateFilterValues dateFilterValues,
                                                  String userName);

    List<SpendEntity> findAll();

    SpendEntity create(SpendEntity entity);

    SpendEntity update(SpendEntity spendEntity);

    boolean delete(List<UUID> ids);

    boolean delete(SpendEntity spendEntity);

}
