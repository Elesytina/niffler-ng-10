package guru.qa.niffler.service.spend;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DateFilterValues;
import guru.qa.niffler.model.SpendJson;

import java.util.List;

public class SpendDbClient implements SpendClient {

    private final SpendDao spendDao = new SpendDaoJdbc();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();

    @Override
    public SpendJson createSpend(SpendJson spend) {
        SpendEntity spendEntity = SpendEntity.fromJson(spend);

        if (spendEntity.getCategory().getId() == null) {
            CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
            spendEntity.setCategory(categoryEntity);
        }

        return SpendJson.fromEntity(spendDao.create(spendEntity));
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
    public SpendJson getSpend(String id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<SpendJson> getSpends(CurrencyValues currencyFilter, DateFilterValues dateFilterValues, String userName) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
