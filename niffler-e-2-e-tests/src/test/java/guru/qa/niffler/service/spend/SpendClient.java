package guru.qa.niffler.service.spend;

import guru.qa.niffler.model.enums.CurrencyValues;
import guru.qa.niffler.model.enums.DateFilterValues;
import guru.qa.niffler.model.spend.SpendJson;

import java.util.List;
import java.util.UUID;

public interface SpendClient {

    SpendJson getSpendById(UUID id);

    List<SpendJson> getAllSpendsByFiltersAndUsername(CurrencyValues currencyFilter, DateFilterValues dateFilterValues, String userName);

    List<SpendJson> getAllSpendsByUsername(String userName);

    SpendJson createSpend(SpendJson spend);

    SpendJson updateSpend(SpendJson spendJson);

    void deleteSpend(SpendJson spend);

}
