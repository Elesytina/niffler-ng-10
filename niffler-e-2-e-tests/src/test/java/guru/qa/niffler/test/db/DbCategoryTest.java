package guru.qa.niffler.test.db;

import guru.qa.niffler.data.dao.spend.CategoryDao;
import guru.qa.niffler.data.dao.spend.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.spend.impl.CategoryDaoSpringJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.utils.RandomDataUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class DbCategoryTest {
    CategoryDao dao = new CategoryDaoJdbc();
    CategoryDao springDao = new CategoryDaoSpringJdbc();

    @Test
    void shouldGetAllCategories() {
        List<CategoryEntity> categories1 = dao.findAll();
        List<CategoryEntity> categories2 = springDao.findAll();
        log.info(categories1.toString());
        log.info(categories2.toString());

        Assertions.assertEquals(categories1.size(), categories2.size(), "Category list should be same");
    }

    @Test
    void shouldCreateAndDeleteCategory() {
        CategoryEntity category1 = dao.create(CategoryEntity.fromJson(
                new CategoryJson(null,
                        RandomDataUtils.randomCategoryName(),
                        "fishka",
                        true)
        ));
        log.info(category1.toString());

        CategoryEntity category2 = springDao.create(CategoryEntity.fromJson(
                new CategoryJson(null,
                        RandomDataUtils.randomCategoryName(),
                        "fishka",
                        true)
        ));
        log.info(category2.toString());

        Assertions.assertNotNull(category1.getId(), "Category id should not be null");
        Assertions.assertNotNull(category2.getId(), "Category id should not be null");

        Assertions.assertTrue(dao.delete(category1), "Category should have been deleted");
        Assertions.assertTrue(springDao.delete(category2), "Category should have been deleted");
    }

    @Test
    void shouldGetAllCategoriesByUsernameOrByName() {
        String username = "fishka";
        List<CategoryEntity> categories1 = dao.findAllByUsername(username);
        List<CategoryEntity> categories2 = springDao.findAllByUsername(username);
        log.info(categories1.toString());
        log.info(categories2.toString());

        Assertions.assertFalse(categories1.isEmpty(), "Category list should not be empty");
        Assertions.assertFalse(categories2.isEmpty(), "Category list should not be empty");

        Optional<CategoryEntity> category1 = dao.findByNameAndUsername(categories1.getFirst().getName(), username);
        Optional<CategoryEntity> category2 = springDao.findByNameAndUsername(categories2.getFirst().getName(), username);

        Assertions.assertTrue(category1.isPresent(), "Category should have been found");
        Assertions.assertTrue(category2.isPresent(), "Category should have been found");

        Assertions.assertEquals(category1.get().getId(), category2.get().getId(), "Categories should have the same id");
    }

    @Test
    void shouldGetCategoryByIdAndUpdate() {
        String categoryId = "f286ecf8-6c72-47ca-ad23-e3a9a0d0f4aa";
        Optional<CategoryEntity> category1 = dao.findById(UUID.fromString(categoryId));
        Optional<CategoryEntity> category2 = springDao.findById(UUID.fromString(categoryId));
        log.info(category1.toString());
        log.info(category2.toString());

        Assertions.assertTrue(category1.isPresent(), "Category should have been found");
        Assertions.assertTrue(category2.isPresent(), "Category should have been found");
        Assertions.assertEquals(category1.get().getName(), category2.get().getName(), "Categories should be same");

        category1.get().setName("newCatName1");
        category2.get().setName("newCatName2");

        CategoryEntity updated1 = dao.update(category1.get());
        CategoryEntity updated2 = springDao.update(category2.get());

        Optional<CategoryEntity> category = springDao.findById(updated1.getId());

        Assertions.assertEquals(updated1.getId(), updated2.getId(), "Categories should be same");
        Assertions.assertTrue(category.isPresent(), "Category should have been found");
        Assertions.assertEquals(category.get().getName(), updated2.getName(), "Category should have a new name");
    }

}
