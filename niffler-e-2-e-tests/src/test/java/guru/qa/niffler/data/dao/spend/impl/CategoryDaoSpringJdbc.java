package guru.qa.niffler.data.dao.spend.impl;

import guru.qa.niffler.data.dao.spend.CategoryDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.mapper.CategoryRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.DataSources.getDataSource;
import static guru.qa.niffler.helper.TestConstantHolder.CFG;

public class CategoryDaoSpringJdbc implements CategoryDao {
    private final JdbcTemplate template = new JdbcTemplate(getDataSource(CFG.spendJdbcUrl()));

    @Override
    public Optional<CategoryEntity> findById(UUID id) {

        return Optional.ofNullable(template.queryForObject(
                "SELECT * FROM category WHERE id = ?",
                CategoryRowMapper.INSTANCE,
                id));
    }

    @Override
    public Optional<CategoryEntity> findByNameAndUsername(String name, String username) {
        try {
            CategoryEntity categoryEntity = template.queryForObject(
                    "SELECT * FROM category WHERE name= ? and username = ?",
                    CategoryRowMapper.INSTANCE,
                    name,
                    username);

            return Optional.ofNullable(categoryEntity);
        } catch (EmptyResultDataAccessException e) {

            return Optional.empty();
        }
    }

    @Override
    public List<CategoryEntity> findAllByUsername(String username) {

        return template.query("SELECT * FROM category WHERE username = ?",
                CategoryRowMapper.INSTANCE,
                username);
    }

    @Override
    public List<CategoryEntity> findAll() {

        return template.query("SELECT * FROM category",
                CategoryRowMapper.INSTANCE);
    }

    @Override
    public CategoryEntity create(CategoryEntity category) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(conn -> {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO category(name, username, archived) values (?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, category.getName());
            ps.setString(2, category.getUsername());
            ps.setBoolean(3, category.isArchived());

            return ps;
        }, keyHolder);

        final UUID id = (UUID) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        category.setId(id);

        return category;
    }

    @Override
    public CategoryEntity update(CategoryEntity entity) {
        int result = template.update("UPDATE category SET name = ?, archived = ? WHERE id = ?",
                entity.getName(),
                entity.isArchived(),
                entity.getId());

        if (result != 1) {
            throw new RuntimeException("Update failed");
        }

        return entity;
    }

    @Override
    public boolean delete(CategoryEntity entity) {
        var result = template.update("DELETE FROM category where id = ?",
                entity.getId());

        return result == 1;
    }
}
