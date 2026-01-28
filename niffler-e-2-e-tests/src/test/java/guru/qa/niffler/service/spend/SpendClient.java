package guru.qa.niffler.service.spend;

import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import io.qameta.allure.Step;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface SpendClient {

    @Step("get spend by id {id}")
    SpendJson getSpend(UUID id);

    @Step("add new spend {spend}")
    SpendJson addSpend(SpendJson spend);

    @Step("edit spend {spendJson}")
    SpendJson editSpend(SpendJson spendJson);

    @Step("delete spends with id in {uuids} for user {username}")
    void deleteSpends(List<UUID> uuids, String username);

    @Step("create category {category}")
    CategoryJson createCategory(CategoryJson category);

    @Step("update category {category}")
    CategoryJson updateCategory(CategoryJson category);

    @Step("get categories by username {username}")
    List<CategoryJson> getCategories(String username);
}
