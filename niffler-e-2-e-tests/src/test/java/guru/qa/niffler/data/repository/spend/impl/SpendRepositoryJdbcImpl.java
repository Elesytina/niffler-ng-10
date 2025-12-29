package guru.qa.niffler.data.repository.spend.impl;

import guru.qa.niffler.data.dao.spend.CategoryDao;
import guru.qa.niffler.data.dao.spend.SpendDao;
import guru.qa.niffler.data.dao.spend.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.spend.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.spend.SpendRepository;

import java.util.Optional;
import java.util.UUID;

public class SpendRepositoryJdbcImpl implements SpendRepository {

    private final SpendDao spendDao = new SpendDaoJdbc();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();

    @Override
    public SpendEntity create(SpendEntity spend) {

        return spendDao.create(spend);
    }

    @Override
    public SpendEntity update(SpendEntity spend) {

        return spendDao.update(spend);
    }

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {

        return categoryDao.create(category);
    }

    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {

        return categoryDao.findById(id);
    }

    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {

        return categoryDao.findByNameAndUsername(categoryName, username);
    }

    @Override
    public Optional<SpendEntity> findById(UUID id) {

        return spendDao.findById(id);
    }

    @Override
    public Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String spendDescription) {

        return spendDao.findByUsernameAndSpendDescription(username, spendDescription);
    }

    @Override
    public void removeSpend(SpendEntity spend) {
        spendDao.delete(spend);
    }

    @Override
    public void removeCategory(CategoryEntity category) {
        categoryDao.delete(category);
    }
}
