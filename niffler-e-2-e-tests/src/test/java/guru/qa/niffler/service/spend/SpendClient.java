package guru.qa.niffler.service.spend;

import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;

import java.util.List;
import java.util.UUID;

public interface SpendClient {

    SpendJson findById(UUID id);

    SpendJson create(SpendJson spend);

    SpendJson update(SpendJson spendJson);

    void remove(SpendJson spend);

    CategoryJson createCategory(CategoryJson category);

}
