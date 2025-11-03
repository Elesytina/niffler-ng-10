package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.entity.CategoryEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.getConnection;
import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class CategoryDaoJdbc implements CategoryDao {

    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        try (Connection connection = getConnection(CFG.spendJdbcUrl());
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM category WHERE id = ?")) {
            ps.setObject(1, id);

            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                CategoryEntity category = new CategoryEntity();
                category.setId(id);
                category.setName(resultSet.getString("name"));
                category.setUsername(resultSet.getString("username"));
                category.setArchived(resultSet.getBoolean("archived"));
                return Optional.of(category);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CategoryEntity create(CategoryEntity entity) {
        try (Connection connection = getConnection(CFG.spendJdbcUrl());
             PreparedStatement ps = connection.prepareStatement("INSERT INTO category(name, username, archived) values (?,?,?)",
                     RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getUsername());
            ps.setBoolean(3, entity.isArchived());
            if (ps.executeUpdate() != 0) {
                ResultSet resultSet = ps.getGeneratedKeys();
                if (resultSet.next()) {
                    entity.setId(resultSet.getObject(1, UUID.class));
                }
                return entity;
            }
            throw new RuntimeException("Failed to insert new category");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


