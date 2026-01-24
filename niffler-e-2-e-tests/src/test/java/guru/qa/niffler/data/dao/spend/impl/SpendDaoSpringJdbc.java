package guru.qa.niffler.data.dao.spend.impl;

import guru.qa.niffler.data.dao.spend.SpendDao;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.mapper.SpendRowMapper;
import guru.qa.niffler.data.mapper.extractor.SpendResultSetExtractor;
import guru.qa.niffler.model.enums.CurrencyValues;
import guru.qa.niffler.model.enums.DateFilterValues;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.DataSources.getDataSource;
import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static guru.qa.niffler.model.enums.DateFilterValues.getSpendEndDate;
import static java.time.LocalDate.now;

@ParametersAreNonnullByDefault
public class SpendDaoSpringJdbc implements SpendDao {

    private final JdbcTemplate template = new JdbcTemplate(getDataSource(CFG.spendJdbcUrl()));

    @Override
    public Optional<SpendEntity> findById(UUID id) {

        return Optional.ofNullable(template.queryForObject(
                """
                        SELECT * FROM spend sp
                        LEFT JOIN category c
                        ON sp.category_id = c.id
                        WHERE sp.id = ?
                        """,
                SpendRowMapper.INSTANCE,
                id));
    }

    @Override
    public @Nullable List<SpendEntity> findByUsername(String userName) {
        try {
            return template.query(
                    """
                            
                            SELECT sp.id as id,
                                sp.username as username,
                                sp.description as description,
                                sp.category_id as category_id,
                                sp.currency as currency,
                                sp.amount as amount,
                                sp.spend_date as spend_date,
                                c.name as category_name,
                                c.archived as archived
                                FROM spend sp
                                LEFT JOIN category c
                                ON sp.category_id = c.id
                                WHERE sp.username = ?
                            """,
                    SpendResultSetExtractor.
                            INSTANCE,
                    userName);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String spendDescription) {

        return Optional.ofNullable(template.queryForObject(
                """
                        SELECT * FROM spend sp
                        LEFT JOIN category c
                        ON sp.category_id = c.id
                        WHERE sp.username = ?
                        AND sp.description = ?
                        """,
                SpendRowMapper.INSTANCE,
                username, spendDescription));
    }

    @Override
    public @Nonnull List<SpendEntity> findAllByFiltersAndUsername(@Nullable CurrencyValues currencyFilter,
                                                                  @Nullable DateFilterValues dateFilterValues,
                                                                  String userName) {

        return template.query(conn -> {
            PreparedStatement ps = conn.prepareStatement(
                    """
                            SELECT * FROM spend
                            LEFT JOIN category
                            ON spend.category_id = category.id
                            WHERE spend.username = ?
                            AND (? is null OR currency = ?)
                            AND (? is null OR spend_date BETWEEN ? AND ?)
                            """);
            var currency = currencyFilter == null ? null : currencyFilter.name();
            var date = dateFilterValues == null ? null : dateFilterValues.name();
            var dateStart = dateFilterValues == null ? null : Date.valueOf(now());
            var dateEnd = dateFilterValues == null ? null : getSpendEndDate(dateFilterValues);
            ps.setString(1, userName);
            ps.setString(2, currency);
            ps.setString(3, currency);
            ps.setString(4, date);
            ps.setDate(5, dateStart);
            ps.setDate(6, dateEnd);
            return ps;

        }, SpendRowMapper.INSTANCE);
    }

    @Override
    public @Nonnull List<SpendEntity> findAll() {
        return template.query("SELECT * FROM spend s LEFT JOIN category c ON s.category_id = c.id",
                SpendRowMapper.INSTANCE);
    }

    @Override
    public @Nullable SpendEntity create(SpendEntity entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(conn -> {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO spend(username, spend_date, currency, amount, description, category_id) values (?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getUsername());
            ps.setDate(2, entity.getSpendDate());
            ps.setString(3, entity.getCurrency().name());
            ps.setDouble(4, entity.getAmount());
            ps.setString(5, entity.getDescription());
            ps.setObject(6, entity.getCategory().getId());

            return ps;
        }, keyHolder);

        final UUID id = (UUID) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        entity.setId(id);

        return entity;
    }

    @Override
    public @Nullable SpendEntity update(SpendEntity spendEntity) {
        var result = template.update("UPDATE spend set spend_date = ?, currency = ?, amount = ?, description = ?, category_id =? where id = ?",
                spendEntity.getSpendDate(),
                spendEntity.getCurrency().name(),
                spendEntity.getAmount(),
                spendEntity.getDescription(),
                spendEntity.getCategory().getId(),
                spendEntity.getId());

        if (result != 1) {
            throw new RuntimeException("Update failed");
        }

        return spendEntity;
    }

    @Override
    public boolean delete(List<UUID> ids) {
        var result = template.update("DELETE FROM spend where id IN (?)",
                ids.toArray());

        return result == 1;
    }

    @Override
    public boolean delete(SpendEntity spendEntity) {
        var result = template.update("DELETE FROM spend where id = ?",
                spendEntity.getId());

        return result == 1;
    }
}
