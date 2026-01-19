package guru.qa.niffler.service.spend;

import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;

import java.util.List;
import java.util.UUID;

public interface SpendClient {

    SpendJson getSpend(UUID id);

    SpendJson addSpend(SpendJson spend);

    SpendJson editSpend(SpendJson spendJson);

    CategoryJson createCategory(CategoryJson category);

    CategoryJson updateCategory(CategoryJson category);

    List<CategoryJson> getCategories(String username);
}
