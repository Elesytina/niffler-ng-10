package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.SpendEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.getConnection;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

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
}
