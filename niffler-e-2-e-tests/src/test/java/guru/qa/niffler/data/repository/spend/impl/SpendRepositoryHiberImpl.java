package guru.qa.niffler.data.repository.spend.impl;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.jpa.EntityManagers;
import guru.qa.niffler.data.repository.spend.SpendRepository;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;

public class SpendRepositoryHiberImpl implements SpendRepository {

    private final EntityManager em = EntityManagers.em(CFG.spendJdbcUrl());

    @Override
    public SpendEntity create(SpendEntity spend) {
        CategoryEntity category = spend.getCategory();
        if (category.getId() != null && !em.contains(category)) {
            CategoryEntity categoryRef = em.getReference(CategoryEntity.class, spend.getCategory().getId());
            spend.setCategory(categoryRef);
        }

        em.joinTransaction();
        em.persist(spend);

        return spend;
    }

    @Override
    public SpendEntity update(SpendEntity spend) {
        em.joinTransaction();
        em.merge(spend);

        return spend;
    }

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        em.joinTransaction();
        em.persist(category);

        return category;
    }

    @Override
    public CategoryEntity updateCategory(CategoryEntity category) {
        em.joinTransaction();
        em.merge(category);

        return category;
    }

    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {

        return Optional.ofNullable(em.find(CategoryEntity.class, id));
    }

    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        var sql = """
                FROM CategoryEntity c
                WHERE c.username=:username
                AND c.name=:categoryName
                """;

        return Optional.ofNullable(em.createQuery(sql, CategoryEntity.class)
                .setParameter("username", username)
                .setParameter("categoryName", categoryName)
                .getSingleResultOrNull());
    }

    @Override
    public List<CategoryEntity> findCategoriesByUsername(String username) {
        var sql = """
                FROM CategoryEntity c
                WHERE c.username=:username
                """;

        return em.createQuery(sql, CategoryEntity.class)
                .setParameter("username", username)
                .getResultList();
    }

    @Override
    public Optional<SpendEntity> findById(UUID id) {

        return Optional.ofNullable(em.find(SpendEntity.class, id));
    }

    @Override
    public Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String spendDescription) {
        var sql = """
                FROM SpendEntity s
                WHERE s.description=:spendDescription
                AND s.username=:username
                """;

        return Optional.ofNullable(em.createQuery(sql, SpendEntity.class)
                .setParameter("spendDescription", spendDescription)
                .setParameter("username", username)
                .getSingleResultOrNull());
    }

    @Override
    public void removeSpend(SpendEntity spend) {
        em.joinTransaction();
        SpendEntity attached = em.contains(spend) ? spend : em.merge(spend);
        em.remove(attached);
    }

    @Override
    public void removeCategory(CategoryEntity category) {
        em.joinTransaction();
        CategoryEntity attached = em.contains(category) ? category : em.merge(category);
        em.remove(attached);
    }
}
