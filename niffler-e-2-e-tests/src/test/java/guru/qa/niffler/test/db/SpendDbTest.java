package guru.qa.niffler.test.db;

import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.SpendingCategory;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.enums.CurrencyValues;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.service.spend.SpendDbClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static guru.qa.niffler.utils.RandomDataUtils.randomCategoryName;
import static guru.qa.niffler.utils.RandomDataUtils.randomCurrency;
import static guru.qa.niffler.utils.RandomDataUtils.randomDouble;
import static guru.qa.niffler.utils.RandomDataUtils.randomSentence;

@Slf4j
public class SpendDbTest {

    private final SpendDbClient spendDbClient = new SpendDbClient();

    @Test
    @User(spendings = @Spending(
            category = "category2",
            description = "description",
            amount = 100,
            currency = CurrencyValues.USD))
    void shouldGetSpend(UserJson user) {
        UUID spendId = user.testData().spends().getFirst().id();
        String spendDescription = user.testData().spends().getFirst().description();
        SpendJson spend1 = spendDbClient.getSpend(spendId);
        SpendJson spend2 = spendDbClient.findByUsernameAndSpendDescription(user.username(), spendDescription);

        Assertions.assertEquals(spend1.id(), spend2.id(), "Spends should be same");
    }

    @Test
    @User
    void shouldCreateSpend(UserJson user) {
        var username = user.username();
        CategoryJson categoryJson = new CategoryJson(
                null,
                randomCategoryName(),
                username,
                false
        );
        CategoryJson createdCategory = spendDbClient.createCategory(categoryJson);

        SpendJson spend1 = spendDbClient.addSpend(
                new SpendJson(null,
                        Date.from(Instant.now()),
                        createdCategory,
                        CurrencyValues.USD,
                        randomDouble(10, 100),
                        randomSentence(4),
                        username)
        );
        log.info(spend1.toString());

        Assertions.assertNotNull(spend1.id(), "Spend id should not be null");
    }


    @Test
    @User(spendings = @Spending(
            category = "category4",
            description = "description",
            amount = 100,
            currency = CurrencyValues.USD))
    void shouldGetSpendByIdAndUpdate(UserJson userJson) {
        SpendJson spend = userJson.testData().spends().getFirst();
        var spendId = spend.id();
        var newCurrency = CurrencyValues.USD;
        var newAmount = randomDouble(100, 5000);

        SpendJson forUpdate = new SpendJson(spendId,
                spend.spendDate(),
                spend.category(),
                newCurrency,
                newAmount,
                spend.description(),
                spend.username());
        spendDbClient.editSpend(forUpdate);

        SpendJson spend2 = spendDbClient.getSpend(spendId);

        Assertions.assertEquals(spend.id(), spend2.id(), "Spends should be same");
        Assertions.assertEquals(newCurrency, spend2.currency(), "Spend should have a new currency");
        Assertions.assertEquals(newAmount, spend2.amount(), "Spend should have a new amount");
    }

    @Test
    @User(categories = @SpendingCategory)
    void shouldGetSpendByUsernameAndDescription(UserJson user) {
        var username = user.username();
        CategoryJson category = user.testData().categories().getFirst();

        SpendJson created = spendDbClient.addSpend(
                new SpendJson(null,
                        Date.from(Instant.now()),
                        category,
                        randomCurrency(),
                        randomDouble(10, 10000),
                        randomSentence(4),
                        username)
        );
        Assertions.assertNotNull(created.id(), "Spend id should not be null");

        SpendJson found = spendDbClient.findByUsernameAndSpendDescription(created.username(), created.description());
        Assertions.assertNotNull(found.id(), "Spend description should not be null");
    }

    @Test
    @User
    void shouldCreateCategoryAndRemoveIt(UserJson user) {
        var username = user.username();
        CategoryJson categoryJson = new CategoryJson(
                null,
                randomSentence(3),
                username,
                false
        );
        CategoryJson createdCategory = spendDbClient.createCategory(categoryJson);
        Assertions.assertNotNull(createdCategory, "Category should not be null");
        Assertions.assertDoesNotThrow(() ->
                spendDbClient.findCategoryByUsernameAndCategoryName(
                        username,
                        createdCategory.name()));


        spendDbClient.removeCategory(createdCategory);

        Assertions.assertThrows(RuntimeException.class, () -> spendDbClient.findCategoryById(createdCategory.id()));
    }

}
