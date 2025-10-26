package guru.qa.niffler.service.spend;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DateFilterValues;
import guru.qa.niffler.model.SpendJson;

import java.util.List;

public interface SpendClient {

    SpendJson createSpend(SpendJson spend);

    SpendJson updateSpend(SpendJson spendJson);

    void deleteSpends(List<String> ids, String userName);

    SpendJson getSpend(String id);

    List<SpendJson> getSpends(CurrencyValues currencyFilter, DateFilterValues dateFilterValues, String userName);
}
