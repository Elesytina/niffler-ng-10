package guru.qa.niffler.data.dao.spend.impl;

import guru.qa.niffler.data.dao.spend.SpendDao;
import guru.qa.niffler.data.tpl.JdbcConnectionHolder;
import guru.qa.niffler.model.enums.CurrencyValues;
import guru.qa.niffler.model.enums.DateFilterValues;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static guru.qa.niffler.data.tpl.Connections.getHolder;
import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static guru.qa.niffler.model.enums.DateFilterValues.getSpendEndDate;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.time.LocalDate.now;

public class SpendDaoJdbc implements SpendDao {
    private final JdbcConnectionHolder connectionHolder = getHolder(CFG.spendJdbcUrl());

    @Override
    public SpendEntity create(SpendEntity entity) {
        try (PreparedStatement ps = connectionHolder.getConnection()
                .prepareStatement("INSERT INTO spend(username, spend_date, currency, amount, description, category_id) values (?,?,?,?,?,?)",
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
    public SpendEntity update(SpendEntity entity) {
        try (PreparedStatement ps = connectionHolder.getConnection()
                .prepareStatement("UPDATE spend set spend_date = ?, currency = ?, amount = ?, description = ?, category_id =? where id = ?")) {
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
    public boolean delete(List<UUID> ids) {
        try (PreparedStatement ps = connectionHolder.getConnection()
                .prepareStatement("DELETE FROM spend where id = ?")) {
            for (UUID id : ids) {
                ps.setObject(1, id);
                ps.addBatch();
                ps.clearParameters();
            }
            int[] deletingResults = ps.executeBatch();

            return Arrays.stream(deletingResults).allMatch(r -> r == 1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(SpendEntity spendEntity) {
        try (PreparedStatement ps = connectionHolder.getConnection()
                .prepareStatement("DELETE FROM spend where id = ?")) {
            ps.setObject(1, spendEntity.getId());

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<SpendEntity> findById(UUID id) {
        try (PreparedStatement ps = connectionHolder.getConnection()
                .prepareStatement("""
                        SELECT *
                        FROM spend
                        LEFT JOIN category
                        ON spend.category_id = category.id
                        WHERE spend.id = ?
                        """)) {
            ps.setObject(1, id);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                SpendEntity spend = getSpendEntity(resultSet);

                if (resultSet.getObject("category_id") != null) {
                    CategoryEntity category = getCategoryEntity(resultSet);
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
    public List<SpendEntity> findByUsername(String userName) {
        try (PreparedStatement ps = connectionHolder.getConnection()
                .prepareStatement(
                        """
                                SELECT * FROM spend
                                LEFT JOIN category
                                ON spend.category_id = category.id
                                WHERE spend.username = ?
                                """)) {
            ps.setString(1, userName);

            ResultSet resultSet = ps.executeQuery();
            List<SpendEntity> spends = new ArrayList<>();

            while (resultSet.next()) {
                SpendEntity spend = getSpendEntity(resultSet);
                if (resultSet.getObject("category_id") != null) {
                    CategoryEntity category = getCategoryEntity(resultSet);
                    spend.setCategory(category);
                }
                spends.add(spend);
            }

            return spends;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SpendEntity> findAllByFiltersAndUsername(CurrencyValues currencyFilter, DateFilterValues dateFilterValues, String userName) {
        try (PreparedStatement ps = connectionHolder
                .getConnection()
                .prepareStatement(
                        """
                                SELECT * FROM spend
                                LEFT JOIN category
                                ON spend.category_id = category.id
                                WHERE spend.username = ?
                                AND (? is null OR currency = ?)
                                AND (? is null OR spend_date BETWEEN ? AND ?)
                                """)) {
            var currency = currencyFilter == null ? null : currencyFilter.name();
            var date = dateFilterValues == null ? null : dateFilterValues.name();
            var dateStart = dateFilterValues == null ? null : java.sql.Date.valueOf(now());
            var dateEnd = dateFilterValues == null ? null : getSpendEndDate(dateFilterValues);
            ps.setString(1, userName);
            ps.setString(2, currency);
            ps.setString(3, currency);
            ps.setString(4, date);
            ps.setDate(5, dateStart);
            ps.setDate(6, dateEnd);
            ResultSet resultSet = ps.executeQuery();
            List<SpendEntity> spends = new ArrayList<>();

            while (resultSet.next()) {
                SpendEntity spend = getSpendEntity(resultSet);
                if (resultSet.getObject("category_id") != null) {
                    CategoryEntity category = getCategoryEntity(resultSet);
                    spend.setCategory(category);
                }
                spends.add(spend);
            }

            return spends;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SpendEntity> findAll() {
        try (PreparedStatement ps = connectionHolder
                .getConnection()
                .prepareStatement("SELECT * FROM spend")) {
            ResultSet resultSet = ps.executeQuery();
            List<SpendEntity> spends = new ArrayList<>();

            while (resultSet.next()) {
                SpendEntity spend = getSpendEntity(resultSet);
                spends.add(spend);
            }

            return spends;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private SpendEntity getSpendEntity(ResultSet resultSet) throws SQLException {
        SpendEntity spend = new SpendEntity();
        spend.setId(resultSet.getObject("id", UUID.class));
        spend.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
        spend.setUsername(resultSet.getString("username"));
        spend.setAmount(resultSet.getDouble("amount"));
        spend.setSpendDate(resultSet.getDate("spend_date"));
        spend.setDescription(resultSet.getString("description"));

        return spend;
    }

    private CategoryEntity getCategoryEntity(ResultSet resultSet) throws SQLException {
        CategoryEntity category = new CategoryEntity();
        category.setId(resultSet.getObject("category_id", UUID.class));
        category.setName(resultSet.getString("name"));
        category.setUsername(resultSet.getString("username"));
        category.setArchived(resultSet.getBoolean("archived"));
        return category;
    }
}
