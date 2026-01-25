package guru.qa.niffler.test.db;

import guru.qa.niffler.jupiter.annotation.Spending;
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

import static guru.qa.niffler.model.enums.CurrencyValues.RUB;
import static guru.qa.niffler.utils.RandomDataUtils.randomCategoryName;
import static guru.qa.niffler.utils.RandomDataUtils.randomDouble;
import static guru.qa.niffler.utils.RandomDataUtils.randomSentence;

@Slf4j
public class SpendDbTest {

    private final SpendDbClient spendDbClient = new SpendDbClient();

    @Test
    @User(spendings = @Spending(
            category = "just spend",
            amount = 100,
            currency = RUB,
            description = "new description for spend"
    ))
    void shouldGetSpend(UserJson userJson) {
        SpendJson spendJson = userJson.testData().spends().getFirst();
        String description = spendJson.description();
        var username = userJson.username();

        SpendJson spend1 = spendDbClient.getSpend(spendJson.id());
        SpendJson spend2 = spendDbClient.findByUsernameAndSpendDescription(username, description);

        Assertions.assertEquals(spend1.id(), spend2.id(), "Spends should be same");
    }

    @Test
    void shouldCreateSpend() {
        var username = "ivan";
        CategoryJson categoryJson = new CategoryJson(
                null,
                randomCategoryName(),
                username,
                false
        );
        CategoryJson createdCategory = spendDbClient.createCategory(categoryJson);
        log.info("Created new category: {}", createdCategory);

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
            category = "just spend",
            amount = 200,
            currency = RUB,
            description = "description for spend"
    ))
    void shouldGetSpendByIdAndUpdate(UserJson userJson) {
        SpendJson spendJson = userJson.testData().spends().getFirst();
        var spendId = spendJson.id();
        SpendJson spend1 = spendDbClient.getSpend(spendId);
        var newCurrency = CurrencyValues.USD;
        var newAmount = randomDouble(100, 5000);

        SpendJson forUpdate = new SpendJson(spendId,
                spend1.spendDate(),
                spend1.category(),
                newCurrency,
                newAmount,
                spend1.description(),
                spend1.username());
        spendDbClient.editSpend(forUpdate);

        SpendJson spend2 = spendDbClient.getSpend(spendId);

        Assertions.assertEquals(spend1.id(), spend2.id(), "Spends should be same");
        Assertions.assertEquals(newCurrency, spend2.currency(), "Spend should have a new currency");
        Assertions.assertEquals(newAmount, spend2.amount(), "Spend should have a new amount");
    }

    @Test
    @User(spendings = @Spending(
            category = "just spend",
            amount = 200,
            currency = RUB,
            description = "description"
    ))
    void shouldGetSpendByUsernameAndDescription(UserJson userJson) {
        SpendJson spendJson = userJson.testData().spends().getFirst();
        var spendDescription = spendJson.description();
        var username = userJson.username();

        SpendJson found = spendDbClient.findByUsernameAndSpendDescription(username, spendDescription);
        Assertions.assertNotNull(found.id(), "Spend description should not be null");
    }

    @Test
    @User
    void shouldCreateCategoryAndRemoveIt(UserJson userJson) {
        var username = userJson.username();
        CategoryJson categoryJson = new CategoryJson(
                null,
                randomSentence(3),
                username,
                false
        );
        CategoryJson createdCategory = spendDbClient.createCategory(categoryJson);
        Assertions.assertDoesNotThrow(() ->
                spendDbClient.findCategoryByUsernameAndCategoryName(
                        createdCategory.username(),
                        createdCategory.name()));


        spendDbClient.removeCategory(createdCategory);

        Assertions.assertThrows(RuntimeException.class, () -> spendDbClient.findCategoryById(createdCategory.id()));
    }

}
