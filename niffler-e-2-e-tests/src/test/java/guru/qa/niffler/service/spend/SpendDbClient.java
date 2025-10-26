package guru.qa.niffler.service.spend;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DateFilterValues;
import guru.qa.niffler.model.SpendJson;

import java.util.List;

public class SpendDbClient implements SpendClient {

    @Override
    public SpendJson createSpend(SpendJson spend) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public SpendJson updateSpend(SpendJson spendJson) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void deleteSpends(List<String> ids, String userName) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public SpendJson getSpend(String id, String userName) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<SpendJson> getSpends(CurrencyValues currencyFilter, DateFilterValues dateFilterValues, String userName) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
