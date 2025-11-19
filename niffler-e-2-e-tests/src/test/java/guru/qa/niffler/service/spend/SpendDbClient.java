package guru.qa.niffler.service.spend;

import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.spend.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.spend.impl.SpendDaoJdbc;
import guru.qa.niffler.data.dao.spend.impl.SpendDaoSpringJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.enums.CurrencyValues;
import guru.qa.niffler.model.enums.DateFilterValues;
import guru.qa.niffler.model.spend.SpendJson;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;

@Slf4j
public class SpendDbClient implements SpendClient {

    @Override
    public SpendJson getSpendById(UUID id) {
        return Databases.xaTransaction(new Databases.XaFunction<>(connect -> {
            Optional<SpendEntity> spendEntity = new SpendDaoJdbc(connect).findById(id);

            if (spendEntity.isPresent()) {

                return SpendJson.fromEntity(spendEntity.get());
            }
            throw new RuntimeException("Failed to find spend");
        }, CFG.spendJdbcUrl()));
    }

    @Override
    public List<SpendJson> getAllSpendsByFiltersAndUsername(CurrencyValues currencyFilter, DateFilterValues dateFilterValues, String userName) {
        return Databases.xaTransaction(new Databases.XaFunction<>(connect -> {
            List<SpendEntity> spendEntities = new SpendDaoJdbc(connect).findAllByFiltersAndUsername(currencyFilter, dateFilterValues, userName);

            return spendEntities.stream()
                    .map(SpendJson::fromEntity)
                    .toList();
        }, CFG.spendJdbcUrl()));
    }

    @Override
    public List<SpendJson> getAllSpendsByUsername(String userName) {
        return Databases.xaTransaction(new Databases.XaFunction<>(connect -> {
            List<SpendEntity> spendEntities = new SpendDaoJdbc(connect).findByUsername(userName);

            return spendEntities.stream()
                    .map(SpendJson::fromEntity)
                    .toList();
        }, CFG.spendJdbcUrl()));
    }

    @Override
    public SpendJson createSpend(SpendJson spend) {
        return Databases.xaTransaction(new Databases.XaFunction<>(connect -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);

            if (spendEntity.getCategory().getId() == null) {
                CategoryEntity categoryEntity = new CategoryDaoJdbc(connect).create(spendEntity.getCategory());
                spendEntity.setCategory(categoryEntity);
            }

            SpendEntity created = new SpendDaoJdbc(connect).create(spendEntity);
            return SpendJson.fromEntity(created);
        }, CFG.spendJdbcUrl()));
    }

    @Override
    public SpendJson updateSpend(SpendJson spendJson) {
        return Databases.xaTransaction(new Databases.XaFunction<>(connect -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spendJson);
            SpendEntity updated = new SpendDaoJdbc(connect).update(spendEntity);

            return SpendJson.fromEntity(updated);
        }, CFG.spendJdbcUrl()));
    }

    public void deleteSpends(List<UUID> ids) {
        Databases.xaTransaction(new Databases.XaConsumer(connect -> {
            boolean isSuccess = new SpendDaoJdbc(connect).delete(ids);

            if (!isSuccess) {
                throw new RuntimeException("Failed to delete spends");
            }
        }, CFG.spendJdbcUrl()));
    }

    @Override
    public void deleteSpend(SpendJson spend) {
        Databases.xaTransaction(new Databases.XaConsumer(connect -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            boolean isSuccess = new SpendDaoJdbc(connect).delete(spendEntity);

            if (!isSuccess) {
                throw new RuntimeException("Failed to delete spends");
            }
        }, CFG.spendJdbcUrl()));
    }

    public List<SpendJson> getAllSpends() {
        return Databases.transaction(connect -> {
            List<SpendEntity> spendEntities = new SpendDaoJdbc(connect).findAll();

            return spendEntities.stream()
                    .map(SpendJson::fromEntity)
                    .toList();
        }, CFG.spendJdbcUrl());
    }

}
