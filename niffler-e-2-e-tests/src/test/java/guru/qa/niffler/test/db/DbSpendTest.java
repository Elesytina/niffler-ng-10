package guru.qa.niffler.test.db;

import guru.qa.niffler.data.dao.spend.CategoryDao;
import guru.qa.niffler.data.dao.spend.SpendDao;
import guru.qa.niffler.data.dao.spend.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.spend.impl.SpendDaoJdbc;
import guru.qa.niffler.data.dao.spend.impl.SpendDaoSpringJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.enums.CurrencyValues;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.utils.RandomDataUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class DbSpendTest {
    SpendDao dao = new SpendDaoJdbc();
    CategoryDao categoryDao = new CategoryDaoJdbc();
    SpendDaoSpringJdbc springDao = new SpendDaoSpringJdbc();

    @Test
    void shouldGetAllSpends() {
        List<SpendEntity> spend1 = dao.findAll();
        List<SpendEntity> spend2 = springDao.findAll();
        log.info(spend1.toString());
        log.info(spend2.toString());

        Assertions.assertEquals(spend1.size(), spend2.size(), "Spend list should be same");
    }

    @Test
    void shouldCreateAndDeleteSpend() {
        List<CategoryEntity> categoryEntities = categoryDao.findAllByUsername("fishka");
        Assertions.assertFalse(categoryEntities.isEmpty(), "Category list should not be empty");

        SpendEntity spend1 = dao.create(SpendEntity.fromJson(
                new SpendJson(null,
                        Date.from(Instant.now()),
                        CategoryJson.fromEntity(categoryEntities.getFirst()),
                        CurrencyValues.USD,
                        RandomDataUtils.randomDouble(10, 100),
                        RandomDataUtils.randomSentence(4),
                        "fishka")
        ));
        log.info(spend1.toString());

        SpendEntity spend2 = springDao.create(SpendEntity.fromJson(
                new SpendJson(null,
                        Date.from(Instant.now()),
                        CategoryJson.fromEntity(categoryEntities.getFirst()),
                        CurrencyValues.RUB,
                        RandomDataUtils.randomDouble(100, 10000),
                        RandomDataUtils.randomSentence(4),
                        "fishka")
        ));
        log.info(spend2.toString());

        Assertions.assertNotNull(spend1.getId(), "Spend id should not be null");
        Assertions.assertNotNull(spend2.getId(), "Spend id should not be null");

        Assertions.assertTrue(dao.delete(spend1), "Spend should have been deleted");
        Assertions.assertTrue(springDao.delete(spend2), "Spend should have been deleted");
    }

    @Test
    void shouldGetAllSpendsByUsername() {
        String username = "fishka";
        List<SpendEntity> spends1 = dao.findByUsername(username);
        List<SpendEntity> spends2 = springDao.findByUsername(username);
        log.info(spends1.toString());
        log.info(spends2.toString());

        Assertions.assertFalse(spends1.isEmpty(), "Spend list should not be empty");
        Assertions.assertFalse(spends2.isEmpty(), "Spend list should not be empty");

        Assertions.assertEquals(spends1.size(), spends2.size(), "Spends list should have the same size");
    }

    @Test
    void shouldGetSpendByIdAndUpdate() {
        String spendId = "67ecb6c0-bcd0-11f0-af9c-d62d6fb87ff1";
        Optional<SpendEntity> spend1 = dao.findById(UUID.fromString(spendId));
        Optional<SpendEntity> spend2 = springDao.findById(UUID.fromString(spendId));
        log.info(spend1.toString());
        log.info(spend2.toString());

        Assertions.assertTrue(spend1.isPresent(), "Spend should have been found");
        Assertions.assertTrue(spend2.isPresent(), "Spend should have been found");
        Assertions.assertEquals(spend1.get().getAmount(), spend2.get().getAmount(), "Spends should be same");

        spend1.get().setCurrency(CurrencyValues.KZT);
        spend2.get().setDescription(RandomDataUtils.randomSentence(5));

        SpendEntity updated1 = dao.update(spend1.get());
        SpendEntity updated2 = springDao.update(spend2.get());

        Optional<SpendEntity> spend = springDao.findById(updated1.getId());

        Assertions.assertEquals(updated1.getId(), updated2.getId(), "Spends should be same");
        Assertions.assertTrue(spend.isPresent(), "Spend should have been found");
        Assertions.assertEquals(spend.get().getCurrency(), updated1.getCurrency(), "Spend should have a new currency");
        Assertions.assertEquals(spend.get().getDescription(), updated2.getDescription(), "Spend should have a new description");

    }

}
