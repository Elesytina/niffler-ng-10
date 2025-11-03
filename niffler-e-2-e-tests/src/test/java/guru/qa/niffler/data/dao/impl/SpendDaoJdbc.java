package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DateFilterValues;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.getConnection;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.time.LocalDate.now;

public class SpendDaoJdbc implements SpendDao {

    private final Config CFG = Config.getInstance();

    @Override
    public SpendEntity create(SpendEntity entity) {
        try (Connection connection = getConnection(CFG.spendJdbcUrl());
             PreparedStatement ps = connection.prepareStatement("INSERT INTO spend(username, spend_date,currency, amount, description, category_id) values (?,?,?,?,?,?)",
                     RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getUsername());
            ps.setDate(2, entity.getSpendDate());
            ps.setString(3, entity.getCurrency().name());
            ps.setDouble(4, entity.getAmount());
            ps.setString(5, entity.getDescription());
            ps.setObject(6, entity.getCategory().getId());

            if (ps.executeUpdate() != 0) {
                ResultSet resultSet = ps.getGeneratedKeys();

                if (resultSet.next()) {
                    entity.setId(resultSet.getObject(1, UUID.class));
                }

                return entity;
            }
            throw new RuntimeException("Failed to insert new spend");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SpendEntity updateSpend(SpendEntity entity) {
        try (Connection connection = getConnection(CFG.spendJdbcUrl());
             PreparedStatement ps = connection.prepareStatement("UPDATE spend set spend_date = ?, currency = ?, amount = ?, description = ?, category_id =? where id = ?")) {
            ps.setDate(1, entity.getSpendDate());
            ps.setString(2, entity.getCurrency().name());
            ps.setDouble(3, entity.getAmount());
            ps.setString(4, entity.getDescription());
            ps.setObject(5, entity.getCategory().getId());
            ps.setObject(6, entity.getId());

            if (ps.executeUpdate() != 0) {

                return entity;
            }
            throw new RuntimeException("Failed to update spend");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteSpends(List<UUID> ids, String userName) {
        try (Connection connection = getConnection(CFG.spendJdbcUrl());
             PreparedStatement ps = connection.prepareStatement("DELETE FROM spend where id in(?) and username = ?")) {
            ps.setObject(1, ids);
            ps.setObject(2, userName);

            return ps.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<SpendEntity> getSpend(UUID id) {
        try (Connection connection = getConnection(CFG.spendJdbcUrl());
             PreparedStatement ps = connection.prepareStatement("""
                     SELECT *
                     FROM spend
                     LEFT JOIN category
                     ON spend.category_id = category.id
                     WHERE spend.id = ?
                     """)) {
            ps.setObject(1, id);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                SpendEntity spend = new SpendEntity();
                spend.setId(id);
                spend.setDescription(resultSet.getString("description"));
                spend.setUsername(resultSet.getString("username"));
                spend.setAmount(resultSet.getDouble("amount"));
                spend.setSpendDate(resultSet.getDate("spend_date"));
                if (resultSet.getObject("category_id") != null) {
                    CategoryEntity category = new CategoryEntity();
                    category.setId(resultSet.getObject("category_id", UUID.class));
                    category.setName(resultSet.getString("name"));
                    category.setUsername(resultSet.getString("username"));
                    category.setArchived(resultSet.getBoolean("archived"));
                    spend.setCategory(category);
                }

                return Optional.of(spend);
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SpendEntity> getSpends(CurrencyValues currencyFilter, DateFilterValues dateFilterValues, String userName) {
        try (Connection connection = getConnection(CFG.spendJdbcUrl());
             PreparedStatement ps = connection.prepareStatement(
                     """
                             SELECT * FROM spend
                             LEFT JOIN category
                             ON spend.category_id = category.id
                             WHERE spend.username = ?
                             AND (? is null OR currency = ?)
                             AND (? is null OR spend_date BETWEEN ? AND ?)
                             """)) {
            ps.setString(1, userName);
            ps.setString(2, currencyFilter.name());
            ps.setString(3, currencyFilter.name());
            ps.setString(4, dateFilterValues.name());
            ps.setDate(5, java.sql.Date.valueOf(now()));
            ps.setDate(6, java.sql.Date.valueOf(getSpendEndDate(dateFilterValues)));
            ResultSet resultSet = ps.executeQuery();
            List<SpendEntity> spends = new ArrayList<>();

            while (resultSet.next()) {
                SpendEntity spend = new SpendEntity();
                spend.setId(resultSet.getObject("id", UUID.class));
                spend.setCurrency(resultSet.getObject("currency", CurrencyValues.class));
                spend.setUsername(resultSet.getString("username"));
                spend.setAmount(resultSet.getDouble("amount"));
                spend.setSpendDate(resultSet.getDate("spend_date"));
                spend.setDescription(resultSet.getString("description"));
                if (resultSet.getObject("category_id") != null) {
                    CategoryEntity category = new CategoryEntity();
                    category.setId(resultSet.getObject("category_id", UUID.class));
                    category.setName(resultSet.getString("name"));
                    category.setUsername(resultSet.getString("username"));
                    category.setArchived(resultSet.getBoolean("archived"));
                    spend.setCategory(category);
                    spends.add(spend);
                }
            }

            return spends;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private LocalDate getSpendEndDate(DateFilterValues dateFilterValues) {

        return switch (dateFilterValues) {
            case TODAY -> now();
            case WEEK -> now().plusWeeks(1);
            case MONTH -> now().plusMonths(1);
        };
    }
}
