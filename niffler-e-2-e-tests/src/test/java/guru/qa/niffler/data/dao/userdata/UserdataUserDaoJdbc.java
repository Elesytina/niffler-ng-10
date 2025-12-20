package guru.qa.niffler.data.dao.userdata;

import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.tpl.JdbcConnectionHolder;
import guru.qa.niffler.model.enums.CurrencyValues;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.getHolder;
import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class UserdataUserDaoJdbc implements UserdataUserDao {
    private final JdbcConnectionHolder connectionHolder = getHolder(CFG.userdataJdbcUrl());

    @Override
    public Optional<UserEntity> findById(UUID id) {
        try (PreparedStatement ps = connectionHolder.getConnection()
                .prepareStatement("SELECT * FROM \"user\" WHERE id = ?")) {
            ps.setObject(1, id);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                UserEntity entity = getUserEntity(resultSet);

                return Optional.of(entity);
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        try (PreparedStatement ps = connectionHolder.getConnection()
                .prepareStatement("SELECT * FROM \"user\" WHERE username = ?")) {
            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                UserEntity entity = getUserEntity(resultSet);

                return Optional.of(entity);
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserEntity create(UserEntity entity) {
        try (PreparedStatement ps = connectionHolder.getConnection()
                .prepareStatement("INSERT INTO \"user\"( username, currency, firstname, surname, photo, photo_small, full_name) values (?,?,?, ?,?,?,?)",
                        RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getUsername());
            ps.setString(2, entity.getCurrency().name());
            ps.setString(3, entity.getFirstname());
            ps.setString(4, entity.getSurname());
            ps.setBytes(5, entity.getPhoto());
            ps.setBytes(6, entity.getPhotoSmall());
            ps.setString(7, entity.getFullname());

            if (ps.executeUpdate() != 0) {
                ResultSet resultSet = ps.getGeneratedKeys();

                if (resultSet.next()) {
                    entity.setId(resultSet.getObject(1, UUID.class));
                }

                return entity;
            }
            throw new RuntimeException("Failed to insert new user");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(UserEntity entity) {
        try (PreparedStatement ps = connectionHolder.getConnection()
                .prepareStatement("DELETE FROM \"user\" where id = ?")) {
            ps.setObject(1, entity.getId());

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private UserEntity getUserEntity(ResultSet resultSet) throws SQLException {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(resultSet.getObject("id", UUID.class));
        userEntity.setFullname(resultSet.getString("full_name"));
        userEntity.setUsername(resultSet.getString("username"));
        userEntity.setFirstname(resultSet.getString("firstname"));
        userEntity.setSurname(resultSet.getString("surname"));
        userEntity.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
        userEntity.setPhoto(resultSet.getBytes("photo"));
        userEntity.setPhotoSmall(resultSet.getBytes("photo_small"));

        return userEntity;
    }
}
