package guru.qa.niffler.service;

import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.UUID;

public interface SpendClient {

    SpendJson createSpend(SpendJson spend);

    SpendJson getSpendById(UUID spendId, String userName);

    List<SpendJson> getAllSpends(String username);

    SpendJson updateSpend(SpendJson spend);

    SpendJson deleteSpend(SpendJson spendJson);
}
