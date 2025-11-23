package guru.qa.niffler.data.dao.spend.impl;

import guru.qa.niffler.data.dao.spend.CategoryDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.getHolder;
import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class CategoryDaoJdbc implements CategoryDao {

    @Override
    public Optional<CategoryEntity> findById(UUID id) {
        try (PreparedStatement ps = getHolder(CFG.spendJdbcUrl())
                .getConnection()
                .prepareStatement("SELECT * FROM category WHERE id = ?")) {
            ps.setObject(1, id);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                CategoryEntity category = getCategoryEntity(resultSet);

                return Optional.of(category);
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<CategoryEntity> findByNameAndUsername(String name, String username) {
        try (PreparedStatement ps = getHolder(CFG.spendJdbcUrl())
                .getConnection()
                .prepareStatement("SELECT * FROM category WHERE name = ? AND username = ?")) {
            ps.setString(1, name);
            ps.setString(2, username);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                CategoryEntity category = getCategoryEntity(resultSet);

                return Optional.of(category);
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CategoryEntity> findAllByUsername(String username) {
        try (PreparedStatement ps = getHolder(CFG.spendJdbcUrl())
                .getConnection()
                .prepareStatement("SELECT * FROM category WHERE username = ?")) {
            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();
            List<CategoryEntity> categories = new ArrayList<>();

            while (resultSet.next()) {
                CategoryEntity category = getCategoryEntity(resultSet);
                categories.add(category);
            }

            return categories;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CategoryEntity> findAll() {
        try (PreparedStatement ps = getHolder(CFG.spendJdbcUrl())
                .getConnection()
                .prepareStatement("SELECT * FROM category")) {
            ResultSet resultSet = ps.executeQuery();
            List<CategoryEntity> categories = new ArrayList<>();

            while (resultSet.next()) {
                CategoryEntity category = getCategoryEntity(resultSet);
                categories.add(category);
            }

            return categories;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CategoryEntity create(CategoryEntity entity) {
        try (PreparedStatement ps = getHolder(CFG.spendJdbcUrl())
                .getConnection()
                .prepareStatement("INSERT INTO category(name, username, archived) values (?,?,?)",
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

    @Override
    public CategoryEntity update(CategoryEntity entity) {
        try (PreparedStatement ps = getHolder(CFG.spendJdbcUrl())
                .getConnection()
                .prepareStatement("UPDATE category SET name = ?,  archived = ? WHERE id = ?")) {
            ps.setString(1, entity.getName());
            ps.setBoolean(2, entity.isArchived());
            ps.setObject(3, entity.getId());

            if (ps.executeUpdate() == 1) {

                return entity;
            }
            throw new RuntimeException("Failed to update category");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(CategoryEntity entity) {
        try (PreparedStatement ps = getHolder(CFG.spendJdbcUrl())
                .getConnection()
                .prepareStatement("DELETE FROM category where id = ?")) {
            ps.setObject(1, entity.getId());

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private CategoryEntity getCategoryEntity(ResultSet resultSet) throws SQLException {
        CategoryEntity category = new CategoryEntity();
        category.setId(resultSet.getObject("id", UUID.class));
        category.setName(resultSet.getString("name"));
        category.setUsername(resultSet.getString("username"));
        category.setArchived(resultSet.getBoolean("archived"));

        return category;
    }
}


