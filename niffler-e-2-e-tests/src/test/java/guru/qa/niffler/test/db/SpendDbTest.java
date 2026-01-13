package guru.qa.niffler.test.db;

import guru.qa.niffler.model.enums.CurrencyValues;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.service.spend.SpendDbClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static guru.qa.niffler.model.enums.RepositoryImplType.HIBERNATE;
import static guru.qa.niffler.utils.RandomDataUtils.randomCategoryName;
import static guru.qa.niffler.utils.RandomDataUtils.randomCurrency;
import static guru.qa.niffler.utils.RandomDataUtils.randomDouble;
import static guru.qa.niffler.utils.RandomDataUtils.randomSentence;

@Slf4j
public class SpendDbTest {

    private final SpendDbClient spendDbClient = new SpendDbClient(HIBERNATE);

    @Test
    void shouldGetSpend() {
        SpendJson spend1 = spendDbClient.findById(UUID.fromString("a4d41184-9776-46d1-93bd-b3ee3bbc4d75"));
        SpendJson spend2 = spendDbClient.findByUsernameAndSpendDescription("fishka", "Harum iure voluptas aspernatur qui.");

        Assertions.assertNotEquals(spend1.id(), spend2.id(), "Spends should not be same");
    }

    @Test
    void shouldCreateAndDeleteSpend() {
        var username = "ivan";
        CategoryJson categoryJson = new CategoryJson(
                null,
                randomCategoryName(),
                username,
                false
        );
        CategoryJson createdCategory = spendDbClient.createCategory(categoryJson);
        log.info("Created new category: {}", createdCategory);

        SpendJson spend1 = spendDbClient.create(
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

        spendDbClient.remove(spend1);

        Assertions.assertThrows(RuntimeException.class, () -> spendDbClient.findById(spend1.id()));
    }


    @Test
    void shouldGetSpendByIdAndUpdate() {
        UUID spendId = UUID.fromString("67ecb6c0-bcd0-11f0-af9c-d62d6fb87ff1");
        SpendJson spend1 = spendDbClient.findById(spendId);
        var newCurrency = CurrencyValues.USD;
        var newAmount = randomDouble(100, 5000);

        SpendJson forUpdate = new SpendJson(spendId,
                spend1.spendDate(),
                spend1.category(),
                newCurrency,
                newAmount,
                spend1.description(),
                spend1.username());
        spendDbClient.update(forUpdate);

        SpendJson spend2 = spendDbClient.findById(spendId);

        Assertions.assertEquals(spend1.id(), spend2.id(), "Spends should be same");
        Assertions.assertEquals(newCurrency, spend2.currency(), "Spend should have a new currency");
        Assertions.assertEquals(newAmount, spend2.amount(), "Spend should have a new amount");
    }

    @Test
    void shouldGetSpendByUsernameAndDescription() {
        var username = "bonnie.ziemann";
        CategoryJson categoryJson = new CategoryJson(
                null,
                randomSentence(3),
                username,
                false
        );
        CategoryJson createdCategory = spendDbClient.createCategory(categoryJson);

        SpendJson created = spendDbClient.create(
                new SpendJson(null,
                        Date.from(Instant.now()),
                        createdCategory,
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
    void shouldCreateCategoryAndRemoveIt() {
        var username = "bonnie.ziemann";
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
