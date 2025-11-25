package guru.qa.niffler.data.dao.spend.impl;

import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.spend.CategoryDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.mapper.CategoryRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;

public class CategoryDaoSpringJdbc implements CategoryDao {

    private final DataSource dataSource = Databases.getDataSource(CFG.spendJdbcUrl());

    @Override
    public Optional<CategoryEntity> findById(UUID id) {
        JdbcTemplate template = new JdbcTemplate(dataSource);

        return Optional.ofNullable(template.queryForObject(
                "SELECT * FROM category WHERE id = ?",
                CategoryRowMapper.INSTANCE,
                id));
    }

    @Override
    public Optional<CategoryEntity> findByNameAndUsername(String name, String username) {
        JdbcTemplate template = new JdbcTemplate(dataSource);

        return Optional.ofNullable(template.queryForObject(
                "SELECT * FROM category WHERE name= ? and username = ?",
                CategoryRowMapper.INSTANCE,
                name,
                username));
    }

    @Override
    public List<CategoryEntity> findAllByUsername(String username) {
        JdbcTemplate template = new JdbcTemplate(dataSource);

        return template.query("SELECT * FROM category WHERE username = ?",
                CategoryRowMapper.INSTANCE,
                username);
    }

    @Override
    public List<CategoryEntity> findAll() {
        JdbcTemplate template = new JdbcTemplate(dataSource);

        return template.query("SELECT * FROM category",
                CategoryRowMapper.INSTANCE);
    }

    @Override
    public CategoryEntity create(CategoryEntity category) {
        JdbcTemplate template = new JdbcTemplate(dataSource);
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
        JdbcTemplate template = new JdbcTemplate(dataSource);

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
        JdbcTemplate template = new JdbcTemplate(dataSource);
        var result = template.update("DELETE FROM category where id = ?",
                entity.getId());

        return result == 1;
    }
}
